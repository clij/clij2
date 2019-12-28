package net.haesleinhuepf.clijx.temp;

import ij.ImagePlus;
import ij.gui.NewImage;
import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPluginService;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij.test.TestUtilities;
import net.haesleinhuepf.clijx.CLIJx;
import org.scijava.Context;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class ListNotTranslatedCLIJPugins {

    public static void main(String ... arg) throws IOException {
        CLIJ clij = CLIJ.getInstance();
        CLIJx clijx = CLIJx.getInstance();

        System.out.println("CLIJ: " + clij.getGPUName() + " " + clij.getClearCLContext().toString());
        System.out.println("CLIJx: " + clijx.getGPUName() + " " + clijx.getClij().getClearCLContext().toString());

        CLIJMacroPluginService service = new Context(CLIJMacroPluginService.class).getService(CLIJMacroPluginService.class);
        for (String pluginName : service.getCLIJMethodNames()) {
            if (pluginName.startsWith("CLIJ_")) {
                CLIJMacroPlugin clijPlugin = service.getCLIJMacroPlugin(pluginName);
                CLIJMacroPlugin clijxPlugin = service.getCLIJMacroPlugin(pluginName.replace("CLIJ_", "CLIJx_"));
                if (clijxPlugin == null) {
                    System.out.println("Error: No successor found for " + pluginName);
                }
            }
        }
    }
}
