package net.haesleinhuepf.clijx.advancedfilters;

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
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_projectMaximumZBounded")
public class ProjectMaximumZBounded extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();
        boolean result = projectMaximumZBounded(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asInteger(args[2]), asInteger(args[3]));
        releaseBuffers(args);
        return result;
    }

    public static boolean projectMaximumZBounded(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst_max, Integer min_z, Integer max_z) {
        assertDifferent(src, dst_max);
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst_max", dst_max);
        parameters.put("min_z", min_z);
        parameters.put("max_z", max_z);
        return clij.execute(ProjectMaximumZBounded.class, "project_maximum_z_bounded.cl", "project_maximum_z_bounded", parameters);
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination_max, Number min_z, Number max_z";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        return clij.createCLBuffer(new long[]{input.getWidth(), input.getHeight()}, input.getNativeType());
    }

    @Override
    public String getDescription() {
        return "Determines the maximum projection of an image along Z within a given z range.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }

}
