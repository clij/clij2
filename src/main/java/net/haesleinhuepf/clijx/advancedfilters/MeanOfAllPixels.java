package net.haesleinhuepf.clijx.advancedfilters;

import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
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
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_meanOfAllPixels")
public class MeanOfAllPixels extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        double mean = meanOfAllPixels(getCLIJx(), (ClearCLBuffer) args[0]);

        ResultsTable table = ResultsTable.getResultsTable();
        table.incrementCounter();
        table.addValue("Mean", mean);
        table.show("Results");
        return true;
    }

    public static double meanOfAllPixels(CLIJx clijx, ClearCLImageInterface image) {
        return clijx.sumOfAllPixels(image) / (image.getWidth() * image.getHeight() * image.getDepth());
    }

    @Override
    public String getParameterHelpText() {
        return "Image source";
    }

    @Override
    public String getDescription() {
        return "Determines the mean average of all pixels in a given image. It will be stored in a new row of ImageJs\n" +
                "Results table in the column 'Mean'.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
