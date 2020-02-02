package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;
import static net.haesleinhuepf.clij.utilities.CLIJUtilities.radiusToKernelSize;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_countNonZeroPixels2DSphere")
public class CountNonZeroPixels2DSphere extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        return countNonZeroPixelsLocally(getCLIJ2(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asInteger(args[2]), asInteger(args[3]));
    }

    public static boolean countNonZeroPixelsLocally(CLIJ2 clij2, ClearCLBuffer src, ClearCLBuffer dst, Integer radiusX, Integer radiusY) {
        return countNonZeroPixels2DSphere(clij2, src, dst, radiusX, radiusY);
    }

    public static boolean countNonZeroPixels2DSphere(CLIJ2 clij2, ClearCLBuffer src, ClearCLBuffer dst, Integer radiusX, Integer radiusY) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("Nx", radiusToKernelSize(radiusX));
        parameters.put("Ny", radiusToKernelSize(radiusY));
        parameters.put("src", src);
        parameters.put("dst", dst);
        clij2.execute(CountNonZeroPixels2DSphere.class, "count_nonzero_pixels_sphere_2d_x.cl", "count_nonzero_pixels_sphere_2d", dst.getDimensions(), dst.getDimensions(), parameters);
        return true;
    }


    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number radiusX, Number radiusY";
    }

    @Override
    public String getDescription() {
        return "Counts non-zero pixels in a sphere around every pixel." +
                "Put the number in the result image.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }
}
