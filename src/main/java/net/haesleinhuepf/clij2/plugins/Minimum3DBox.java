package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import net.haesleinhuepf.clij2.utilities.ProcessableInTiles;
import org.scijava.plugin.Plugin;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.radiusToKernelSize;
import static net.haesleinhuepf.clij2.utilities.CLIJUtilities.executeSeparableKernel;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_minimum3DBox")
public class Minimum3DBox extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, ProcessableInTiles, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Image";
    }

    @Override
    public String getOutputType() {
        return "Image";
    }

    @Override
    public String getCategories() {
        return "Filter";
    }

    @Override
    public Object[] getDefaultValues() {
        return new Object[]{null, null, 10, 10, 10};
    }

    @Override
    public boolean executeCL() {
        int radiusX = asInteger(args[2]);
        int radiusY = asInteger(args[3]);
        int radiusZ = asInteger(args[4]);

        return getCLIJ2().minimum3DBox((ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]), radiusX, radiusY, radiusZ);
    }

    public static boolean minimum3DBox(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst, Integer radiusX, Integer radiusY, Integer radiusZ) {
        return minimumBox(clij2, src, dst, radiusX, radiusY, radiusZ);
    }

    public static boolean minimumBox(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst, Integer radiusX, Integer radiusY, Integer radiusZ) {
        return executeSeparableKernel(clij2, src, dst, Minimum3DBox.class, "minimum_separable_" + src.getDimension() + "d_x.cl", "minimum_separable_" + src.getDimension() + "d", radiusToKernelSize(radiusX), radiusToKernelSize(radiusY), radiusToKernelSize(radiusZ), radiusX, radiusY, radiusZ, src.getDimension());
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Image destination, Number radius_x, Number radius_y, Number radius_z";
    }

    @Override
    public String getDescription() {
        return "Computes the local minimum of a pixels cube neighborhood. \n\nThe cubes size is specified by \n" +
                "its half-width, half-height and half-depth (radius).";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }

}
