package net.haesleinhuepf.clij2.plugins;

import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_meanOfMaskedPixels")
public class MeanOfMaskedPixels extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized {
    @Override
    public String getCategories() {
        return "Binary, Measurements";
    }

    @Override
    public boolean executeCL() {
        double minVal = getCLIJ2().meanOfMaskedPixels((ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]));

        ResultsTable table = ResultsTable.getResultsTable();
        table.incrementCounter();
        table.addValue("Masked_mean", minVal);
        table.show("Results");
        return true;
    }

    public static double meanOfMaskedPixels(CLIJ2 clij2, ClearCLBuffer clImage, ClearCLBuffer mask) {
        ClearCLBuffer tempBinary = clij2.create(clImage);
        // todo: if we can be sure that the mask has really only 0 and 1 pixel values, we can skip this first step:
        ClearCLBuffer tempMultiplied = clij2.create(clImage);
        EqualConstant.equalConstant(clij2, mask, tempBinary, 1f);
        clij2.mask(clImage, tempBinary, tempMultiplied);
        double sum = clij2.sumPixels(tempMultiplied);
        double count = clij2.sumPixels(tempBinary);
        clij2.release(tempBinary);
        clij2.release(tempMultiplied);
        return sum / count;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image mask";
    }

    @Override
    public String getDescription() {
        return "Determines the mean intensity in a masked image. \n\nOnly in pixels which have non-zero values in another" +
                " binary mask image.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
