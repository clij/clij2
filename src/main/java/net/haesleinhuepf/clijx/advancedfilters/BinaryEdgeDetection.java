package net.haesleinhuepf.clijx.advancedfilters;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

/**
 * Author: @haesleinhuepf
 *         September 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_binaryEdgeDetection")
public class BinaryEdgeDetection extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();

        ClearCLBuffer src = (ClearCLBuffer)( args[0]);
        ClearCLBuffer dst = (ClearCLBuffer)( args[1]);

        binaryEdgeDetection(getCLIJx(), src, dst);

        return true;
    }

    public static boolean binaryEdgeDetection(CLIJx clijx, ClearCLImageInterface src, ClearCLImageInterface dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);

        clijx.execute(BinaryEdgeDetection.class, "binaryEdgeDetection" + dst.getDimension() + "d_x.cl", "binary_edge_detection_diamond_image" + dst.getDimension() +  "d", dst.getDimensions(), dst.getDimensions(), parameters);

        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination";
    }

    @Override
    public String getDescription() {
        return "Determines pixels/voxels which are on the surface of a binary objects and sets only them to 1 in the destination image.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
