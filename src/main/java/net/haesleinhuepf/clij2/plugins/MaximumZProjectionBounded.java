package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;

/**
 * Author: @haesleinhuepf
 * December 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_maximumZProjectionBounded")
public class MaximumZProjectionBounded extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public Object[] getDefaultValues() {
        return new Object[]{null, null, 0, 100};
    }

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();
        boolean result = getCLIJ2().maximumZProjectionBounded((ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asInteger(args[2]), asInteger(args[3]));
        releaseBuffers(args);
        return result;
    }

    public static boolean maximumZProjectionBounded(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst_max, Integer min_z, Integer max_z) {
        assertDifferent(src, dst_max);
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst_max", dst_max);
        parameters.put("min_z", min_z);
        parameters.put("max_z", max_z);
        clij2.execute(MaximumZProjectionBounded.class, "maximum_z_projection_bounded_3d_2d_x.cl", "maximum_z_projection_bounded", dst_max.getDimensions(), dst_max.getDimensions(), parameters);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Image destination_max, Number min_z, Number max_z";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        return clij.createCLBuffer(new long[]{input.getWidth(), input.getHeight()}, input.getNativeType());
    }

    @Override
    public String getDescription() {
        return "Determines the maximum intensity projection of an image along Z within a given z range.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D -> 2D";
    }

}
