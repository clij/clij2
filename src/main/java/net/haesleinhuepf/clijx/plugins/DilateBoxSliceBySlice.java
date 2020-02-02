package net.haesleinhuepf.clijx.plugins;

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
import static net.haesleinhuepf.clijx.utilities.CLIJUtilities.checkDimensions;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_dilateBoxSliceBySlice")
public class DilateBoxSliceBySlice extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        dilateBoxSliceBySlice(getCLIJx(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]));
        return true;
    }


    public static boolean dilateBoxSliceBySlice(CLIJx clijx, ClearCLImageInterface src, ClearCLImageInterface dst) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }
        clijx.execute(DilateBoxSliceBySlice.class, "dilate_box_slice_by_slice_3d_x.cl", "dilate_box_slice_by_slice_3d", dst.getDimensions(), dst.getDimensions(), parameters);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination";
    }

    @Override
    public String getDescription() {
        return "Computes a binary image with pixel values 0 and 1 containing the binary dilation of a given input image.\n" +
                "The dilation takes the Moore-neighborhood (8 pixels in 2D and 26 pixels in 3d) into account.\n" +
                "The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.\n\n" +
                "This method is comparable to the 'Dilate' menu in ImageJ in case it is applied to a 2D image. The only\n" +
                "difference is that the output image contains values 0 and 1 instead of 0 and 255.\n\n" +
                "This filter is applied slice by slice in 2D.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }
}
