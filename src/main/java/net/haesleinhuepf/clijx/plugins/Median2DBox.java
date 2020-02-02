package net.haesleinhuepf.clijx.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij.utilities.CLKernelExecutor;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;
import static net.haesleinhuepf.clij.utilities.CLIJUtilities.radiusToKernelSize;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_median2DBox")
public class Median2DBox extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        int radiusX = asInteger(args[2]);
        int radiusY = asInteger(args[3]);

        return median2DBox(getCLIJx(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), radiusX, radiusY);
    }

    public static boolean median2DBox(CLIJx clijx, ClearCLImageInterface src, ClearCLImageInterface dst, Integer radiusX, Integer radiusY) {
        assertDifferent(src, dst);

        int kernelSizeX = radiusToKernelSize(radiusX);
        int kernelSizeY = radiusToKernelSize(radiusY);

        if (kernelSizeX * kernelSizeY > CLKernelExecutor.MAX_ARRAY_SIZE) {
            throw new IllegalArgumentException("Error: kernels of the medianBox filter is too big. Consider increasing MAX_ARRAY_SIZE.");
        }
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        clijx.execute(Median2DBox.class, "median_box_2d_x.cl", "median_box_2d", dst.getDimensions(), dst.getDimensions(), parameters);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number radiusX, Number radiusY";
    }

    @Override
    public String getDescription() {
        return "Computes the local median of a pixels rectangular neighborhood. The rectangle is specified by \n" +
                "its half-width and half-height (radius).\n\n" +
                "For technical reasons, the area of the rectangle must have less than 1000 pixels.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }
}
