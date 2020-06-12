package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;
import static net.haesleinhuepf.clij.utilities.CLIJUtilities.radiusToKernelSize;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_countNonZeroVoxels3DSphere")
public class CountNonZeroVoxels3DSphere extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized {
    @Override
    public String getCategories() {
        return "Measurements";
    }

    @Override
    public boolean executeCL() {
        return getCLIJ2().countNonZeroVoxelsLocally((ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asInteger(args[2]), asInteger(args[3]), asInteger(args[4]));
    }

    @Deprecated
    public static boolean countNonZeroVoxelsLocally(CLIJ2 clij2, ClearCLBuffer src, ClearCLBuffer dst, Integer radiusX, Integer radiusY, Integer radiusZ) {
        return countNonZeroVoxels3DSphere(clij2, src, dst, radiusX, radiusY, radiusZ);
    }

    public static boolean countNonZeroVoxels3DSphere(CLIJ2 clij2, ClearCLBuffer src, ClearCLBuffer dst, Integer radiusX, Integer radiusY, Integer radiusZ) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("Nx", radiusToKernelSize(radiusX));
        parameters.put("Ny", radiusToKernelSize(radiusY));
        parameters.put("Nz", radiusToKernelSize(radiusZ));
        parameters.put("src", src);
        parameters.put("dst", dst);
        clij2.execute(CountNonZeroVoxels3DSphere.class, "count_nonzero_voxels_sphere_3d_x.cl", "count_nonzero_voxels_sphere_3d", dst.getDimensions(), dst.getDimensions(), parameters);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Image destination, Number radiusX, Number radiusY, Number radiusZ";
    }

    @Override
    public String getDescription() {
        return "Counts non-zero voxels in a sphere around every voxel. \n\n" +
                "Put the number in the result image.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }
}
