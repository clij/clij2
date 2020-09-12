package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import net.imglib2.Cursor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;
import org.scijava.plugin.Plugin;

import java.nio.FloatBuffer;

/**
 * ConnectedComponentsLabeling
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 06 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_closeIndexGapsInLabelMap")
public class CloseIndexGapsInLabelMap extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized {

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();
        ClearCLBuffer input = (ClearCLBuffer) args[0];
        ClearCLBuffer output = (ClearCLBuffer) args[1];

        boolean result = getCLIJ2().closeIndexGapsInLabelMap(input, output);
        releaseBuffers(args);
        return result;
    }

    @Deprecated
    public static boolean shiftIntensitiesToCloseGaps(CLIJ2 clij2, ClearCLBuffer input, ClearCLBuffer output) {
        return closeIndexGapsInLabelMap(clij2, input, output);
    }

    public static boolean closeIndexGapsInLabelMap(CLIJ2 clij2, ClearCLBuffer input, ClearCLImageInterface output) {

        int maximum = (int) clij2.maximumOfAllPixels(input);
        final float[] allNewIndices = new float[maximum + 1];

        float[] count = {1};
/*
        long number_of_pixels = (int) (input.getWidth() * input.getHeight());
        if (number_of_pixels < Integer.MAX_VALUE && input.getNativeType() == NativeTypeEnum.Float) {
            clij2.stopWatch("");

            ClearCLBuffer sliceBuffer = (input.getDimension() == 3)?clij2.create(new long[]{input.getWidth(), input.getHeight()}, input.getNativeType()):null;

            float[] slice1 = new float[(int) number_of_pixels];
            FloatBuffer buffer1 = FloatBuffer.wrap(slice1);
            float[] slice2 = new float[(int) number_of_pixels];
            FloatBuffer buffer2 = FloatBuffer.wrap(slice2);


            clij2.stopWatch("alloc/wrap");

            for (int z = -1; z < input.getDepth(); z++) {
                final float[] slice = (z%2==0)?slice1:slice2;
                final FloatBuffer buffer = (z%2==1)?buffer1:buffer2;
                final int currentZ = z;
                Thread copyThread = null;
                if (z < input.getDepth() - 1) {
                    copyThread = new Thread() {
                        @Override
                        public void run() {
                            //System.out.println("input size " + input.getSizeInBytes());
                            //System.out.println("input pixel size " + input.getPixelSizeInBytes());
                            //System.out.println("number_of_pixels " + number_of_pixels);
                            //System.out.println("currentZ " + currentZ);
                            //if (input.getDimension() == 3) {
                            //    input.writeTo(buffer, new long[]{0, 0, currentZ+1}, new long[]{0,0,0}, new long[]{input.getWidth(), input.getHeight(), 1}, true);
                            //} else {
                            //    input.writeTo(buffer, new long[]{0, 0}, new long[]{0,0}, new long[]{input.getWidth(), input.getHeight()}, true);
                            //}
                            if (input.getDimension() == 3) {
                                clij2.copySlice(input, sliceBuffer, currentZ);
                                sliceBuffer.writeTo(buffer, true);
                            } else {
                                input.writeTo(buffer, true);
                            }
                            //input.writeTo(buffer, (currentZ + 1) * number_of_pixels, number_of_pixels, true);

                        }
                    };
                    copyThread.start();
                }

                // clij2.stopWatch("copy");

                Thread readerThread = null;
                if (z >= 0) {
                    readerThread  = new Thread() {
                        @Override
                        public void run() {
                            for (int i = 0; i < slice.length; i++) {
                                int key = (int) slice[i];
                                if (key > 0 && allNewIndices[key] == 0) {
                                    allNewIndices[key] = count[0];
                                    count[0] = count[0] + 1;
                                }
                            }
                            //System.out.println("                         count " + count[0]);
                        }
                    };
                    readerThread.start();
                }

                if (copyThread != null) {
                    try {
                        copyThread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (readerThread != null) {
                    try {
                        readerThread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (sliceBuffer != null) {
                clij2.release(sliceBuffer);
            }
            clij2.stopWatch("parse " + count[0]);
*/

        long number_of_pixels = (int) (input.getWidth() * input.getHeight() * input.getDepth());
        if (number_of_pixels < Integer.MAX_VALUE && input.getNativeType() == NativeTypeEnum.Float) {
            //clij2.stopWatch("");
            float[] slice = new float[(int) number_of_pixels];
            FloatBuffer buffer = FloatBuffer.wrap(slice);

            input.writeTo(buffer, true);
            for (int i = 0; i < slice.length; i++) {
                int key = (int) slice[i];
                if (key > 0 && allNewIndices[key] == 0) {
                    allNewIndices[key] = count[0];
                    count[0] = count[0] + 1;
                }
            }
            //clij2.stopWatch("parse" + count[0]);
        } else { // that's slower but more generic:
            RandomAccessibleInterval rai = clij2.convert(input, RandomAccessibleInterval.class);
            Cursor<RealType> cursor = Views.iterable(rai).cursor();

            while (cursor.hasNext()) {
                int key = new Float(cursor.next().getRealFloat()).intValue();
                if (key > 0 && allNewIndices[key] == 0) {
                    allNewIndices[key] = count[0];
                    count[0] = count[0] + 1;
                }
            }
        }

        //clij2.stopWatch("");

        ClearCLBuffer keyValueMap = clij2.create(new long[]{allNewIndices.length, 1, 1}, NativeTypeEnum.Float);
        //clij2.stopWatch("alloc2");
        keyValueMap.readFrom(FloatBuffer.wrap(allNewIndices), true);
        //clij2.stopWatch("copy2");

        ReplaceIntensities.replaceIntensities(clij2, input, keyValueMap, output);
        //clij2.stopWatch("repl");

        keyValueMap.close();
        return true;
    }

/*
    public static boolean nonzeroMinimumBox(CLIJ clij, ClearCLBuffer src, ClearCLBuffer flag, ClearCLBuffer dst, int radiusX, int radiusY, int radiusZ) {
        return executeSeparableKernel(clij, src, flag, dst, "cca.cl", "min_sep_image" + src.getDimension() + "d", radiusToKernelSize(radiusX), radiusToKernelSize(radiusY), radiusToKernelSize(radiusZ), radiusX, radiusY, radiusZ, src.getDimension());
    }

    private static boolean executeSeparableKernel(CLIJ clij, ClearCLBuffer src, ClearCLBuffer flag, ClearCLBuffer dst, String clFilename, String kernelname, int kernelSizeX, int kernelSizeY, int kernelSizeZ, float blurSigmaX, float blurSigmaY, float blurSigmaZ, long dimensions) {
        CLIJUtilities.assertDifferent(src, dst);

        int[] n = new int[]{kernelSizeX, kernelSizeY, kernelSizeZ};
        float[] blurSigma = new float[]{blurSigmaX, blurSigmaY, blurSigmaZ};

        ClearCLBuffer temp = clij.createCLBuffer((ClearCLBuffer) src);

        HashMap<String, Object> parameters = new HashMap<>();

        if (blurSigma[0] > 0) {
            parameters.clear();
            parameters.put("N", n[0]);
            parameters.put("s", blurSigma[0]);
            parameters.put("dim", 0);
            parameters.put("src", src);
            if (dimensions == 2) {
                parameters.put("dst", temp);
            } else {
                parameters.put("dst", dst);
            }
            parameters.put("flag_dst", flag);
            clij.execute(ConnectedComponentsLabeling.class, clFilename, kernelname, dst.getDimensions(), parameters);
        } else {
            if (dimensions == 2) {
                copyInternal(clij, src, temp, 2, 2);
            } else {
                copyInternal(clij, src, dst, 3, 3);
            }
        }

        if (blurSigma[1] > 0) {
            parameters.clear();
            parameters.put("N", n[1]);
            parameters.put("s", blurSigma[1]);
            parameters.put("dim", 1);
            if (dimensions == 2) {
                parameters.put("src", temp);
                parameters.put("dst", dst);
            } else {
                parameters.put("src", dst);
                parameters.put("dst", temp);
            }
            parameters.put("flag_dst", flag);
            clij.execute(ConnectedComponentsLabeling.class, clFilename, kernelname, dst.getDimensions(),parameters);
        } else {
            if (dimensions == 2) {
                copyInternal(clij, temp, dst, 2, 2);
            } else {
                copyInternal(clij, dst, temp, 3, 3);
            }
        }

        if (dimensions == 3) {
            if (blurSigma[2] > 0) {
                parameters.clear();
                parameters.put("N", n[2]);
                parameters.put("s", blurSigma[2]);
                parameters.put("dim", 2);
                parameters.put("src", temp);
                parameters.put("dst", dst);
                parameters.put("flag_dst", flag);
                clij.execute(ConnectedComponentsLabeling.class, clFilename, kernelname, dst.getDimensions(), parameters);
            } else {
                copyInternal(clij, temp, dst,3, 3);
            }
        }

        temp.close();

        return true;
    }*/

/*    static boolean copyInternal(CLIJ clij, Object src, Object dst, long srcNumberOfDimensions, long dstNumberOfDimensions) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        if (!checkDimensions(srcNumberOfDimensions, dstNumberOfDimensions)) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }
        clij.execute(Kernels.class, "duplication.cl", "copy_" + srcNumberOfDimensions + "d", parameters);
        return true;
    }
*/
    @Override
    public String getParameterHelpText() {
        return "Image labeling_input, ByRef Image labeling_destination";
    }

    @Override
    public String getDescription() {
        return "Analyses a label map and if there are gaps in the indexing (e.g. label 5 is not present) all \n" +
                "subsequent labels will be relabelled. \n\nThus, afterwards number of labels and maximum label index are equal.\n" +
                "This operation is mostly performed on the CPU.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        return clij.create(input.getDimensions(), NativeTypeEnum.Float);
    }

    @Override
    public String getCategories() {
        return "Label";
    }
}
