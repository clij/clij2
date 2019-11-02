package net.haesleinhuepf.clijx.advancedfilters;

import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clijx.advancedmath.EqualConstant;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_meanOfMaskedPixels")
public class MeanOfMaskedPixels extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        double minVal = meanOfMaskedPixels(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]));

        ResultsTable table = ResultsTable.getResultsTable();
        table.incrementCounter();
        table.addValue("Masked_mean", minVal);
        table.show("Results");
        return true;
    }

    public static double meanOfMaskedPixels(CLIJ clij, ClearCLBuffer clImage, ClearCLBuffer mask) {
        ClearCLBuffer tempBinary = clij.create(clImage);
        // todo: if we can be sure that the mask has really only 0 and 1 pixel values, we can skip this first step:
        ClearCLBuffer tempMultiplied = clij.create(clImage);
        EqualConstant.equalConstant(clij, mask, tempBinary, 1f);
        clij.op().mask(clImage, tempBinary, tempMultiplied);
        double sum = clij.op().sumPixels(tempMultiplied);
        double count = clij.op().sumPixels(tempBinary);
        tempBinary.close();
        tempMultiplied.close();
        return sum / count;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image mask";
    }

    @Override
    public String getDescription() {
        return "Determines the mean intensity in an image, but only in pixels which have non-zero values in another" +
                " binary mask image.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
