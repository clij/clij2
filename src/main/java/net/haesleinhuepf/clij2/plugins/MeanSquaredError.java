package net.haesleinhuepf.clij2.plugins;

import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
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
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_meanSquaredError")
public class MeanSquaredError extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        double meanSquaredError = 0;

        ClearCLBuffer buffer1 = (ClearCLBuffer)( args[0]);
        ClearCLBuffer buffer2 = (ClearCLBuffer)( args[1]);
        meanSquaredError = getCLIJ2().meanSquaredError(buffer1, buffer2);


        ResultsTable table = ResultsTable.getResultsTable();
        table.incrementCounter();
        table.addValue("MSE", meanSquaredError);
        table.show("Results");
        return true;
    }

    public static double meanSquaredError(CLIJ2 clij2, ClearCLBuffer buffer1, ClearCLBuffer buffer2) {
        ClearCLBuffer difference = clij2.create(buffer1.getDimensions(), NativeTypeEnum.Float);
        ClearCLBuffer squared = clij2.create(buffer1.getDimensions(), NativeTypeEnum.Float);

        clij2.subtractImages(buffer1, buffer2, difference);

        clij2.power(difference, squared, 2f);

        double mse = clij2.sumOfAllPixels(squared) / squared.getWidth() / squared.getHeight() / squared.getDepth();

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
        return "Determines the mean squared error (MSE) between two images. \n\nThe MSE will be stored in a new row of ImageJs\n" +
                "Results table in the column 'MSE'.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
