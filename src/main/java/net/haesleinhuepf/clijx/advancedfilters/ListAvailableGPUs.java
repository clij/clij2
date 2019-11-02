package net.haesleinhuepf.clijx.advancedfilters;

import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.clearcl.ClearCL;
import net.haesleinhuepf.clij.clearcl.ClearCLDevice;
import net.haesleinhuepf.clij.clearcl.backend.ClearCLBackendInterface;
import net.haesleinhuepf.clij.clearcl.backend.ClearCLBackends;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 *         September 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_listAvailableGPUs")
public class ListAvailableGPUs extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {

        ResultsTable table = ResultsTable.getResultsTable();
        ClearCLBackendInterface lClearCLBackend = ClearCLBackends.getBestBackend();
        ClearCL lClearCL = new ClearCL(lClearCLBackend);
        for (ClearCLDevice lDevice : lClearCL.getAllDevices()) {
            table.incrementCounter();
            table.addValue("GPUName", lDevice.getName());
            table.addValue("Max_memory_in_bytes", lDevice.getMaxMemoryAllocationSizeInBytes());
            table.addValue("OpenCL_version", lDevice.getVersion());
            table.addValue("Clock_frequency", lDevice.getClockFrequency());
            table.addValue("Number_of_compute_units", lDevice.getNumberOfComputeUnits());
            table.addValue("Global_memory_in_bytes", lDevice.getGlobalMemorySizeInBytes());
            table.addValue("Local_memory_in_bytes", lDevice.getLocalMemorySizeInBytes());
            table.addValue("Max_workgroup_size", lDevice.getMaxWorkGroupSize());
            table.addValue("Extensions", lDevice.getExtensions());
            table.addValue("Info", lDevice.getInfoString());
            table.show("Results");
        }
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
