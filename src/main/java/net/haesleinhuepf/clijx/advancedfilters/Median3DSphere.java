package net.haesleinhuepf.clijx.advancedfilters;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij.utilities.CLKernelExecutor;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;
import static net.haesleinhuepf.clij.utilities.CLIJUtilities.radiusToKernelSize;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_median3DSphere")
public class Median3DSphere extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        return median3DSphere(getCLIJx(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asInteger(args[2]), asInteger(args[3]), asInteger(args[4]));
    }

    public static boolean median3DSphere(CLIJx clijx, ClearCLImageInterface src, ClearCLImageInterface dst, Integer radiusX, Integer radiusY, Integer radiusZ) {
        assertDifferent(src, dst);

        int kernelSizeX = radiusToKernelSize(radiusX);
        int kernelSizeY = radiusToKernelSize(radiusY);
        int kernelSizeZ = radiusToKernelSize(radiusZ);

        if (kernelSizeX * kernelSizeY * kernelSizeZ > CLKernelExecutor.MAX_ARRAY_SIZE) {
            throw new IllegalArgumentException("Error: kernels of the medianSphere filter is too big. Consider increasing MAX_ARRAY_SIZE.");
        }
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);
        parameters.put("Nz", kernelSizeZ);

        clijx.execute(Median3DSphere.class, "median_sphere_3d_x.cl", "median_sphere_3d", dst.getDimensions(), dst.getDimensions(), parameters);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number radiusX, Number radiusY, Number radiusZ";
    }

    @Override
    public String getDescription() {
        return "Computes the local median of a pixels spherical neighborhood. The spheres size is specified by \n" +
                "its half-width, half-height and half-depth (radius).\n\n" +
                "For technical reasons, the volume of the sphere must contain less than 1000 voxels.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }

}
