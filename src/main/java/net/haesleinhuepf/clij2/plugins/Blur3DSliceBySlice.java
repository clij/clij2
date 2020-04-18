package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.sigmaToKernelSize;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */
@Deprecated
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_blur3DSliceBySlice")
public class Blur3DSliceBySlice extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        float sigmaX = asFloat(args[2]);
        float sigmaY = asFloat(args[3]);
        int nX = sigmaToKernelSize(sigmaX);
        int nY = sigmaToKernelSize(sigmaY);

        ClearCLBuffer input = (ClearCLBuffer) args[0];
        ClearCLBuffer output = (ClearCLBuffer) args[1];

        return blur3DSliceBySlice(getCLIJ2(), input, output, sigmaX, sigmaY);
    }

    @Deprecated
    public static boolean blur3DSliceBySlice(CLIJ2 clij2, ClearCLImageInterface input, ClearCLImageInterface output, Float sigmaX, Float sigmaY) {
        return GaussianBlur3D.gaussianBlur3D(clij2, input, output, sigmaX, sigmaY, 0.0f);
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Image destination, Number sigmaX, Number sigmaY";
    }


    @Override
    public String getDescription() {
        return "Computes the Gaussian blurred image of an image given two sigma values in X and Y. Thus, the filter" +
                "kernel can have non-isotropic shape.\n\n" +
                "" +
                "The Gaussian blur is applied slice by slice in 2D."
                + "\n\nDEPRECATED: This method is deprecated. Use gaussianBlur3D instead.";
    }


    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }
}
