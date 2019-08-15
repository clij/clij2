package net.haesleinhuepf.clij.advancedfilters;

import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_meanSquaredError")
public class MeanSquaredError extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        double meanSquaredError = 0;

        Object[] args = openCLBufferArgs();
        ClearCLBuffer buffer1 = (ClearCLBuffer)( args[0]);
        ClearCLBuffer buffer2 = (ClearCLBuffer)( args[1]);
        meanSquaredError = meanSquaredError(clij, buffer1, buffer2);
        releaseBuffers(args);


        ResultsTable table = ResultsTable.getResultsTable();
        table.incrementCounter();
        table.addValue("MSE", meanSquaredError);
        table.show("Results");
        return true;
    }

    public static double meanSquaredError(CLIJ clij, ClearCLBuffer buffer1, ClearCLBuffer buffer2) {
        ClearCLBuffer difference = clij.create(buffer1);
        ClearCLBuffer squared = clij.create(buffer1);

        clij.op().subtractImages(buffer1, buffer2, difference);

        clij.op().power(difference, squared, 2f);

        double mse = clij.op().sumPixels(squared) / squared.getWidth() / squared.getHeight() / squared.getDepth();

        difference.close();
        squared.close();

        return mse;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source1, Image source2";
    }

    @Override
    public String getDescription() {
        return "Determines the mean squared error (MSE) between two images. The MSE will be stored in a new row of ImageJs\n" +
                "Results table in the column 'MSE'.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
