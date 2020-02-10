package net.haesleinhuepf.clijx.codegenerator;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.CLIJMacroPluginService;
import net.haesleinhuepf.clijx.CLIJx;
import org.scijava.Context;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;

import static net.haesleinhuepf.clijx.codegenerator.OpGenerator.findDocumentation;
import static net.haesleinhuepf.clijx.codegenerator.OpGenerator.guessParameterNames;
import static net.haesleinhuepf.clijx.codegenerator.OpGenerator.typeToString;

/**
 * GenerateCLIJ1Wrappers
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 10 2019
 */
public class GenerateCLIJ1Wrappers {

    private static Class[] klasses = { Kernels.class};
    public static void main(String... args) {
        CLIJMacroPluginService service = new Context(CLIJMacroPluginService.class).getService(CLIJMacroPluginService.class);

        for (Class klass : klasses) {
            for (Method method : klass.getMethods()) {
                if (Modifier.isStatic(method.getModifiers()) &&
                        Modifier.isPublic(method.getModifiers()) &&
                        method.getParameterCount() > 0 &&
                        (method.getParameters()[0].getType() == CLIJ.class ||
                                method.getParameters()[0].getType() == CLIJx.class
                        )) {

                    String methodName = method.getName();
                    String returnType = typeToString(method.getReturnType());
                    String parametersHeader = "";
                    String parametersCall = "";
                    for (Parameter parameter : method.getParameters()) {
                        if (parametersCall.length() == 0) { // first parameter
                            if (method.getParameters()[0].getType() == CLIJ.class) {
                                parametersCall = "clij";
                            } else {
                                parametersCall = "clijx";
                            }
                            continue;
                        }

                        if (parametersHeader.length() > 0) {
                            parametersHeader = parametersHeader + ", ";
                        }
                        if (parameter.getType() == Float.class) {
                            parametersHeader = parametersHeader + "double " + parameter.getName();
                            parametersCall = parametersCall + ", new Double (" + parameter.getName() + ").floatValue()";
                        } else if (parameter.getType() == Integer.class){
                            parametersHeader = parametersHeader + "double " + parameter.getName();
                            parametersCall = parametersCall + ", new Double (" + parameter.getName() + ").intValue()";
                        } else if (parameter.getType() == Boolean.class){
                            parametersHeader = parametersHeader + "boolean " + parameter.getName();
                            parametersCall = parametersCall + ", " + parameter.getName();
                        } else {
                            parametersHeader = parametersHeader + parameter.getType().getSimpleName() + " " + parameter.getName();
                            parametersCall = parametersCall + ", " + parameter.getName();
                        }
                    }

                    String[] variableNames = guessParameterNames(service, methodName, parametersHeader.split(","));
                    if (variableNames.length > 0) {
                        for (int i = 0; i < variableNames.length; i++) {
                            parametersCall = parametersCall.replace("arg" + (i + 1), variableNames[i]);
                            parametersHeader = parametersHeader.replace("arg" + (i + 1), variableNames[i]);
                        }
                    }

                    String documentation = findDocumentation(service, methodName, false);

                    generateWrapper(methodName, parametersHeader, parametersCall, returnType, documentation);
                }
            }
        }
    }

    private static void generateWrapper(String methodName, String parametersHeader, String parametersCall, String returnType, String documentation) {

        String klassName = "CLIJ1" + methodName.substring(0,1).toUpperCase() + methodName.substring(1);

        StringBuilder builder = new StringBuilder();
        builder.append("package net.haesleinhuepf.clijx.clij1wrappers;\n");
        builder.append("import net.haesleinhuepf.clij.CLIJ;\n");
        builder.append("import net.haesleinhuepf.clij.clearcl.ClearCLKernel;\n");
        builder.append("import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;\n");
        builder.append("import net.haesleinhuepf.clij.clearcl.ClearCLImage;\n");
        builder.append("import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;\n");
        builder.append("import net.imglib2.realtransform.AffineTransform2D;\n");
        builder.append("import net.imglib2.realtransform.AffineTransform3D;\n");

        builder.append("import " + klasses[0].getName() + ";\n");

        builder.append("// this is generated code. See src/test/java/net/haesleinhuepf/clijx/codegenerator for details\n");
        builder.append("public class " + klassName + "{\n");
        builder.append("   \n");

        builder.append("    /**\n");
        builder.append("     * " + documentation.replace("\n", "\n     * ") + "\n");
        builder.append("     */\n");

        builder.append("    public " + returnType + " " + methodName + "(");
        builder.append("CLIJ clij, " + parametersHeader);
        builder.append(") {\n");
        builder.append("        return Kernels." + methodName + "(" + parametersCall + ");\n");
        builder.append("    }\n\n");

        builder.append("}\n");

        File outputTarget = new File("src/main/java/net/haesleinhuepf/clij/clij1wrappers/" + klassName + ".java");

        try {
            FileWriter writer = new FileWriter(outputTarget);
            writer.write(builder.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
