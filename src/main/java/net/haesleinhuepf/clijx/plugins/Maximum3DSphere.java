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
import static net.haesleinhuepf.clij.utilities.CLIJUtilities.radiusToKernelSize;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_maximum3DSphere")
public class Maximum3DSphere extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        int radiusX = asInteger(args[2]);
        int radiusY = asInteger(args[3]);
        int radiusZ = asInteger(args[4]);

        return maximum3DSphere(getCLIJx(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), radiusX, radiusY, radiusZ);
    }


    public static boolean maximum3DSphere(CLIJx clijx, ClearCLImageInterface src, ClearCLImageInterface dst, Integer radiusX, Integer radiusY, Integer radiusZ) {
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

        clijx.execute(Maximum3DSphere.class, "maximum_sphere_3d_x.cl", "maximum_sphere_3d", dst.getDimensions(), dst.getDimensions(), parameters);
        return true;
    }


    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number radiusX, Number radiusY, Number radiusZ";
    }

    @Override
    public String getDescription() {
        return "Computes the local maximum of a pixels spherical neighborhood. The spheres size is specified by \n" +
                "its half-width, half-height and half-depth (radius).";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }


}
