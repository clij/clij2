package net.haesleinhuepf.clijx.temp;

import ij.IJ;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJHandler;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_reportMemory")
public class ReportMemory extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        IJ.log(CLIJHandler.getInstance().reportGPUMemory());
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "";
    }


    @Override
    public String getDescription() {
        return "Prints a list of all images cached in the GPU to ImageJs log window together with a sum of memory \n" +
                "consumption.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "-";
    }
}
