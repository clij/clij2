package net.haesleinhuepf.clijx.plugins;

import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.plugins.GreaterConstant;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_meanOfPixelsAboveThreshold")
public class MeanOfPixelsAboveThreshold extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        double minVal = meanOfPixelsAboveThreshold(getCLIJx(), (ClearCLBuffer)( args[0]), asFloat(args[1]));

        ResultsTable table = ResultsTable.getResultsTable();
        table.incrementCounter();
        table.addValue("Mean_of_pixels_above_threshold", minVal);
        table.show("Results");
        return true;
    }

    public static double meanOfPixelsAboveThreshold(CLIJx clijx, ClearCLBuffer clImage, Float threshold) {
        ClearCLBuffer tempBinary = clijx.create(clImage);
        // todo: if we can be sure that the mask has really only 0 and 1 pixel values, we can skip this first step:
        ClearCLBuffer tempMultiplied = clijx.create(clImage);
        GreaterConstant.greaterConstant(clijx, clImage, tempBinary, threshold);
        clijx.mask(clImage, tempBinary, tempMultiplied);
        double sum = clijx.sumPixels(tempMultiplied);
        double count = clijx.sumPixels(tempBinary);
        clijx.release(tempBinary);
        clijx.release(tempMultiplied);
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
