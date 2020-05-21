package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_minimum2DBox")
public class Minimum2DBox extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    protected Object[] getDefaultValues() {
        return new Object[]{null, null, 10, 10};
    }

    @Override
    public boolean executeCL() {
        int radiusX = asInteger(args[2]);
        int radiusY = asInteger(args[3]);

        return getCLIJ2().minimum2DBox((ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]), radiusX, radiusY);
    }

    public static boolean minimumBox(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst, Integer radiusX, Integer radiusY) {
        return Minimum3DBox.minimumBox(clij2, src, dst, radiusX, radiusY, 0);
    }

    public static boolean minimum2DBox(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst, Integer radiusX, Integer radiusY) {
        return Minimum3DBox.minimumBox(clij2, src, dst, radiusX, radiusY, 0);
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Image destination, Number radiusX, Number radiusY";
    }

    @Override
    public String getDescription() {
        return "Computes the local minimum of a pixels rectangular neighborhood. \n\nThe rectangles size is specified by \n" +
                "its half-width and half-height (radius).";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }

}
