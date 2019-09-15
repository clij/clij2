package net.haesleinhuepf.clij.advancedfilters;

import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 *         September 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_getGPUProperties")
public class GetGPUProperties extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {

        ResultsTable table = ResultsTable.getResultsTable();
        table.incrementCounter();
        table.addValue("GPUName", clij.getGPUName());
        table.addValue("Max_memory_in_bytes", clij.getGPUMemoryInBytes());
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
                " results table in the columns 'GPUName', 'Memory_in_bytes' and 'OpenCL_Version'.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "";
    }

}
