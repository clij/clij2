package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 *         August 2020
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_getMeanOfMaskedPixels")
public class GetMeanOfMaskedPixels extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized {
    @Override
    public String getCategories() {
        return "Measurements";
    }

    @Override
    public boolean executeCL() {
        double meanGreyValue = getCLIJ2().getMeanOfMaskedPixels((ClearCLBuffer)( args[0]), (ClearCLBuffer)( args[1]));

        ((Double[])args[1])[0] = meanGreyValue;

        return true;
    }


    public static double getMeanOfMaskedPixels(CLIJ2 clij2, ClearCLBuffer image, ClearCLBuffer mask_image) {
        return clij2.meanOfMaskedPixels(image, mask_image);
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Number mean_of_masked_pixels";
    }

    @Override
    public String getDescription() {
        return "Determines the mean of all pixels in a given image which have non-zero value in a corresponding mask image. \n\nIt will be stored in the variable mean_of_masked_pixels.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
