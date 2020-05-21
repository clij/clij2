package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
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
 *         April 2020
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_depthColorProjection")
public class DepthColorProjection extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    protected Object[] getDefaultValues() {
        return new Object[]{null, null, null, 0, 255};
    }

    @Override
    public boolean executeCL() {
        boolean result = depthColorProjection(getCLIJ2(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), (ClearCLBuffer)(args[2]), asFloat(args[3]), asFloat(args[4]));
        return result;
    }

    public static boolean depthColorProjection(CLIJ2 clij2, ClearCLImageInterface src, ClearCLBuffer lut, ClearCLBuffer dst_depth, Float min_display_intensity, Float max_display_intensity ) {
        assertDifferent(src, dst_depth);
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst_depth", dst_depth);
        parameters.put("lut", lut);
        parameters.put("min_display_intensity", min_display_intensity);
        parameters.put("max_display_intensity", max_display_intensity);

        System.out.println("min_display_intensity " + min_display_intensity);
        System.out.println("max_display_intensity " + max_display_intensity);

        long[] dims = new long[]{dst_depth.getWidth(), dst_depth.getHeight()};

        if (src instanceof ClearCLImage) {
            clij2.execute(DepthColorProjection.class, "depth_projection_interpolation_x.cl", "depth_projection", dims, dims, parameters);
        } else {
            clij2.execute(DepthColorProjection.class, "depth_projection_x.cl", "depth_projection", dims, dims, parameters);
        }
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image lookup_table, ByRef Image destination_max, Number min_display_intensity, Number max_display_intensity";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        return clij.createCLBuffer(new long[]{input.getWidth(), input.getHeight(), 3}, input.getNativeType());
    }

    @Override
    public String getDescription() {
        return "Determines a maximum projection of an image stack and does a color coding of the determined arg Z (position of the found maximum). \n\n" +
                "Second parameter is a Lookup-Table in the form of an 8-bit image stack 255 pixels wide, 1 pixel high with 3 planes representing red, green and blue intensities.\n" +
                "Resulting image is a 3D image with three Z-planes representing red, green and blue channels.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }

}
