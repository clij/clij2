package net.haesleinhuepf.clijx.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.plugins.GaussianBlur3D;
import net.haesleinhuepf.clij2.utilities.CLIJUtilities;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.sigmaToKernelSize;
import static net.haesleinhuepf.clij2.utilities.CLIJUtilities.executeSeparableKernel;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */
@Deprecated
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_blur3DSliceBySlice")
public class Blur3DSliceBySlice extends BlurSliceBySlice {
    /**
     * use blur() with sigmaZ = 0 instead
     */
    @Deprecated
    public static boolean blur3DSliceBySlice(CLIJx clijx, ClearCLImageInterface src, ClearCLImageInterface dst, Float blurSigmaX, Float blurSigmaY) {
        return GaussianBlur3D.blur(clijx, src, dst, blurSigmaX, blurSigmaY, 0f);
    }

    /**
     * use blur() with sigmaZ = 0 instead
     */
    @Deprecated
    public static boolean blur3DSliceBySlice(CLIJx clijx, ClearCLImageInterface src, ClearCLImageInterface dst, Integer kernelSizeX, Integer kernelSizeY, Float blurSigmaX, Float blurSigmaY) {
        return executeSeparableKernel(clijx, src, dst, GaussianBlur3D.class, "blur_separable_" + src.getDimension() + "d_x.cl", "blur_separable_" + src.getDimension() + "d", sigmaToKernelSize(blurSigmaX), sigmaToKernelSize(blurSigmaY), sigmaToKernelSize(0), blurSigmaX, blurSigmaY, 0, src.getDimension());
    }
}
