package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;
import static net.haesleinhuepf.clij.utilities.CLIJUtilities.radiusToKernelSize;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_countNonZeroPixelsSliceBySliceSphere")
public class CountNonZeroPixelsSliceBySliceSphere extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        return getCLIJ2().countNonZeroPixelsLocallySliceBySlice((ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asInteger(args[2]), asInteger(args[3]));
    }

    @Deprecated
    public static boolean countNonZeroPixelsLocallySliceBySlice(CLIJ2 clij2, ClearCLBuffer src, ClearCLBuffer dst, Integer radiusX, Integer radiusY) {
        return countNonZeroPixelsSliceBySliceSphere(clij2, src, dst, radiusX, radiusY);
    }

    public static boolean countNonZeroPixelsSliceBySliceSphere(CLIJ2 clij2, ClearCLBuffer src, ClearCLBuffer dst, Integer radiusX, Integer radiusY) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("Nx", radiusToKernelSize(radiusX));
        parameters.put("Ny", radiusToKernelSize(radiusY));
        parameters.put("src", src);
        parameters.put("dst", dst);
        clij2.execute(CountNonZeroPixelsSliceBySliceSphere.class, "count_nonzero_pixels_slice_by_slice_sphere_3d_x.cl", "count_nonzero_pixels_slice_by_slice_sphere_3d", dst.getDimensions(), dst.getDimensions(), parameters);
        return true;
    }


    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Image destination, Number radiusX, Number radiusY";
    }

    @Override
    public String getDescription() {
        return "Counts non-zero pixels in a sphere around every pixel slice by slice in a stack. \n\n It puts the resulting number in the destination image stack.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }
}
