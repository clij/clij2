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
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_threshold")
public class Threshold extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
         return threshold(getCLIJ2(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asFloat(args[2]));
    }

    public static boolean threshold(CLIJ2 clij2, ClearCLImageInterface input, ClearCLImageInterface output, Float threshold) {
        return clij2.greaterOrEqualConstant(input, output, threshold);
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Image destination, Number threshold";
    }

    @Override
    public String getDescription() {
        return "Computes a binary image with pixel values 0 and 1. \n\nAll pixel values x of a given input image with \n" +
                "value larger or equal to a given threshold t will be set to 1.\n\n" +
                "f(x,t) = (1 if (x >= t); (0 otherwise))\n\n" +
                "This plugin is comparable to setting a raw threshold in ImageJ and using the 'Convert to Mask' menu.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
