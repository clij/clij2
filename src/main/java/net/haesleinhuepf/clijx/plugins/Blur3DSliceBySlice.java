package net.haesleinhuepf.clijx.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.plugins.GaussianBlur3D;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import net.haesleinhuepf.clij2.utilities.CLIJUtilities;
import org.scijava.plugin.Plugin;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.sigmaToKernelSize;
import static net.haesleinhuepf.clij2.utilities.CLIJUtilities.executeSeparableKernel;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_blur3DSliceBySlice")
public class Blur3DSliceBySlice extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        float sigmaX = asFloat(args[2]);
        float sigmaY = asFloat(args[3]);

        ClearCLBuffer input = (ClearCLBuffer) args[0];
        ClearCLBuffer output = (ClearCLBuffer) args[1];

        CLIJx clijx = getCLIJx();

        if (clijx.hasImageSupport()) {
            ClearCLImage image = clijx.create(input.getDimensions(), CLIJUtilities.nativeToChannelType(input.getNativeType()));
            clijx.copy(input, image);
            blurSliceBySlice(clijx, image, output, sigmaX, sigmaY);
            clijx.release(image);
        } else {
            blurSliceBySlice(clijx, input, output, sigmaX, sigmaY);
        }
        return true;
    }

    /**
     * use blur() with sigmaZ = 0 instead
     */
    @Deprecated
    public static boolean blurSliceBySlice(CLIJx clijx, ClearCLImageInterface src, ClearCLImageInterface dst, Float blurSigmaX, Float blurSigmaY) {
        return GaussianBlur3D.blur(clijx, src, dst, blurSigmaX, blurSigmaY, 0f);
    }

    /**
     * use blur() with sigmaZ = 0 instead
     */
    @Deprecated
    public static boolean blurSliceBySlice(CLIJx clijx, ClearCLImageInterface src, ClearCLImageInterface dst, Integer kernelSizeX, Integer kernelSizeY, Float blurSigmaX, Float blurSigmaY) {
        return executeSeparableKernel(clijx, src, dst, GaussianBlur3D.class, "blur_separable_" + src.getDimension() + "d_x.cl", "blur_separable_" + src.getDimension() + "d", sigmaToKernelSize(blurSigmaX), sigmaToKernelSize(blurSigmaY), sigmaToKernelSize(0), blurSigmaX, blurSigmaY, 0, src.getDimension());
    }

        @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number sigmaX, Number sigmaY";
    }


    @Override
    public String getDescription() {
        return "Computes the Gaussian blurred image of an image given two sigma values in X and Y. Thus, the filter" +
                "kernel can have non-isotropic shape.\n\n" +
                "" +
                "The Gaussian blur is applied slice by slice in 2D.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }
}
