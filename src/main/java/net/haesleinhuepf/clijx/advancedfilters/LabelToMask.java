package net.haesleinhuepf.clijx.advancedfilters;

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
 *         August 2019
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_labelToMask")
public class LabelToMask extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        boolean result = labelToMask(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asFloat(args[2]));
        return result;
    }

    public static boolean labelToMask(CLIJ clij, ClearCLBuffer labelMap, ClearCLBuffer maskOutput, Float index) {
        return EqualConstant.equalConstant(clij, labelMap, maskOutput, index);
    }

    @Override
    public String getParameterHelpText() {
        return "Image label_map_source, Image mask_destination, Number label_index";
    }

    @Override
    public String getDescription() {
        return "Masks a single label in a label map: Sets all pixels in the target image to 1, where the given label" +
                " index was present in the label map. Other pixels are set to 0.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
