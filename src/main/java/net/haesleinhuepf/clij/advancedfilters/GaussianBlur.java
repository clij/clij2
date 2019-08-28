package net.haesleinhuepf.clij.advancedfilters;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.clearcl.enums.ImageChannelDataType;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;
import static net.haesleinhuepf.clij.utilities.CLIJUtilities.sigmaToKernelSize;

/**
 * GaussianBlur
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 08 2019
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_gaussianBlur")
public class GaussianBlur extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    private static final int MAX_GROUP_SIZE = 128;

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number sigmaX, Number sigmaY, Number sigmaZ";
    }

    @Override
    public String getDescription() {
        return "An experimental fast Gaussian blur.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public boolean executeCL() {
        ClearCLBuffer input = (ClearCLBuffer) args[0];
        ClearCLBuffer output = (ClearCLBuffer) args[1];

        Float sigmaX = asFloat(args[2]);
        Float sigmaY = asFloat(args[3]);
        Float sigmaZ = asFloat(args[4]);

        return gaussianBlur(clij, input, output, sigmaX, sigmaY, sigmaZ);
    }

    public static boolean gaussianBlur(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Float blurSigmaX, Float blurSigmaY, Float blurSigmaZ) {
        return executeSeparableKernel(clij, src, dst, "blurLS.cl", "gaussian_blur_sep_image" + src.getDimension() + "d", sigmaToKernelSize(blurSigmaX), sigmaToKernelSize(blurSigmaY), sigmaToKernelSize(blurSigmaZ), blurSigmaX, blurSigmaY, blurSigmaZ, src.getDimension());
    }

    public static boolean gaussianBlur(CLIJ clij, ClearCLImage src, ClearCLImage dst, Float blurSigmaX, Float blurSigmaY, Float blurSigmaZ) {
        return executeSeparableKernel(clij, src, dst, "blurLS.cl", "gaussian_blur_sep_image" + src.getDimension() + "d", sigmaToKernelSize(blurSigmaX), sigmaToKernelSize(blurSigmaY), sigmaToKernelSize(blurSigmaZ), blurSigmaX, blurSigmaY, blurSigmaZ, src.getDimension());
    }

    private static boolean executeSeparableKernel(CLIJ clij, Object src, Object dst, String clFilename, String kernelname, int kernelSizeX, int kernelSizeY, int kernelSizeZ, float blurSigmaX, float blurSigmaY, float blurSigmaZ, long dimensions) {
        assertDifferent(src, dst);

        int[] n = new int[]{kernelSizeX, kernelSizeY, kernelSizeZ};
        float[] blurSigma = new float[]{blurSigmaX, blurSigmaY, blurSigmaZ};

        Object temp1;
        Object temp2;

        long[] globalSizeBuffer;
        if (src instanceof ClearCLBuffer) {
            temp1 = clij.create(((ClearCLBuffer) src).getDimensions(), NativeTypeEnum.Float);
            temp2 = clij.create(((ClearCLBuffer) src).getDimensions(), NativeTypeEnum.Float);
            globalSizeBuffer = ((ClearCLBuffer) src).getDimensions();
        } else if (src instanceof ClearCLImage) {
            temp1 = clij.create(((ClearCLImage) src).getDimensions(), ImageChannelDataType.Float);
            temp2 = clij.create(((ClearCLImage) src).getDimensions(), ImageChannelDataType.Float);
            globalSizeBuffer = ((ClearCLImage) src).getDimensions();
        } else {
            throw new IllegalArgumentException("Error: Wrong type of images in blurFast");
        }

        long[] globalSize = new long[globalSizeBuffer.length];
        for (int i = 0; i < globalSize.length; i++) {
            //System.out.println("Was: " + globalSize[i]);
            if (globalSizeBuffer[i] % MAX_GROUP_SIZE > 0) {
                globalSize[i] = ((globalSizeBuffer[i] / MAX_GROUP_SIZE) + 1) * MAX_GROUP_SIZE;
            } else {
                globalSize[i] = globalSizeBuffer[i];
            }
            //System.out.println("Is: " + globalSize[i]);
        }

        HashMap<String, Object> parameters = new HashMap<>();

        if (blurSigma[0] > 0) {
            parameters.clear();
            parameters.put("N", n[0]);
            parameters.put("s", blurSigma[0]);
            parameters.put("dim", 0);
            parameters.put("src", src);
            if (dimensions == 2) {
                parameters.put("dst", temp1);
            } else {
                parameters.put("dst", temp2);
            }
            long[] localSize = {MAX_GROUP_SIZE, 1, 1};
            //System.out.print("dst h0 " + ((ClearCLBuffer)dst).getHeight());
            clij.execute(GaussianBlur.class, clFilename, kernelname, localSize, globalSize, parameters);
            //System.out.print("dst h1 " + ((ClearCLBuffer)dst).getHeight());
        } else {
            if (dimensions == 2) {
                copyInternal(clij, src, temp1, 2, 2);
            } else {
                copyInternal(clij, src, temp2, 3, 3);
            }
        }

        if (blurSigma[1] > 0) {
            parameters.clear();
            parameters.put("N", n[1]);
            parameters.put("s", blurSigma[1]);
            parameters.put("dim", 1);
            if (dimensions == 2) {
                parameters.put("src", temp1);
                parameters.put("dst", dst);
            } else {
                parameters.put("src", temp2);
                parameters.put("dst", temp1);
            }
            long[] localSize = {1, MAX_GROUP_SIZE, 1};
            //System.out.print("dst h2 " + ((ClearCLBuffer)dst).getHeight() + " L " + Arrays.toString(localSize) +  " G " + Arrays.toString(globalSize));
            clij.execute(GaussianBlur.class, clFilename, kernelname, localSize, globalSize, parameters);
            //System.out.print("dst h3 " + ((ClearCLBuffer)dst).getHeight());
        } else {
            if (dimensions == 2) {
                copyInternal(clij, temp1, dst, 2, 2);
            } else {
                copyInternal(clij, temp2, temp1, 3, 3);
            }
        }

        if (dimensions == 3) {
            if (blurSigma[2] > 0) {
                parameters.clear();
                parameters.put("N", n[2]);
                parameters.put("s", blurSigma[2]);
                parameters.put("dim", 2);
                parameters.put("src", temp1);
                parameters.put("dst", dst);
                long[] localSize = {MAX_GROUP_SIZE};
                clij.execute(GaussianBlur.class, clFilename, kernelname, localSize, globalSize, parameters);
            } else {
                copyInternal(clij, temp1, dst,3, 3);
            }
        }

        if (temp1 instanceof ClearCLBuffer) {
            ((ClearCLBuffer) temp1).close();
            ((ClearCLBuffer) temp2).close();
        } else if (temp1 instanceof ClearCLImage) {
            ((ClearCLImage) temp1).close();
            ((ClearCLImage) temp2).close();
        }

        return true;
    }


    private static boolean copyInternal(CLIJ clij, Object src, Object dst, long srcNumberOfDimensions, long dstNumberOfDimensions) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        return clij.execute(Kernels.class, "duplication.cl", "copy_" + srcNumberOfDimensions + "d", parameters);
    }




}
