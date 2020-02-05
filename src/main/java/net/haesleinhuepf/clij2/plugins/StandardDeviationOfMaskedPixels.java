package net.haesleinhuepf.clij2.plugins;

import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 *         September 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_standardDeviationOfMaskedPixels")
public class StandardDeviationOfMaskedPixels extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        double variance = 0;

        Object[] args = openCLBufferArgs();
        ClearCLBuffer buffer1 = (ClearCLBuffer)( args[0]);
        ClearCLBuffer mask = (ClearCLBuffer)( args[1]);

        variance = standardDeviationOfMaskedPixels(getCLIJ2(), buffer1, mask);
        releaseBuffers(args);


        ResultsTable table = ResultsTable.getResultsTable();
        table.incrementCounter();
        table.addValue("Masked_standard_deviation", variance);
        table.show("Results");
        return true;
    }

    public static double standardDeviationOfMaskedPixels(CLIJ2 clij2, ClearCLBuffer buffer1, ClearCLBuffer mask) {
        double meanIntensity = MeanOfMaskedPixels.meanOfMaskedPixels(clij2, buffer1, mask);
        return standardDeviationOfMaskedPixels(clij2, buffer1, mask, new Float(meanIntensity));
    }

    public static double standardDeviationOfMaskedPixels(CLIJ2 clij2, ClearCLBuffer buffer1, ClearCLBuffer mask, Float meanIntensity) {
        return Math.sqrt(VarianceOfMaskedPixels.varianceOfMaskedPixels(clij2, buffer1, mask, meanIntensity));
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image mask";
    }

    @Override
    public String getDescription() {
        return "Determines the standard deviation of all pixels in an image which have non-zero value in a corresponding mask image. The value will be stored in a new row of ImageJs\n" +
                "Results table in the column 'Masked_standard_deviation'.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
