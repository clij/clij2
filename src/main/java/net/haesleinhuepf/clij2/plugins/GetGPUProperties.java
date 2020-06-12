package net.haesleinhuepf.clij2.plugins;

import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 *         September 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_getGPUProperties")
public class GetGPUProperties extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized {
    @Override
    public String getCategories() {
        return "Measurements";
    }

    @Override
    public boolean executeCL() {
        ((String[])args[0])[0] = clij.getGPUName();
        ((Double[])args[1])[0] = (double)clij.getGPUMemoryInBytes();
        ((Double[])args[2])[0] = clij.getOpenCLVersion();
        return true;
    }


    @Override
    public String getParameterHelpText() {
        return "ByRef String GPU_name, ByRef Number global_memory_in_bytes, ByRef Number OpenCL_version";
    }

    @Override
    public String getDescription() {
        return "Reads out properties of the currently active GPU and write it in the variables 'GPU_name', \n" +
                "'global_memory_in_bytes' and 'OpenCL_Version'.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "";
    }

}
