package net.haesleinhuepf.clijx.advancedfilters;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;
import static net.haesleinhuepf.clij.utilities.CLIJUtilities.radiusToKernelSize;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_countNonZeroPixelsSliceBySliceSphere")
public class CountNonZeroPixelsSliceBySliceSphere extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        return countNonZeroPixelsLocallySliceBySlice(getCLIJx(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asInteger(args[2]), asInteger(args[3]));
    }

    public static boolean countNonZeroPixelsLocallySliceBySlice(CLIJx clijx, ClearCLBuffer src, ClearCLBuffer dst, Integer radiusX, Integer radiusY) {
        return countNonZeroPixelsSliceBySliceSphere(clijx, src, dst, radiusX, radiusY);
    }

    public static boolean countNonZeroPixelsSliceBySliceSphere(CLIJx clijx, ClearCLBuffer src, ClearCLBuffer dst, Integer radiusX, Integer radiusY) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("Nx", radiusToKernelSize(radiusX));
        parameters.put("Ny", radiusToKernelSize(radiusY));
        parameters.put("src", src);
        parameters.put("dst", dst);
        clijx.execute(CountNonZeroPixelsSliceBySliceSphere.class, "count_nonzero_pixels_slice_by_slice_sphere_3d_x.cl", "count_nonzero_pixels_slice_by_slice_sphere_3d", dst.getDimensions(), dst.getDimensions(), parameters);
        return true;
    }


    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number radiusX, Number radiusY";
    }

    @Override
    public String getDescription() {
        return "Counts non-zero pixels in a sphere around every pixel slice by slice in a stack and puts the resulting number in the destination image stack.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }
}
