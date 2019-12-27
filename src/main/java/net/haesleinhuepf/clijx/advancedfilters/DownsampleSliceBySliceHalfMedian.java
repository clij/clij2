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

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_downsampleSliceBySliceHalfMedian")
public class DownsampleSliceBySliceHalfMedian extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        return downsampleSliceBySliceHalfMedian(getCLIJx(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]));
    }

    //
    public static boolean downsampleSliceBySliceHalfMedian(CLIJx clijx, ClearCLImageInterface src, ClearCLImageInterface dst) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        clijx.execute(DownsampleSliceBySliceHalfMedian.class, "downsample_xy_by_half_median_3d_x.cl", "downsample_xy_by_half_median_3d", dst.getDimensions(), dst.getDimensions(), parameters);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        return clij.createCLBuffer(new long[]{input.getWidth() / 2, input.getHeight() / 2, input.getDepth()}, input.getNativeType());
    }


    @Override
    public String getDescription() {
        return "Scales an image using scaling factors 0.5 for X and Y dimensions. The Z dimension stays untouched. Thus, each slice is processed separately.\n" +
                "The median method is applied. Thus, each pixel value in the destination image equals to the median of\n" +
                "four corresponding pixels in the source image.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }
}
