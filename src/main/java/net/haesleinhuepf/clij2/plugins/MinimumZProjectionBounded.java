package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;

/**
 * Author: @haesleinhuepf
 * December 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_minimumZProjectionBounded")
public class MinimumZProjectionBounded extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        boolean result = minimumZProjectionBounded(getCLIJ2(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asInteger(args[2]), asInteger(args[3]));
        return result;
    }

    public static boolean minimumZProjectionBounded(CLIJ2 clij2, ClearCLBuffer src, ClearCLBuffer dst_min, Integer min_z, Integer max_z) {
        assertDifferent(src, dst_min);
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst_min", dst_min);
        parameters.put("min_z", min_z);
        parameters.put("max_z", max_z);
        clij2.execute(MinimumZProjectionBounded.class, "minimum_z_projection_bounded_3d_2d_x.cl", "minimum_z_projection_bounded_3d_2d", dst_min.getDimensions(), dst_min.getDimensions(), parameters);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination_min, Number min_z, Number max_z";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        return getCLIJ2().create(new long[]{input.getWidth(), input.getHeight()}, input.getNativeType());
    }

    @Override
    public String getDescription() {
        return "Determines the minimum projection of an image along Z within a given z range.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }

}
