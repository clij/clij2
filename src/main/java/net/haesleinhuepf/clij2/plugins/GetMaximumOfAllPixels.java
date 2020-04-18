package net.haesleinhuepf.clij2.plugins;

import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

import java.nio.FloatBuffer;

/**
 * Author: @haesleinhuepf
 *         April 2020
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_getMaximumOfAllPixels")
public class GetMaximumOfAllPixels extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        double maximumGreyValue = getMaximumOfAllPixels(getCLIJ2(), (ClearCLBuffer)( args[0]));

        ((Double[])args[1])[0] = maximumGreyValue;

        return true;
    }


    public static double getMaximumOfAllPixels(CLIJ2 clij2, ClearCLImageInterface clImage) {
        return clij2.maximumOfAllPixels(clImage);
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Number maximum_of_all_pixels";
    }

    @Override
    public String getDescription() {
        return "Determines the maximum of all pixels in a given image. It will be stored in the variable maximum_of_all_pixels";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
