package net.haesleinhuepf.clijx.advancedfilters;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
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
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_countNonZeroVoxels3DSphere")
public class CountNonZeroVoxels3DSphere extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        return countNonZeroVoxelsLocally(getCLIJx(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asInteger(args[2]), asInteger(args[3]), asInteger(args[4]));
    }

    public static boolean countNonZeroVoxelsLocally(CLIJx clijx, ClearCLBuffer src, ClearCLBuffer dst, Integer radiusX, Integer radiusY, Integer radiusZ) {
        return countNonZeroVoxels3DSphere(clijx, src, dst, radiusX, radiusY, radiusZ);
    }

    public static boolean countNonZeroVoxels3DSphere(CLIJx clijx, ClearCLBuffer src, ClearCLBuffer dst, Integer radiusX, Integer radiusY, Integer radiusZ) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("Nx", radiusToKernelSize(radiusX));
        parameters.put("Ny", radiusToKernelSize(radiusY));
        parameters.put("Nz", radiusToKernelSize(radiusZ));
        parameters.put("src", src);
        parameters.put("dst", dst);
        clijx.execute(Kernels.class, "count_nonzero_voxels_sphere_3d_x.cl", "count_nonzero_voxels_sphere_3d", dst.getDimensions(), dst.getDimensions(), parameters);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number radiusX, Number radiusY, Number radiusZ";
    }

    @Override
    public String getDescription() {
        return "Counts non-zero voxels in a sphere around every voxel." +
                "Put the number in the result image.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }
}
