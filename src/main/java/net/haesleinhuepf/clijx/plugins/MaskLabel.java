package net.haesleinhuepf.clijx.plugins;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_maskLabel")
public class MaskLabel extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();
        boolean result = maskLabel(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), (ClearCLBuffer)(args[2]), asFloat(args[3]));
        releaseBuffers(args);
        return result;
    }

    public static boolean maskLabel(CLIJ clij, ClearCLBuffer input, ClearCLBuffer label_map, ClearCLBuffer output, Float index) {

        HashMap<String, Object> parameters = new HashMap<>();

        parameters.put("src", input);
        parameters.put("src_label_map", label_map);
        parameters.put("label_id", index);
        parameters.put("dst", output);

        long dimensions = input.getDimension();

        return clij.execute(MaskLabel.class, "masklabel.cl", "mask_label_" + dimensions + "d", parameters);
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image label_map, Image destination, Number label_index";
    }

    @Override
    public String getDescription() {
        return "Computes a masked image by applying a label mask to an image. All pixel values x of image X will be copied\n" +
                "to the destination image in case pixel value m at the same position in the label_map image has the right index value i.\n\n" +
                "f(x,m,i) = (x if (m == i); (0 otherwise))";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
