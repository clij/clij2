package net.haesleinhuepf.clijx.advancedmath;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_power")
public class Power extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        if (containsCLImageArguments() && clij.hasImageSupport()) {
            return Kernels.power(clij, (ClearCLImage)( args[0]), (ClearCLImage)(args[1]), asFloat(args[2]));
        } else {
            Object[] args = openCLBufferArgs();
            boolean result = Kernels.power(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asFloat(args[2]));
            releaseBuffers(args);
            return result;
        }
    }

    public static boolean power(CLIJ clij, ClearCLImage src, ClearCLImage dst, Float exponent) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("exponent", exponent);
        return clij.execute(Kernels.class, "power_" + src.getDimension() + "d_x.cl", "power_" + src.getDimension() + "d", parameters);
    }


    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number exponent";
    }

    @Override
    public String getDescription() {
        return "Computes all pixels value x to the power of a given exponent a.\n\n<pre>f(x, a) = x ^ a</pre>";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
