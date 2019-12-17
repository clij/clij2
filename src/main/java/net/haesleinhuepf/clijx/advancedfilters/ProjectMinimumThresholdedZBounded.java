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
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_projectMinimumThresholdedZBounded")
public class ProjectMinimumThresholdedZBounded extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();
        boolean result = projectMinimumThresholdedZBounded(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asFloat(args[2]), asInteger(args[3]), asInteger(args[4]));
        releaseBuffers(args);
        return result;
    }

    public static boolean projectMinimumThresholdedZBounded(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst_min, Float threshold_intensity, Integer min_z, Integer max_z) {
        assertDifferent(src, dst_min);
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst_min", dst_min);
        parameters.put("threshold_intensity", threshold_intensity);
        parameters.put("min_z", min_z);
        parameters.put("max_z", max_z);
        return clij.execute(ProjectMinimumThresholdedZBounded.class, "project_minimum_thresholded_z_bounded.cl", "project_minimum_thresholded_z_bounded", parameters);
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination_min, Number min_z, Number max_z";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        return clij.createCLBuffer(new long[]{input.getWidth(), input.getHeight()}, input.getNativeType());
    }

    @Override
    public String getDescription() {
        return "Determines the minimum projection of all pixels in an image above a given threshold along Z within a given z range.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }

}
