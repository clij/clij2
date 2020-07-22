package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij.utilities.CLKernelExecutor;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;
import static net.haesleinhuepf.clij.utilities.CLIJUtilities.radiusToKernelSize;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_median2DBox")
public class Median2DBox extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized {
    @Override
    public String getCategories() {
        return "Filter";
    }

    @Override
    public Object[] getDefaultValues() {
        return new Object[]{null, null, 2, 2};
    }

    @Override
    public boolean executeCL() {
        int radiusX = asInteger(args[2]);
        int radiusY = asInteger(args[3]);

        return getCLIJ2().median2DBox((ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), radiusX, radiusY);
    }

    public static boolean median2DBox(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst, Integer radiusX, Integer radiusY) {
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

        clij2.execute(Median2DBox.class, "median_box_2d_x.cl", "median_box_2d", dst.getDimensions(), dst.getDimensions(), parameters);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Image destination, Number radiusX, Number radiusY";
    }

    @Override
    public String getDescription() {
        return "Computes the local median of a pixels rectangular neighborhood. \n\nThe rectangle is specified by \n" +
                "its half-width and half-height (radius).\n\n" +
                "For technical reasons, the area of the rectangle must have less than 1000 pixels.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }
}
