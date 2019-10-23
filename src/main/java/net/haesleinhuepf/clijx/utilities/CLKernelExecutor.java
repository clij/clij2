package net.haesleinhuepf.clijx.utilities;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.*;
import net.haesleinhuepf.clij.clearcl.enums.ImageChannelDataType;
import net.haesleinhuepf.clij.clearcl.exceptions.OpenCLException;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.clearcl.util.ElapsedTime;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This executor can call OpenCL files. It
 * uses some functionality adapted from FastFuse, to make .cl file handling
 * easier. For example, it ensures that the right
 * image_read/image_write methods are called depending on the image
 * type.
 * <p>
 * Author: Robert Haase (http://haesleinhuepf.net) at MPI CBG (http://mpi-cbg.de)
 * February 2018
 */
public class CLKernelExecutor {
    public static int MAX_ARRAY_SIZE = 1000;
    ClearCLContext context;
    Class anchorClass;
    String programFilename;
    String kernelName;
    Map<String, Object> parameterMap;
    long[] globalSizes;

    private final HashMap<String, ClearCLProgram> programCacheMap = new HashMap();
    ClearCLProgram currentProgram = null;

    public CLKernelExecutor(ClearCLContext context) {
        super();
        this.context = context;
    }

    public static void getOpenCLDefines(Map<String, Object> defines, String key, ImageChannelDataType imageChannelDataType, int dimension) {

        String sizeParameters = "long image_size_" + key + "_width, long image_size_" + key + "_height, long image_size_" + key + "_depth, ";

        if (key.contains("dst") || key.contains("destination") || key.contains("output")) {
            if (dimension == 3) {
                defines.put("IMAGE_" + key + "_TYPE", sizeParameters + "__write_only image3d_t");
            } else if (dimension == 2) {
                defines.put("IMAGE_" + key + "_TYPE", sizeParameters + "__write_only image2d_t");
            }
        } else {
            if (dimension == 3) {
                defines.put("IMAGE_" + key + "_TYPE", sizeParameters + "__read_only image3d_t");
            } else if (dimension == 2) {
                defines.put("IMAGE_" + key + "_TYPE", sizeParameters + "__read_only image2d_t");
            }
        }

        defines.put("READ_" + key + "_IMAGE", imageChannelDataType.isInteger() ? "read_imageui" : "read_imagef");
        defines.put("WRITE_" + key + "_IMAGE", imageChannelDataType.isInteger() ? "write_imageui" : "write_imagef");

        switch (imageChannelDataType)
        {
            case Float:
                defines.put("IMAGE_" + key + "_PIXEL_TYPE", "float");
                defines.put("CONVERT_" + key + "_PIXEL_TYPE", "clij_convert_float_sat");
                break;
            case UnsignedInt8:
                defines.put("IMAGE_" + key + "_PIXEL_TYPE", "uchar");
                defines.put("CONVERT_" + key + "_PIXEL_TYPE(parameter)", "clij_convert_uchar_sat(parameter)");
                break;
            case SignedInt8:
                defines.put("IMAGE_" + key + "_PIXEL_TYPE", "char");
                defines.put("CONVERT_" + key + "_PIXEL_TYPE(parameter)", "clij_convert_char_sat(parameter)");
                break;
            case SignedInt32:
                defines.put("IMAGE_" + key + "_PIXEL_TYPE", "int");
                defines.put("CONVERT_" + key + "_PIXEL_TYPE(parameter)", "clij_convert_int_sat(parameter)");
                break;
            default: // UnsignedInt16, TODO: throw exception if different
                defines.put("IMAGE_" + key + "_PIXEL_TYPE", "ushort");
                defines.put("CONVERT_" + key + "_PIXEL_TYPE(parameter)", "clij_convert_ushort_sat(parameter)");
                break;
        }
    }

    public static void getOpenCLDefines(Map<String, Object> defines, String key, NativeTypeEnum nativeTypeEnum, int dimension) {
        String typeName = nativeTypeToOpenCLTypeName(nativeTypeEnum);
        String typeId = nativeTypeToOpenCLTypeShortName(nativeTypeEnum);

        String sizeParameters = "long image_size_" + key + "_width, long image_size_" + key + "_height, long image_size_" + key + "_depth, ";

        String sat = "_sat"; //typeName.compareTo("float")==0?"":"_sat";

        defines.put("IMAGE_" + key + "_PIXEL_TYPE", typeName);
        defines.put("CONVERT_" + key + "_PIXEL_TYPE", "clij_convert_" + typeName + sat);
        defines.put("IMAGE_" + key + "_TYPE", sizeParameters + "__global " + typeName + "*");

        if (dimension == 2) {
            defines.put("READ_" + key + "_IMAGE(a,b,c)", "read_buffer2d" + typeId + "(GET_IMAGE_WIDTH(a),GET_IMAGE_HEIGHT(a),GET_IMAGE_DEPTH(a),a,b,c)");
            defines.put("WRITE_" + key + "_IMAGE(a,b,c)", "write_buffer2d" + typeId + "(GET_IMAGE_WIDTH(a),GET_IMAGE_HEIGHT(a),GET_IMAGE_DEPTH(a),a,b,c)");
        } else if (dimension == 3) {
            defines.put("READ_" + key + "_IMAGE(a,b,c)", "read_buffer3d" + typeId + "(GET_IMAGE_WIDTH(a),GET_IMAGE_HEIGHT(a),GET_IMAGE_DEPTH(a),a,b,c)");
            defines.put("WRITE_" + key + "_IMAGE(a,b,c)", "write_buffer3d" + typeId + "(GET_IMAGE_WIDTH(a),GET_IMAGE_HEIGHT(a),GET_IMAGE_DEPTH(a),a,b,c)");
        }
    }

    private static String nativeTypeToOpenCLTypeName(NativeTypeEnum pDType) {
        if (pDType == NativeTypeEnum.Byte) {
            return "char";
        } else if (pDType == NativeTypeEnum.UnsignedByte) {
            return "uchar";
        } else if (pDType == NativeTypeEnum.Short) {
            return "short";
        } else if (pDType == NativeTypeEnum.UnsignedShort) {
            return "ushort";
        } else if (pDType == NativeTypeEnum.Float) {
            return "float";
        } else {
            return "";
        }
    }

    private static String nativeTypeToOpenCLTypeShortName(NativeTypeEnum pDType) {
        if (pDType == NativeTypeEnum.Byte) {
            return "c";
        } else if (pDType == NativeTypeEnum.UnsignedByte) {
            return "uc";
        } else if (pDType == NativeTypeEnum.Short) {
            return "i";
        } else if (pDType == NativeTypeEnum.UnsignedShort) {
            return "ui";
        } else if (pDType == NativeTypeEnum.Float) {
            return "f";
        } else {
            return "";
        }
    }

    /**
     * Map of all parameters. It is recommended that input and output
     * images are given with the names "src" and "dst", respectively.
     *
     * @param parameterMap
     */
    public void setParameterMap(Map<String, Object> parameterMap) {
        this.parameterMap = parameterMap;
    }

    public ClearCLKernel enqueue(boolean waitToFinish) {
        return enqueue(waitToFinish, null);
    }

    public ClearCLKernel enqueue(boolean waitToFinish, ClearCLKernel kernel) {

        if (kernel == null) {

            if (CLIJ.debug) {
                System.out.println("Loading " + kernelName);
            }

            Map<String, Object> openCLDefines = new HashMap();

            // deal with image width/height/depth for all images and buffers
            ArrayList<String> definedParameterKeys = new ArrayList<String>();
            for (String key : parameterMap.keySet()) {
                if (parameterMap.get(key) instanceof ClearCLImage) {
                    ClearCLImage image = (ClearCLImage) parameterMap.get(key);
//                    openCLDefines.put("IMAGE_SIZE_" + key + "_WIDTH", image.getWidth());
//                    openCLDefines.put("IMAGE_SIZE_" + key + "_HEIGHT", image.getHeight());
//                    openCLDefines.put("IMAGE_SIZE_" + key + "_DEPTH", image.getDepth());
//
                    getOpenCLDefines(openCLDefines, key, image.getChannelDataType(), (int) image.getDimension());
                } else if (parameterMap.get(key) instanceof ClearCLBuffer) {
                    ClearCLBuffer image = (ClearCLBuffer) parameterMap.get(key);
//                    openCLDefines.put("IMAGE_SIZE_" + key + "_WIDTH", image.getWidth());
//                    openCLDefines.put("IMAGE_SIZE_" + key + "_HEIGHT", image.getHeight());
//                    openCLDefines.put("IMAGE_SIZE_" + key + "_DEPTH", image.getDepth());
//
                    getOpenCLDefines(openCLDefines, key, image.getNativeType(), (int) image.getDimension());
                }
                definedParameterKeys.add(key);
            }

            // add undefined parameters to define list
//            ArrayList<String> variableNames = getImageVariablesFromSource();
//            for (String variableName : variableNames) {
//
//                boolean existsAlready = false;
//                for (String key : definedParameterKeys) {
//                    if (key.compareTo(variableName) == 0) {
//                        existsAlready = true;
//                        break;
//                    }
//                }
//                if (!existsAlready) {
//                    //openCLDefines.put("IMAGE_SIZE_" + variableName + "_WIDTH", 0);
//                    //openCLDefines.put("IMAGE_SIZE_" + variableName + "_HEIGHT", 0);
//                    //openCLDefines.put("IMAGE_SIZE_" + variableName + "_DEPTH", 0);
//                }
//            }

            openCLDefines.put("GET_IMAGE_WIDTH(image_key)", "image_size_ ## image_key ## _width");
            openCLDefines.put("GET_IMAGE_HEIGHT(image_key)", "image_size_ ## image_key ## _height");
            openCLDefines.put("GET_IMAGE_DEPTH(image_key)", "image_size_ ## image_key ## _depth");

            System.out.println("Hello world " + CLIJ.debug);
            if (CLIJ.debug) {
                for (String key : openCLDefines.keySet()) {
                    System.out.println(key + " = " + openCLDefines.get(key));
                }
            }


            try {

                if (openCLDefines != null) {
                    kernel = getKernel(context, kernelName, openCLDefines);
                } else {
                    kernel = getKernel(context, kernelName);
                }

            } catch (IOException e1) {
                e1.printStackTrace();
                return null;
            }
        }
        if (kernel != null) {
            if (globalSizes != null) {
                kernel.setGlobalSizes(globalSizes);
            }/* else if (dstImage != null) {
                clearCLKernel.setGlobalSizes(dstImage.getDimensions());
            } else if (dstBuffer != null) {
                clearCLKernel.setGlobalSizes(dstBuffer.getDimensions());
            }*/
            if (parameterMap != null) {
                for (String key : parameterMap.keySet()) {
                    Object obj = parameterMap.get(key);
                    kernel.setArgument(key, obj);
                    if (obj instanceof ClearCLImageInterface) {
                        kernel.setArgument("image_size_" + key + "_width", ((ClearCLImageInterface) obj).getWidth());
                        kernel.setArgument("image_size_" + key + "_height", ((ClearCLImageInterface) obj).getHeight());
                        kernel.setArgument("image_size_" + key + "_depth", ((ClearCLImageInterface) obj).getDepth());
                    }
                }
            }
            if (CLIJ.debug) {
                System.out.println("Executing " + kernelName);
            }

            final ClearCLKernel workaround = kernel;
            double duration = ElapsedTime.measure("Pure kernel execution", () -> {
                try {
                    workaround.run(waitToFinish);
                } catch (Exception e) {
                    e.printStackTrace();

                    System.out.println(workaround.getSourceCode());
                }
            });
            if (CLIJ.debug) {
                System.out.println("Returned from " + kernelName + " after " + duration + " msec" );
            }
            //clearCLKernel.close();
        }

        return kernel;
    }

    private HashMap<String, ArrayList<String>> variableListMap = new HashMap<String, ArrayList<String>>();
    private ArrayList<String> getImageVariablesFromSource() {
        String key = anchorClass.getName() + "_" + programFilename;

        if (variableListMap.containsKey(key)) {
            return variableListMap.get(key);
        }
        ArrayList<String> variableList = new ArrayList<String>();

        String sourceCode = getProgramSource();
        String[] kernels = sourceCode.split("__kernel");

        kernels[0] = "";
        for (String kernel : kernels) {
            if (kernel.length() > 0 ) {
                //System.out.println("Parsing " + kernel.split("\\(")[0]);
                String temp1 = kernel.split("\\(")[1];
                if (temp1.length() > 0) {
                    String parameterText = temp1.split("\\)")[0];
                    parameterText = parameterText.replace("\n", " ");
                    parameterText = parameterText.replace("\t", " ");
                    parameterText = parameterText.replace("\r", " ");

                    //System.out.println("Parsing parameters " + parameterText);

                    String[] parameters = parameterText.split(",");
                    for (String parameter : parameters) {
                        if (parameter.contains("IMAGE")) {
                            String[] temp2 = parameter.trim().split(" ");
                            String variableName = temp2[temp2.length - 1];

                            variableList.add(variableName);

                        }
                    }
                }
            }
        }

        variableListMap.put(key, variableList);
        return variableList;
    }

    private final HashMap<String, String> sourceCodeCache = new HashMap<String, String>();
    protected String getProgramSource() {
        String key = anchorClass.getName() + "_" + programFilename;

        if (sourceCodeCache.containsKey(key)) {
            return sourceCodeCache.get(key);
        }
        try {
            ClearCLProgram program = context.createProgram(this.anchorClass, new String[]{this.programFilename});
            String source = program.getSourceCode();
            sourceCodeCache.put(key, source);
            return source;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void setAnchorClass(Class anchorClass) {
        this.anchorClass = anchorClass;
    }

    public void setProgramFilename(String programFilename) {
        this.programFilename = programFilename;
    }

    public void setKernelName(String kernelName) {
        this.kernelName = kernelName;
    }

    public void setGlobalSizes(long[] globalSizes) {
        this.globalSizes = globalSizes;
    }

    protected ClearCLKernel getKernel(ClearCLContext context, String kernelName) throws IOException {
        return this.getKernel(context, kernelName, (Map) null);
    }

    protected ClearCLKernel getKernel(ClearCLContext context, String kernelName, Map<String, Object> defines) throws IOException {

        String programCacheKey = anchorClass.getCanonicalName() + " " + programFilename;
        for (String key : defines.keySet()) {
            programCacheKey = programCacheKey + " " + (key + " = " + defines.get(key));
        }
        if (CLIJ.debug) {
            System.out.println("Program cache hash:" + programCacheKey);
        }
        ClearCLProgram clProgram = this.programCacheMap.get(programCacheKey);
        currentProgram = clProgram;
        if (clProgram == null) {
            clProgram = context.createProgram(this.anchorClass, new String[]{this.programFilename});
            if (defines != null) {
                Iterator iterator = defines.entrySet().iterator();

                while (iterator.hasNext()) {
                    Map.Entry<String, Object> entry = (Map.Entry) iterator.next();
                    if (entry.getValue() instanceof String) {
                        clProgram.addDefine((String) entry.getKey(), (String) entry.getValue());
                    } else if (entry.getValue() instanceof Number) {
                        clProgram.addDefine((String) entry.getKey(), (Number) entry.getValue());
                    } else if (entry.getValue() == null) {
                        clProgram.addDefine((String) entry.getKey());
                    }
                }
            }

            clProgram.addBuildOptionAllMathOpt();
            clProgram.buildAndLog();
            //System.out.println("status: " + mProgram.getBuildStatus());
            //System.out.println("LOG: " + this.mProgram.getBuildLog());

            programCacheMap.put(programCacheKey, clProgram);
        }
        //System.out.println(clProgram.getSourceCode());
        //System.out.println(pKernelName);


        try {
            return clProgram.createKernel(kernelName);
        } catch (OpenCLException e) {
            System.out.println("Error when trying to create kernel " + kernelName);
            e.printStackTrace();
            return null;
        }
    }

    public void close() {
        for (String key : programCacheMap.keySet()) {
            ClearCLProgram program = programCacheMap.get(key);
            try {
                program.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (currentProgram != null) {
            try {
                currentProgram.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            currentProgram = null;
        }

        programCacheMap.clear();
    }
}
