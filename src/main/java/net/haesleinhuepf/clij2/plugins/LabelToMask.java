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
 *         August 2019
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_labelToMask")
public class LabelToMask extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Label Image";
    }

    @Override
    public String getOutputType() {
        return "Binary Image";
    }


    @Override
    public Object[] getDefaultValues() {
        return new Object[]{null, null, 1};
    }

    @Override
    public boolean executeCL() {
        boolean result = getCLIJ2().labelToMask((ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asFloat(args[2]));
        return result;
    }

    public static boolean labelToMask(CLIJ2 clij2, ClearCLBuffer labelMap, ClearCLBuffer maskOutput, Float index) {
        return EqualConstant.equalConstant(clij2, labelMap, maskOutput, index);
    }

    @Override
    public String getParameterHelpText() {
        return "Image label_map_source, ByRef Image mask_destination, Number label_index";
    }

    @Override
    public String getDescription() {
        return "Masks a single label in a label map. \n\nSets all pixels in the target image to 1, where the given label" +
                " index was present in the label map. Other pixels are set to 0.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public String getCategories() {
        return "Binary, Label";
    }
}
