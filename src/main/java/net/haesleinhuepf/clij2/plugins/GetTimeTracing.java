package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 *         April 2020
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_getTimeTracing")
public class GetTimeTracing extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        ((String[])args[0])[0] = getCLIJ2().getTimeTraces();
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "ByRef String time_traces";
    }

    @Override
    public String getDescription() {
        return "Writes the recent recording of execution times for all operations in the variable time_traces.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
