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
 *         September 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_standardDeviationOfAllPixels")
public class StandardDeviationOfAllPixels extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        double stdDev = 0;

        ClearCLBuffer buffer1 = (ClearCLBuffer)( args[0]);

        stdDev = standardDeviationOfAllPixels(getCLIJ2(), buffer1);

        ResultsTable table = ResultsTable.getResultsTable();
        table.incrementCounter();
        table.addValue("StandardDeviation", stdDev);
        table.show("Results");
        return true;
    }

    public static double standardDeviationOfAllPixels(CLIJ2 clij2, ClearCLImageInterface buffer1) {
        double meanIntensity = clij2.sumPixels(buffer1) / (buffer1.getWidth() * buffer1.getHeight() * buffer1.getDepth());
        return standardDeviationOfAllPixels(clij2, buffer1, new Float(meanIntensity));
    }

    public static double standardDeviationOfAllPixels(CLIJ2 clij2, ClearCLImageInterface buffer1, Float meanIntensity) {
        return Math.sqrt(VarianceOfAllPixels.varianceOfAllPixels(clij2, buffer1, meanIntensity));
    }

    @Override
    public String getParameterHelpText() {
        return "Image source";
    }

    @Override
    public String getDescription() {
        return "Determines the standard deviation of all pixels in an image. \n\nThe value will be stored in a new row of ImageJs\n" +
                "Results table in the column 'Standard_deviation'.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
