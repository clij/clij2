package net.haesleinhuepf.clijx.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.radiusToKernelSize;
import static net.haesleinhuepf.clijx.utilities.CLIJUtilities.executeSeparableKernel;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_minimum3DBox")
public class Minimum3DBox extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        int radiusX = asInteger(args[2]);
        int radiusY = asInteger(args[3]);
        int radiusZ = asInteger(args[4]);

        return minimum3DBox(getCLIJx(), (ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]), radiusX, radiusY, radiusZ);
    }

    public static boolean minimum3DBox(CLIJx clijx, ClearCLImageInterface src, ClearCLImageInterface dst, int radiusX, int radiusY, int radiusZ) {
        return minimumBox(clijx, src, dst, radiusX, radiusY, radiusZ);
    }

    public static boolean minimumBox(CLIJx clijx, ClearCLImageInterface src, ClearCLImageInterface dst, int radiusX, int radiusY, int radiusZ) {
        return executeSeparableKernel(clijx, src, dst, Minimum3DBox.class, "minimum_separable_" + src.getDimension() + "d_x.cl", "minimum_separable_" + src.getDimension() + "d", radiusToKernelSize(radiusX), radiusToKernelSize(radiusY), radiusToKernelSize(radiusZ), radiusX, radiusY, radiusZ, src.getDimension());
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number radiusX, Number radiusY, Number radiusZ";
    }

    @Override
    public String getDescription() {
        return "Computes the local minimum of a pixels cube neighborhood. The cubes size is specified by \n" +
                "its half-width, half-height and half-depth (radius).";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }

}
