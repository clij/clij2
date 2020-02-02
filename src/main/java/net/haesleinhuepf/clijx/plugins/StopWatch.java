package net.haesleinhuepf.clijx.plugins;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_stopWatch")
public class StopWatch extends AbstractCLIJPlugin implements CLIJOpenCLProcessor, OffersDocumentation {
    private static long timestamp = 0;

    @Override
    public String getDescription() {
        return "Measures time and outputs delay to last call.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "";
    }

    public static boolean stopWatch(CLIJ clij, String text)  {
        if (text.length() > 0) {
            System.out.println("" + (System.currentTimeMillis() - timestamp) + " msec for " + text);
        }
        timestamp = System.currentTimeMillis();
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "String text";
    }

    @Override
    public boolean executeCL() {
        stopWatch(clij, (String) args[0]);
        return true;
    }
}
