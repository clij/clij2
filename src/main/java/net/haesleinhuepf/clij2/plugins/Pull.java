package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJHandler;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import org.scijava.plugin.Plugin;

/**
 * Release
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_pull")
public class Pull extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        CLIJHandler.getInstance().pullFromGPU((String)args[0]);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "String image";
    }

    @Override
    public String getDescription() {
        return "Copies an image specified by its name from GPU memory back to ImageJ and shows it.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
