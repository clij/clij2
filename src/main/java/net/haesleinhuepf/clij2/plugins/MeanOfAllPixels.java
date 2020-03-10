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

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_meanOfAllPixels")
public class MeanOfAllPixels extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        double mean = meanOfAllPixels(getCLIJ2(), (ClearCLBuffer) args[0]);

        ResultsTable table = ResultsTable.getResultsTable();
        table.incrementCounter();
        table.addValue("Mean", mean);
        table.show("Results");
        return true;
    }

    public static double meanOfAllPixels(CLIJ2 clij2, ClearCLImageInterface image) {
        return clij2.sumOfAllPixels(image) / (image.getWidth() * image.getHeight() * image.getDepth());
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
