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
 *         April 2020
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_getMinimumOfAllPixels")
public class GetMinimumOfAllPixels extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        double minimumGreyValue = getCLIJ2().getMinimumOfAllPixels((ClearCLBuffer)( args[0]));

        ((Double[])args[1])[0] = minimumGreyValue;

        return true;
    }


    public static double getMinimumOfAllPixels(CLIJ2 clij2, ClearCLImageInterface clImage) {
        return clij2.minimumOfAllPixels(clImage);
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Number minimum_of_all_pixels";
    }

    @Override
    public String getDescription() {
        return "Determines the minimum of all pixels in a given image. \n\nIt will be stored in the variable minimum_of_all_pixels.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
