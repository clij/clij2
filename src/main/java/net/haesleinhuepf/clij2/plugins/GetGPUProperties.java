package net.haesleinhuepf.clij2.plugins;

import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 *         September 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_getGPUProperties")
public class GetGPUProperties extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {

        ResultsTable table = ResultsTable.getResultsTable();
        table.incrementCounter();
        table.addValue("GPUName", clij.getGPUName());
        table.addValue("Global_memory_in_bytes", clij.getGPUMemoryInBytes());
        table.addValue("OpenCL_version", clij.getOpenCLVersion());
        table.show("Results");
        return true;
    }


    @Override
    public String getParameterHelpText() {
        return "";
    }

    @Override
    public String getDescription() {
        return "Reads out properties of the currently active GPU writes it to the" +
                " results table in the columns 'GPUName', 'Global_memory_in_bytes' and 'OpenCL_Version'.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "";
    }

}
