package net.haesleinhuepf.clijx.codegenerator;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.CLIJMacroPluginService;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.CLIJxOps;
import org.scijava.Context;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static net.haesleinhuepf.clijx.codegenerator.OpGenerator.blockListOk;

/**
 * APIInconsitencyDetector
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 12 2019
 */
public class APIInconsitencyDetector {
    public static void main(String[] args) {
        CLIJMacroPluginService service = new Context(CLIJMacroPluginService.class).getService(CLIJMacroPluginService.class);

        for (String macroMethodName : service.getCLIJMethodNames()) {
            boolean methodFound = false;

            for (Method method : CLIJxOps.class.getMethods()) {
                if (   Modifier.isPublic(method.getModifiers()) &&
                        method.getParameterCount() > 0 &&
                        blockListOk(Kernels.class, method)) {
                    String methodName = method.getName();

                    if (macroMethodName.compareTo("CLIJ_" + methodName) == 0 || macroMethodName.compareTo("CLIJx_" + methodName) == 0) {
                        methodFound = true;
                        break;
                    }
                }
            }
            if (!methodFound) {
                System.out.println("Method not found in macro api: " + macroMethodName);
            }

        }

        for (Method method : CLIJxOps.class.getMethods()) {
            if (Modifier.isStatic(method.getModifiers()) &&
                    Modifier.isPublic(method.getModifiers()) &&
                    method.getParameterCount() > 0 &&
                    (method.getParameters()[0].getType() == CLIJ.class ||
                            method.getParameters()[0].getType() == CLIJx.class
                    ) && blockListOk(Kernels.class, method)) {
                String methodName = method.getName();

                boolean methodFound = false;
                for (String macroMethodName : service.getCLIJMethodNames()) {
                    if (macroMethodName.compareTo("CLIJ_" + methodName) == 0 || macroMethodName.compareTo("CLIJx_" + methodName) == 0 ) {
                        methodFound = true;
                        break;
                    }
                }
                if (!methodFound) {
                    System.out.println("Method not found in clijx api: " + methodName);
                }
            }
        }
    }
}
