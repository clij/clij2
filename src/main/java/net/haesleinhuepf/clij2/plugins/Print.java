package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 *         March 2020 in Bordeaux
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_print")
public class Print extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {

        return getCLIJ2().print((ClearCLBuffer) args[0]);
    }

    public static boolean print(CLIJ2 clij2, ClearCLImageInterface input) {
        System.out.println(PullString.pullString(clij2, input));
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image input";
    }

    @Override
    public String getDescription() {
        return "Visualises an image on standard out (console).";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
