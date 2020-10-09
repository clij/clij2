package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.CLIJUtilities;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import net.haesleinhuepf.clij2.utilities.ProcessableInTiles;
import org.scijava.plugin.Plugin;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.sigmaToKernelSize;
import static net.haesleinhuepf.clij2.utilities.CLIJUtilities.executeSeparableKernel;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_gaussianBlur3D")
public class GaussianBlur3D extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, ProcessableInTiles, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Image";
    }

    @Override
    public String getOutputType() {
        return "Image";
    }


    @Override
    public Object[] getDefaultValues() {
        return new Object[]{null, null, 2, 2, 2};
    }

    @Override
    public boolean executeCL() {
        float sigmaX = asFloat(args[2]);
        float sigmaY = asFloat(args[3]);
        float sigmaZ = asFloat(args[4]);

        ClearCLBuffer input = (ClearCLBuffer) args[0];
        ClearCLBuffer output = (ClearCLBuffer) args[1];

        CLIJ2 clij2 = getCLIJ2();

        if (clij2.hasImageSupport()) {
            ClearCLImage image = clij2.create(input.getDimensions(), CLIJUtilities.nativeToChannelType(input.getNativeType()));
            clij2.copy(input, image);
            clij2.gaussianBlur3D(image, output, sigmaX, sigmaY, sigmaZ);
            clij2.release(image);
        } else {
            clij2.gaussianBlur3D(input, output, sigmaX, sigmaY, sigmaZ);
        }
        return true;
    }

    @Deprecated
    public static boolean blur3D(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst, Float blurSigmaX, Float blurSigmaY, Float blurSigmaZ) {
        return gaussianBlur3D(clij2, src, dst, blurSigmaX, blurSigmaY, blurSigmaZ);
    }

    @Deprecated
    public static boolean blur(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst, Float blurSigmaX, Float blurSigmaY, Float blurSigmaZ) {
        return gaussianBlur(clij2, src, dst, blurSigmaX, blurSigmaY, blurSigmaZ);
    }

    public static boolean gaussianBlur3D(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst, Float blurSigmaX, Float blurSigmaY, Float blurSigmaZ) {
        return gaussianBlur(clij2, src, dst, blurSigmaX, blurSigmaY, blurSigmaZ);
    }

    public static boolean gaussianBlur(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst, Float blurSigmaX, Float blurSigmaY, Float blurSigmaZ) {
        return executeSeparableKernel(clij2, src, dst, GaussianBlur3D.class, "gaussian_blur_separable_" + src.getDimension() + "d_x.cl", "gaussian_blur_separable_" + src.getDimension() + "d", sigmaToKernelSize(blurSigmaX), sigmaToKernelSize(blurSigmaY), sigmaToKernelSize(blurSigmaZ), blurSigmaX, blurSigmaY, blurSigmaZ, src.getDimension());
    }


    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Image destination, Number sigmaX, Number sigmaY, Number sigmaZ";
    }


    @Override
    public String getDescription() {
        return "Computes the Gaussian blurred image of an image given two sigma values in X, Y and Z. \n\nThus, the filter" +
                "kernel can have non-isotropic shape.\n\n" +
                "" +
                "The implementation is done separable. In case a sigma equals zero, the direction is not blurred.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }

    @Override
    public String getCategories() {
        return "Filter, Noise";
    }
}
