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
import static net.haesleinhuepf.clij2.utilities.CLIJUtilities.checkDimensions;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_erodeSphere")
public class ErodeSphere extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        return erodeSphere(getCLIJ2(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]));
    }

    public static boolean erodeSphere(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match!");
        }

        clij2.execute(ErodeSphere.class, "erode_sphere_" + src.getDimension() + "d_x.cl", "erode_sphere_" + src.getDimension() + "d", src.getDimensions(), src.getDimensions(), parameters);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Image destination";
    }


    @Override
    public String getDescription() {
        return "Computes a binary image with pixel values 0 and 1 containing the binary erosion of a given input image. \n\n" +
                "The erosion takes the von-Neumann-neighborhood (4 pixels in 2D and 6 pixels in 3d) into account.\n" +
                "The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
