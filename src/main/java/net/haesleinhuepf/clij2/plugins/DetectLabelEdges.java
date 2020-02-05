package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

/**
 * Author: @haesleinhuepf
 *         October 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_detectLabelEdges")
public class DetectLabelEdges extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        return detectLabelEdges(getCLIJ2(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]));
    }

    public static boolean detectLabelEdges(CLIJ2 clij2, ClearCLBuffer src_label_map, ClearCLBuffer dst_edge_image) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src_label_map", src_label_map);
        parameters.put("dst_edge_image", dst_edge_image);
        clij2.execute(DetectLabelEdges.class, "detect_label_edges_" + src_label_map.getDimension() + "d_x.cl", "detect_label_edges_diamond_" + src_label_map.getDimension() + "d", dst_edge_image.getDimensions(), dst_edge_image.getDimensions(), parameters);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image label_map, Image edge_image_destination";
    }

    @Override
    public String getDescription() {
        return "Takes a labelmap and returns an image where all pixels on label edges are set to 1 and all other pixels to 0.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
