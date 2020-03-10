package net.haesleinhuepf.clij2.plugins;

import ij.IJ;
import net.haesleinhuepf.clij.macro.CLIJHandler;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPluginService;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import org.scijava.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_help")
public class Help extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        CLIJMacroPluginService pluginService = CLIJHandler.getInstance().getPluginService();
        String searchString = "";
        if (args.length > 0) {
            searchString = (String) (args[0]);
        }
        searchString = searchString.toLowerCase();
        ArrayList<String> helpList = new ArrayList<String>();

        for (String name : pluginService.getCLIJMethodNames()) {
            if (searchString.length() == 0 || name.toLowerCase().contains(searchString)) {

                helpList.add(name + "(" + pluginService.getCLIJMacroPlugin(name).getParameterHelpText() + ")");
                //IJ.log(key + "(" + methodMap.get(key).parameters + ")");
            }
        }

        IJ.log("Found " + helpList.size() + " method(s) containing the pattern \"" + searchString + "\":");
        Collections.sort(helpList);
        for (String entry : helpList) {
            IJ.log("Ext." + entry + ";");
        }
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "String searchFor";
    }

    @Override
    public String getDescription() {
        return "Searches in the list of CLIJ commands for a given pattern. Lists all commands in case\"\" is handed\n" +
                "over as parameter.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "-";
    }
}
