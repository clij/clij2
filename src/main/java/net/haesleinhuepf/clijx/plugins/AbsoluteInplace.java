package net.haesleinhuepf.clijx.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 *         November 2019
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_absoluteInplace")
public class AbsoluteInplace extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        return absoluteInplace(getCLIJx(), (ClearCLBuffer) args[0]);
    }

    public static boolean absoluteInplace(CLIJx clijx, ClearCLBuffer input_output) {
        ClearCLBuffer buffer = clijx.create(input_output);
        clijx.copy(input_output, buffer);

        boolean result = Absolute.absolute(clijx, buffer, input_output);
        clijx.release(buffer);
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
