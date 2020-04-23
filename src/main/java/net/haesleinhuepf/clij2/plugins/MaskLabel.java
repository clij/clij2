package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_maskLabel")
public class MaskLabel extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        boolean result = getCLIJ2().maskLabel((ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), (ClearCLBuffer)(args[2]), asFloat(args[3]));
        return result;
    }

    public static boolean maskLabel(CLIJ2 clij2, ClearCLBuffer input, ClearCLBuffer label_map, ClearCLBuffer output, Float index) {

        HashMap<String, Object> parameters = new HashMap<>();

        parameters.put("src", input);
        parameters.put("src_label_map", label_map);
        parameters.put("label_id", index);
        parameters.put("dst", output);

        long dimensions = input.getDimension();

        clij2.execute(MaskLabel.class, "mask_label_" + output.getDimension() + "d_x.cl", "mask_label_" + dimensions + "d", output.getDimensions(),output.getDimensions(), parameters);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image label_map, ByRef Image destination, Number label_index";
    }

    @Override
    public String getDescription() {
        return "Computes a masked image by applying a label mask to an image. \n\nAll pixel values x of image X will be copied\n" +
                "to the destination image in case pixel value m at the same position in the label_map image has the right index value i.\n\n" +
                "f(x,m,i) = (x if (m == i); (0 otherwise))";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
