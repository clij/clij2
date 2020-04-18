package net.haesleinhuepf.clij2.plugins;

import ij.IJ;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_invalidateKernelCache")
public class InvalidateKernelCache extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        getCLIJ2().invalidateKernelCahe();
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "";
    }

    @Override
    public String getDescription() {
        return "Invalidates all cached OpenCL programs and kernels.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "";
    }
}
