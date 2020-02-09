package net.haesleinhuepf.clijx.codegenerator;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPluginService;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2Ops;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.CLIJxOps;
import org.scijava.Context;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Set;

import static net.haesleinhuepf.clijx.codegenerator.OpGenerator.blockListOk;

public class CompareCLIJ1andCLIJ2MacroAPI {
    public static void main(String[] args) {
        StringBuilder methodListing = new StringBuilder();

        CLIJMacroPluginService service = new Context(CLIJMacroPluginService.class).getService(CLIJMacroPluginService.class);

        String[] allMethods = getMethods(service);
        Arrays.sort(allMethods);

        methodListing.append("## CLIJ 1/2/x API comparison\n");

        {
            methodListing.append("# CLIJ1 methods not part of CLIJ2\n");
            int count = 0;
            for (String clij1macroMethodName : allMethods) {
                if (clij1macroMethodName.startsWith("CLIJ_")) {
                    boolean methodFound = false;
                    for (String clij2macroMethodName : allMethods) {
                        if (clij2macroMethodName.startsWith("CLIJ2_")) {
                            if (clij1macroMethodName.compareTo(clij2macroMethodName.replace("CLIJ2_", "CLIJ_")) == 0) {
                                methodFound = true;
                                break;
                            }
                        }
                    }
                    if (!methodFound) {
                        System.out.println("clij1 method not found in clij2 api: " + clij1macroMethodName);
                        methodListing.append("* " + clij1macroMethodName + "\n");
                        count++;
                    }
                }
            }
            methodListing.append("\n" + count + " methods listed.\n");
        }

        {
            methodListing.append("# CLIJ2 methods not part of CLIJ1\n");
            int count = 0;
            for (String clij2macroMethodName : allMethods) {
                if (clij2macroMethodName.startsWith("CLIJ2_")) {
                    boolean methodFound = false;
                    for (String clij1macroMethodName : allMethods) {
                        if (clij1macroMethodName.startsWith("CLIJ_")) {
                            if (clij1macroMethodName.compareTo(clij2macroMethodName.replace("CLIJ2_", "CLIJ_")) == 0) {
                                methodFound = true;
                                break;
                            }
                        }
                    }
                    if (!methodFound) {
                        System.out.println("clij2 method not found in clij1 api: " + clij2macroMethodName);
                        methodListing.append("* " + clij2macroMethodName + "\n");
                        count++;
                    }
                }
            }
            methodListing.append("\n" + count + " methods listed.\n");
        }

        {
            methodListing.append("# CLIJx methods not part of CLIJ2\n");
            int count = 0;
            for (String clijxmacroMethodName : allMethods) {
                if (clijxmacroMethodName.startsWith("CLIJx_")) {
                    boolean methodFound = false;
                    for (String clij1macroMethodName : allMethods) {
                        if (clij1macroMethodName.startsWith("CLIJ2_")) {
                            if (clij1macroMethodName.compareTo(clijxmacroMethodName.replace("CLIJx_", "CLIJ2_")) == 0) {
                                methodFound = true;
                                break;
                            }
                        }
                    }
                    if (!methodFound) {
                        System.out.println("clijx method not found in clij2 api: " + clijxmacroMethodName);
                        methodListing.append("* " + clijxmacroMethodName + "\n");
                        count++;
                    }
                }
            }
            methodListing.append("\n" + count + " methods listed.\n");
        }
        String outputTarget = "clij12xAPIcomparison.md";
        try {
            FileWriter writer = new FileWriter(outputTarget);
            writer.write(methodListing.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String[] getMethods(CLIJMacroPluginService service){
        Set<String> methods = service.getCLIJMethodNames();
        String[] methodsWithParameters = new String[methods.size()];
        int count = 0;
        for (String method : methods) {
            CLIJMacroPlugin plugin = service.getCLIJMacroPlugin(method);
            String parameters = plugin.getParameterHelpText();
            methodsWithParameters[count] = method + "(" + parameters + ")";
            count++;
        }
        return methodsWithParameters;
    }
}
