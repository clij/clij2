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
import static net.haesleinhuepf.clij.utilities.CLIJUtilities.radiusToKernelSize;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_minimum3DSphere")
public class Minimum3DSphere extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        int radiusX = asInteger(args[2]);
        int radiusY = asInteger(args[3]);
        int radiusZ = asInteger(args[4]);

        return minimum3DSphere(getCLIJ2(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), radiusX, radiusY, radiusZ);
    }

    public static boolean minimum3DSphere(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst, Integer radiusX, Integer radiusY, Integer radiusZ) {
        int kernelSizeX = radiusToKernelSize(radiusX);
        int kernelSizeY = radiusToKernelSize(radiusY);
        int kernelSizeZ = radiusToKernelSize(radiusZ);

        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);
        parameters.put("Nz", kernelSizeZ);

        clij2.execute(Minimum3DSphere.class, "minimum_sphere_3d_x.cl", "minimum_sphere_3d", dst.getDimensions(), dst.getDimensions(),  parameters);
        return true;
    }


    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Image destination, Number radiusX, Number radiusY, Number radiusZ";
    }

    @Override
    public String getDescription() {
        return "Computes the local minimum of a pixels spherical neighborhood. \n\nThe spheres size is specified by \n" +
                "its half-width, half-height and half-depth (radius).";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }

}
