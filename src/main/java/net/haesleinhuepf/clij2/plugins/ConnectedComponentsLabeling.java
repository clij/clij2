package net.haesleinhuepf.clij2.plugins;

import ij.ImagePlus;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLKernel;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

import java.nio.ByteBuffer;

//import net.haesleinhuepf.clijx.CLIJx;

/**
 * ConnectedComponentsLabeling
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 06 2019
 */
@Deprecated // use ConnectedComponentsLabellingBox or -Diamond
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_connectedComponentsLabeling")
public class ConnectedComponentsLabeling extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();
        ClearCLBuffer input = (ClearCLBuffer) args[0];
        ClearCLBuffer output = (ClearCLBuffer) args[1];

        boolean result = connectedComponentsLabeling(getCLIJ2(), input, output);
        releaseBuffers(args);
        return result;
    }

    @Deprecated // use connectedComponentsLabelingBox or -Diamond
    public static boolean connectedComponentsLabeling(CLIJ2 clij2, ClearCLImageInterface input, ClearCLImageInterface output) {
        return connectedComponentsLabeling_internal(clij2, input, output, true, true);
    }

    protected static boolean connectedComponentsLabeling_internal(CLIJ2 clij2, ClearCLImageInterface input, ClearCLImageInterface output, boolean forceContinousLabeling, boolean useBoxNeighborhood) {
        //ClearCLImage temp1 = clij2.create(output.getDimensions(), CLIJUtilities.nativeToChannelType(output.getNativeType()));
        //ClearCLImage temp2 = clij2.create(output.getDimensions(), CLIJUtilities.nativeToChannelType(output.getNativeType()));

        ClearCLBuffer temp1 = clij2.create(output.getDimensions());
        ClearCLBuffer temp2 = clij2.create(output.getDimensions());
        ClearCLBuffer temp3 = clij2.create(output.getDimensions());

        ClearCLBuffer flag = clij2.create(new long[]{1,1,1}, NativeTypeEnum.Byte);
        ByteBuffer aByteBufferWithAZero = ByteBuffer.allocate(1);
        aByteBufferWithAZero.put((byte)0);
        flag.readFrom(aByteBufferWithAZero, true);

        SetNonZeroPixelsToPixelIndex.setNonZeroPixelsToPixelIndex(clij2, input, temp1);

        clij2.set(temp2, 0f);


        final int[] iterationCount = {0};
        int flagValue = 1;

        ClearCLKernel flipkernel = null;
        ClearCLKernel flopkernel = null;

        //CLIJx.getInstance().stopWatch("");
        while (flagValue > 0) {
            if (iterationCount[0] % 2 == 0) {
                //NonzeroMinimumBox.nonzeroMinimumBox(clij2, temp1, flag, temp2, null).close();
                if (flipkernel == null) {
                    if (useBoxNeighborhood) {
                        flipkernel = NonzeroMinimumBox.nonzeroMinimumBox(clij2, temp1, flag, temp2, flipkernel);
                    } else {
                        flipkernel = NonzeroMinimumDiamond.nonzeroMinimumDiamond(clij2, temp1, flag, temp2, flipkernel);
                    }
                } else {
                    flipkernel.run(true);
                }
            } else {
                //NonzeroMinimumBox.nonzeroMinimumBox(clij2, temp2, flag, temp1, null).close();

                if (flopkernel == null) {
                    if (useBoxNeighborhood) {
                        flopkernel = NonzeroMinimumBox.nonzeroMinimumBox(clij2, temp2, flag, temp1, flopkernel);
                    } else {
                        flopkernel = NonzeroMinimumDiamond.nonzeroMinimumDiamond(clij2, temp2, flag, temp1, flopkernel);
                    }
                } else {
                    flopkernel.run(true);
                }
            }

            ImagePlus flagImp = clij2.pull(flag);
            flagValue = flagImp.getProcessor().get(0,0);
            flag.readFrom(aByteBufferWithAZero, true);
            iterationCount[0] = iterationCount[0] + 1;
        }
        //CLIJx.getInstance().stopWatch("cca loop " + iterationCount[0]);

        if (iterationCount[0] % 2 == 0) {
            clij2.copy(temp1, temp3);
        } else {
            clij2.copy(temp1, temp3);
        }
        if (flipkernel != null) {
            flipkernel.close();
        }
        if (flopkernel != null) {
            flopkernel.close();
        }

        if (forceContinousLabeling) {
            CloseIndexGapsInLabelMap.closeIndexGapsInLabelMap(clij2, temp3, output);
        } else {
            clij2.copy(temp3, output);
        }
        //shiftIntensitiesToCloseGaps(clij2, temp3, output);

        clij2.release(temp1);
        clij2.release(temp2);
        clij2.release(temp3);
        clij2.release(flag);

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
        return "Image binary_input, ByRef Image labeling_destination";
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
