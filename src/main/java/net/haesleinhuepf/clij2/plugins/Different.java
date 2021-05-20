package net.haesleinhuepf.clij2.plugins;


import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * February 2021
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_different")
public class Different extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Image";
    }

    @Override
    public String getOutputType() {
        return "Binary Image";
    }

    @Override
    public String getCategories() {
        return "Math";
    }

    @Override
    public String getParameterHelpText() {
        return "Image input_image1, Image input_image2, ByRef Image binary_destination, Number tolerance";
    }

    @Override
    public Object[] getDefaultValues() {
        return new Object[]{null, null, 10};
    }

    @Override
    public boolean executeCL() {
        boolean result = different(getCLIJ2(), (ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]), (ClearCLBuffer) (args[2]), asFloat(args[3]));
        return result;
    }

    public static boolean different(CLIJ2 clij2, ClearCLBuffer input_image1, ClearCLBuffer input_image2, ClearCLBuffer binary_destination, Float tolerance) {
        ClearCLBuffer temp = clij2.create(input_image1);
        clij2.absoluteDifference(input_image1, input_image2, temp);
        clij2.greaterConstant(temp, binary_destination, tolerance);
        temp.close();
        return true;
    }

    @Override
    public String getDescription() {
        return "Determines the absolute difference between two images and sets all pixels to 1 where it is above a given tolerance, and 0 otherwise.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
