package net.haesleinhuepf.clijx.plugins;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.WaitForUserDialog;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLKernel;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.plugins.ReplaceIntensities;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import net.imglib2.Cursor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;
import org.scijava.plugin.Plugin;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;
import static net.haesleinhuepf.clij2.utilities.CLIJUtilities.checkDimensions;

/**
 * ConnectedComponentsLabeling
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 06 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_connectedComponentsLabeling")
public class ConnectedComponentsLabeling extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();
        ClearCLBuffer input = (ClearCLBuffer) args[0];
        ClearCLBuffer output = (ClearCLBuffer) args[1];

        boolean result = connectedComponentsLabeling(getCLIJx(), input, output);
        releaseBuffers(args);
        return result;
    }

    public static boolean connectedComponentsLabeling(CLIJx clijx, ClearCLBuffer input, ClearCLBuffer output) {
        //ClearCLImage temp1 = clij.create(output.getDimensions(), CLIJUtilities.nativeToChannelType(output.getNativeType()));
        //ClearCLImage temp2 = clij.create(output.getDimensions(), CLIJUtilities.nativeToChannelType(output.getNativeType()));

        ClearCLBuffer temp1 = clijx.create(output.getDimensions());
        ClearCLBuffer temp2 = clijx.create(output.getDimensions());
        ClearCLBuffer temp3 = clijx.create(output.getDimensions());

        ClearCLBuffer flag = clijx.create(new long[]{1,1,1}, NativeTypeEnum.Byte);
        ByteBuffer aByteBufferWithAZero = ByteBuffer.allocate(1);
        aByteBufferWithAZero.put((byte)0);
        flag.readFrom(aByteBufferWithAZero, true);

        setNonZeroPixelsToPixelIndex(clijx.getClij(), input, temp1);

        clijx.set(temp2, 0f);


        final int[] iterationCount = {0};
        int flagValue = 1;

        ClearCLKernel flipkernel = null;
        ClearCLKernel flopkernel = null;

        while (flagValue > 0) {
            if (iterationCount[0] % 2 == 0) {
                NonzeroMinimumBox.nonzeroMinimumBox(clijx, temp1, flag, temp2, null).close();
                /*if (flipkernel == null) {
                    flipkernel = NonzeroMinimumDiamond.nonzeroMinimumDiamond(clijx, temp1, flag, temp2, flipkernel);
                } else {
                    flipkernel.run(true);
                }*/
            } else {
                NonzeroMinimumBox.nonzeroMinimumBox(clijx, temp2, flag, temp1, null).close();
                /*
                if (flopkernel == null) {
                    flopkernel = NonzeroMinimumDiamond.nonzeroMinimumDiamond(clijx, temp2, flag, temp1, flopkernel);
                } else {
                    flopkernel.run(true);
                }*/

            }

            ImagePlus flagImp = clijx.pull(flag);
            flagValue = flagImp.getProcessor().get(0,0);
            flag.readFrom(aByteBufferWithAZero, true);
            iterationCount[0] = iterationCount[0] + 1;
        }

        if (iterationCount[0] % 2 == 0) {
            copyInternal(clijx.getClij(), temp1, temp3, temp1.getDimension(), temp3.getDimension());
        } else {
            copyInternal(clijx.getClij(), temp2, temp3, temp2.getDimension(), temp3.getDimension());
        }
        if (flipkernel != null) {
            flipkernel.close();
        }
        if (flopkernel != null) {
            flopkernel.close();
        }


        shiftIntensitiesToCloseGaps(clijx, temp3, output);

        clijx.release(temp1);
        clijx.release(temp2);
        clijx.release(temp3);
        clijx.release(flag);

        return true;
    }

    public static boolean shiftIntensitiesToCloseGaps(CLIJx clijx, ClearCLBuffer input, ClearCLBuffer output) {
        //clijx.stopWatch("");
        int maximum = (int) clijx.maximumOfAllPixels((ClearCLBuffer) input);
        final float[] allNewIndices = new float[maximum + 1];
        //clijx.stopWatch("max " + maximum);

        float[] count = {1};
/*
        long number_of_pixels = (int) (input.getWidth() * input.getHeight());
        if (number_of_pixels < Integer.MAX_VALUE && input.getNativeType() == NativeTypeEnum.Float) {
            clijx.stopWatch("");

            ClearCLBuffer sliceBuffer = (input.getDimension() == 3)?clijx.create(new long[]{input.getWidth(), input.getHeight()}, input.getNativeType()):null;

            float[] slice1 = new float[(int) number_of_pixels];
            FloatBuffer buffer1 = FloatBuffer.wrap(slice1);
            float[] slice2 = new float[(int) number_of_pixels];
            FloatBuffer buffer2 = FloatBuffer.wrap(slice2);


            clijx.stopWatch("alloc/wrap");

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
                                clijx.copySlice(input, sliceBuffer, currentZ);
                                sliceBuffer.writeTo(buffer, true);
                            } else {
                                input.writeTo(buffer, true);
                            }
                            //input.writeTo(buffer, (currentZ + 1) * number_of_pixels, number_of_pixels, true);

                        }
                    };
                    copyThread.start();
                }

                // clijx.stopWatch("copy");

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
                clijx.release(sliceBuffer);
            }
            clijx.stopWatch("parse " + count[0]);
*/

        long number_of_pixels = (int) (input.getWidth() * input.getHeight() * input.getDepth());
        if (number_of_pixels < Integer.MAX_VALUE && input.getNativeType() == NativeTypeEnum.Float) {
            //clijx.stopWatch("");
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
            //clijx.stopWatch("parse" + count[0]);
        } else { // that's slower but more generic:
            RandomAccessibleInterval rai = clijx.convert(input, RandomAccessibleInterval.class);
            Cursor<RealType> cursor = Views.iterable(rai).cursor();

            while (cursor.hasNext()) {
                int key = new Float(cursor.next().getRealFloat()).intValue();
                if (key > 0 && allNewIndices[key] == 0) {
                    allNewIndices[key] = count[0];
                    count[0] = count[0] + 1;
                }
            }
        }

        //clijx.stopWatch("");

        ClearCLBuffer keyValueMap = clijx.create(new long[]{allNewIndices.length, 1, 1}, NativeTypeEnum.Float);
        //clijx.stopWatch("alloc2");
        keyValueMap.readFrom(FloatBuffer.wrap(allNewIndices), true);
        //clijx.stopWatch("copy2");

        ReplaceIntensities.replaceIntensities(clijx, input, keyValueMap, output);
        //clijx.stopWatch("repl");

        keyValueMap.close();
        return true;
    }

    public static void main(String[] args) {
        new ImageJ();
        ImagePlus imp = IJ.openImage("src/test/resources/miniBlobs.tif");
        imp.show();

        CLIJx clijx = CLIJx.getInstance();
        CLIJ clij = clijx.getClij();

        int size = 20;

        ClearCLBuffer miniBlobs = clij.push(imp);
        ClearCLBuffer input = clij.create(new long[]{miniBlobs.getWidth() * size, miniBlobs.getHeight() * size, miniBlobs.getDepth() * size}, miniBlobs.getNativeType());
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                for (int z = 0; z < size; z++) {
                    Paste3D.paste(clij, miniBlobs, input, (int)(x * miniBlobs.getWidth()), (int)(y * miniBlobs.getHeight()), (int)(z * miniBlobs.getDepth()));
                }
            }
        }
        clij.show(input, "input");

        ClearCLBuffer thresholded = clij.create(input);
        ClearCLBuffer output = clij.createCLBuffer(input.getDimensions(), NativeTypeEnum.Float);

        clij.op().threshold(input, thresholded, 7f);
        clij.show(thresholded, "thresholded");

        for (int i = 0; i < 3; i++) {
            connectedComponentsLabeling(clijx, thresholded, output);


            //assertEquals(375.0, clij.op().maximumOfAllPixels(output), 0.1);
        }
        clij.show(output, "result");

        new WaitForUserDialog("wait").show();


        input.close();
        output.close();
        thresholded.close();

    }

    public static boolean setNonZeroPixelsToPixelIndex(CLIJ clij, ClearCLImageInterface src, ClearCLImageInterface dst) {
        HashMap<String, Object> parameters = new HashMap<>();

        parameters.clear();
        parameters.put("src", src);
        parameters.put("dst", dst);
        return clij.execute(ConnectedComponentsLabeling.class, "cca.cl", "set_nonzero_pixels_to_pixelindex", parameters);
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

    static boolean copyInternal(CLIJ clij, Object src, Object dst, long srcNumberOfDimensions, long dstNumberOfDimensions) {
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

    @Override
    public String getParameterHelpText() {
        return "Image binary_input, Image labeling_destination";
    }

    @Override
    public String getDescription() {
        return "Performs connected components analysis to a binary image and generates a label map.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        return clij.create(input.getDimensions(), NativeTypeEnum.Float);
    }
}
