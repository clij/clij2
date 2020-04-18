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
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_downsampleSliceBySliceHalfMedian")
public class DownsampleSliceBySliceHalfMedian extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        return downsampleSliceBySliceHalfMedian(getCLIJ2(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]));
    }

    //
    public static boolean downsampleSliceBySliceHalfMedian(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        clij2.execute(DownsampleSliceBySliceHalfMedian.class, "downsample_xy_by_half_median_3d_x.cl", "downsample_xy_by_half_median_3d", dst.getDimensions(), dst.getDimensions(), parameters);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Image destination";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        return clij.createCLBuffer(new long[]{input.getWidth() / 2, input.getHeight() / 2, input.getDepth()}, input.getNativeType());
    }


    @Override
    public String getDescription() {
        return "Scales an image using scaling factors 0.5 for X and Y dimensions. The Z dimension stays untouched. \n\n" +
                "Thus, each slice is processed separately.\n" +
                "The median method is applied. Thus, each pixel value in the destination image equals to the median of\n" +
                "four corresponding pixels in the source image.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }
}
