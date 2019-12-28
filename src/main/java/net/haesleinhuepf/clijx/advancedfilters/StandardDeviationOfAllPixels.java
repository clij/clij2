package net.haesleinhuepf.clijx.advancedfilters;

import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 *         September 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_standardDeviationOfAllPixels")
public class StandardDeviationOfAllPixels extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        double stdDev = 0;

        ClearCLBuffer buffer1 = (ClearCLBuffer)( args[0]);

        stdDev = standardDeviationOfAllPixels(getCLIJx(), buffer1);

        ResultsTable table = ResultsTable.getResultsTable();
        table.incrementCounter();
        table.addValue("StandardDeviation", stdDev);
        table.show("Results");
        return true;
    }

    public static double standardDeviationOfAllPixels(CLIJx clijx, ClearCLImageInterface buffer1) {
        double meanIntensity = clijx.sumPixels(buffer1) / (buffer1.getWidth() * buffer1.getHeight() * buffer1.getDepth());
        return standardDeviationOfAllPixels(clijx, buffer1, new Float(meanIntensity));
    }

    public static double standardDeviationOfAllPixels(CLIJx clijx, ClearCLImageInterface buffer1, Float meanIntensity) {
        return Math.sqrt(VarianceOfAllPixels.varianceOfAllPixels(clijx, buffer1, meanIntensity));
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
