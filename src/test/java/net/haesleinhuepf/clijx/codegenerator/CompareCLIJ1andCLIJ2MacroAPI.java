package net.haesleinhuepf.clijx.codegenerator;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.CLIJMacroPluginService;
import net.haesleinhuepf.clij2.CLIJ2Ops;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.CLIJxOps;
import org.scijava.Context;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static net.haesleinhuepf.clijx.codegenerator.OpGenerator.blockListOk;

public class CompareCLIJ1andCLIJ2MacroAPI {
    public static void main(String[] args) {
        CLIJMacroPluginService service = new Context(CLIJMacroPluginService.class).getService(CLIJMacroPluginService.class);

        for (String clij1macroMethodName : service.getCLIJMethodNames()) {
            if (clij1macroMethodName.startsWith("CLIJ_")) {
                boolean methodFound = false;
                for (String clij2macroMethodName : service.getCLIJMethodNames()) {
                    if (clij2macroMethodName.startsWith("CLIJ2_")) {
                        if (clij1macroMethodName.compareTo(clij2macroMethodName.replace("CLIJ2_", "CLIJ_")) == 0) {
                            methodFound = true;
                            break;
                        }
                    }
                }
                if (!methodFound) {
                    System.out.println("Method not found in clij2 api: " + clij1macroMethodName);
                }
            }
        }

        for (String clij2macroMethodName : service.getCLIJMethodNames()) {
            if (clij2macroMethodName.startsWith("CLIJ2_")) {
                boolean methodFound = false;
                for (String clij1macroMethodName : service.getCLIJMethodNames()) {
                    if (clij1macroMethodName.startsWith("CLIJ_")) {
                        if (clij1macroMethodName.compareTo(clij2macroMethodName.replace("CLIJ2_", "CLIJ1_")) == 0) {
                            methodFound = true;
                            break;
                        }
                    }
                }
                if (!methodFound) {
                    System.out.println("Method not found in clij1 api: " + clij2macroMethodName);
                }
            }
        }
    }
}
