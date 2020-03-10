package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 *         August 2019
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_labelToMask")
public class LabelToMask extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        boolean result = labelToMask(getCLIJ2(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asFloat(args[2]));
        return result;
    }

    public static boolean labelToMask(CLIJ2 clij2, ClearCLBuffer labelMap, ClearCLBuffer maskOutput, Float index) {
        return EqualConstant.equalConstant(clij2, labelMap, maskOutput, index);
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
