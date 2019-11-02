package net.haesleinhuepf.clijx.advancedfilters;

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
 *         September 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_standardDeviationOfAllPixels")
public class StandardDeviationOfAllPixels extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        double variance = 0;

        Object[] args = openCLBufferArgs();
        ClearCLBuffer buffer1 = (ClearCLBuffer)( args[0]);

        variance = standardDeviationOfAllPixels(clij, buffer1);
        releaseBuffers(args);


        ResultsTable table = ResultsTable.getResultsTable();
        table.incrementCounter();
        table.addValue("Variance", variance);
        table.show("Results");
        return true;
    }

    public static double standardDeviationOfAllPixels(CLIJ clij, ClearCLBuffer buffer1) {
        double meanIntensity = clij.op().sumPixels(buffer1) / (buffer1.getWidth() * buffer1.getHeight() * buffer1.getDepth());
        return standardDeviationOfAllPixels(clij, buffer1, new Float(meanIntensity));
    }

    public static double standardDeviationOfAllPixels(CLIJ clij, ClearCLBuffer buffer1, Float meanIntensity) {
        return Math.sqrt(VarianceOfAllPixels.varianceOfAllPixels(clij, buffer1, meanIntensity));
    }

    @Override
    public String getParameterHelpText() {
        return "Image source";
    }

    @Override
    public String getDescription() {
        return "Determines the standard deviation of all pixels in an image. The value will be stored in a new row of ImageJs\n" +
                "Results table in the column 'Standard_deviation'.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
