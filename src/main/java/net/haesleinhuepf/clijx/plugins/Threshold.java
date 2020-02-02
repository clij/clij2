package net.haesleinhuepf.clijx.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_threshold")
public class Threshold extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
         return threshold(getCLIJx(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asFloat(args[2]));
    }

    public static boolean threshold(CLIJx clijx, ClearCLBuffer input, ClearCLBuffer output, Float threshold) {
        return clijx.greaterOrEqualConstant(input, output, threshold);
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number threshold";
    }

    @Override
    public String getDescription() {
        return "Computes a binary image with pixel values 0 and 1. All pixel values x of a given input image with \n" +
                "value larger or equal to a given threshold t will be set to 1.\n\n" +
                "f(x,t) = (1 if (x >= t); (0 otherwise))\n\n" +
                "This plugin is comparable to setting a raw threshold in ImageJ and using the 'Convert to Mask' menu.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
