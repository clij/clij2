package net.haesleinhuepf.clijx.advancedfilters;

import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.advancedmath.EqualConstant;
import net.haesleinhuepf.clijx.advancedmath.GreaterConstant;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_meanOfPixelsAboveThreshold")
public class MeanOfPixelsAboveThreshold extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        double minVal = meanOfPixelsAboveThreshold(clij, (ClearCLBuffer)( args[0]), asFloat(args[1]));

        ResultsTable table = ResultsTable.getResultsTable();
        table.incrementCounter();
        table.addValue("Mean_of_pixels_above_threshold", minVal);
        table.show("Results");
        return true;
    }

    public static double meanOfPixelsAboveThreshold(CLIJ clij, ClearCLBuffer clImage, Float threshold) {
        ClearCLBuffer tempBinary = clij.create(clImage);
        // todo: if we can be sure that the mask has really only 0 and 1 pixel values, we can skip this first step:
        ClearCLBuffer tempMultiplied = clij.create(clImage);
        GreaterConstant.greaterConstant(clij, clImage, tempBinary, threshold);
        clij.op().mask(clImage, tempBinary, tempMultiplied);
        double sum = clij.op().sumPixels(tempMultiplied);
        double count = clij.op().sumPixels(tempBinary);
        tempBinary.close();
        tempMultiplied.close();
        return sum / count;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Number threshold";
    }

    @Override
    public String getDescription() {
        return "Determines the mean intensity in an image, but only in pixels which are above a given threshold.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
