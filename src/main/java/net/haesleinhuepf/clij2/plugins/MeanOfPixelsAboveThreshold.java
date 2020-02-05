package net.haesleinhuepf.clij2.plugins;

import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.plugins.GreaterConstant;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_meanOfPixelsAboveThreshold")
public class MeanOfPixelsAboveThreshold extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        double minVal = meanOfPixelsAboveThreshold(getCLIJ2(), (ClearCLBuffer)( args[0]), asFloat(args[1]));

        ResultsTable table = ResultsTable.getResultsTable();
        table.incrementCounter();
        table.addValue("Mean_of_pixels_above_threshold", minVal);
        table.show("Results");
        return true;
    }

    public static double meanOfPixelsAboveThreshold(CLIJ2 clij2, ClearCLBuffer clImage, Float threshold) {
        ClearCLBuffer tempBinary = clij2.create(clImage);
        // todo: if we can be sure that the mask has really only 0 and 1 pixel values, we can skip this first step:
        ClearCLBuffer tempMultiplied = clij2.create(clImage);
        GreaterConstant.greaterConstant(clij2, clImage, tempBinary, threshold);
        clij2.mask(clImage, tempBinary, tempMultiplied);
        double sum = clij2.sumPixels(tempMultiplied);
        double count = clij2.sumPixels(tempBinary);
        clij2.release(tempBinary);
        clij2.release(tempMultiplied);
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
