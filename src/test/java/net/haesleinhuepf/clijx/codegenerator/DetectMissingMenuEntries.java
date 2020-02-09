package net.haesleinhuepf.clijx.codegenerator;

import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPluginService;
import org.scijava.Context;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Set;

public class DetectMissingMenuEntries {
    public static void main(String[] args) throws IOException {

        String plugins_config = new String(Files.readAllBytes(Paths.get("src/main/resources/plugins.config")));

        CLIJMacroPluginService service = new Context(CLIJMacroPluginService.class).getService(CLIJMacroPluginService.class);

        String[] methods = getMethods(service);
        Arrays.sort(methods);
        for (String macroMethodName : methods) {
            if (!macroMethodName.startsWith("CLIJ2")) {
                continue;
            }
            CLIJMacroPlugin plugin = service.getCLIJMacroPlugin(macroMethodName);
            String fullName = plugin.getClass().getCanonicalName();

            if (!plugins_config.contains(fullName)) {
                System.out.println(fullName);
            }
        }
    }

    private static String[] getMethods(CLIJMacroPluginService service){
        Set<String> methods = service.getCLIJMethodNames();
        String[] methodsWithParameters = new String[methods.size()];
        int count = 0;
        for (String method : methods) {
            CLIJMacroPlugin plugin = service.getCLIJMacroPlugin(method);
            //String parameters = plugin.getParameterHelpText();
            methodsWithParameters[count] = method; // + "(" + parameters + ")";
            count++;
        }
        return methodsWithParameters;
    }
}
