package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;

/**
 * Author: @haesleinhuepf
 * December 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_meanZProjectionBounded")
public class MeanZProjectionBounded extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();
        boolean result = projectMeanZBounded(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asInteger(args[2]), asInteger(args[3]));
        releaseBuffers(args);
        return result;
    }

    public static boolean projectMeanZBounded(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst_mean, Integer min_z, Integer max_z) {
        assertDifferent(src, dst_mean);
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst_mean", dst_mean);
        parameters.put("min_z", min_z);
        parameters.put("max_z", max_z);
        return clij.execute(MeanZProjectionBounded.class, "mean_z_projection_bounded_3d_2d_x.cl", "mean_z_projection_bounded_3d_2d", parameters);
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination_mean, Number min_z, Number max_z";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        return clij.createCLBuffer(new long[]{input.getWidth(), input.getHeight()}, input.getNativeType());
    }

    @Override
    public String getDescription() {
        return "Determines the mean projection of an image along Z within a given z range.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }

}
