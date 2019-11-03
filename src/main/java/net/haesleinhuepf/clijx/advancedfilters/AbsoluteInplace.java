package net.haesleinhuepf.clijx.advancedfilters;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 *         November 2019
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_absoluteInplace")
public class AbsoluteInplace extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        return absoluteInplace(clij, (ClearCLBuffer) args[0]);
    }

    public static boolean absoluteInplace(CLIJ clij, ClearCLBuffer input_output) {
        ClearCLBuffer buffer = clij.create(input_output);
        clij.op().copy(input_output, buffer);
        boolean result = Kernels.absolute(clij, buffer, input_output);
        buffer.close();
        return result;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination";
    }

    @Override
    public String getDescription() {
        return "Computes the absolute value of every individual pixel x in a given image.\n\n<pre>f(x) = |x| </pre>";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
