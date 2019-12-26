package net.haesleinhuepf.clijx.codegenerator;

import ij.gui.Roi;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPluginService;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import org.scijava.Context;

import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;

public class OpGenerator {
    public static void main(String ... args) throws IOException {
        CLIJMacroPluginService service = new Context(CLIJMacroPluginService.class).getService(CLIJMacroPluginService.class);

        StringBuilder builder = new StringBuilder();
        builder.append("package net.haesleinhuepf.clijx.utilities;\n");
        builder.append("import net.haesleinhuepf.clij.CLIJ;\n");
        builder.append("import net.haesleinhuepf.clijx.CLIJx;\n");
        builder.append("import net.haesleinhuepf.clij.clearcl.ClearCLKernel;\n");
        builder.append("import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;\n");
        builder.append("import net.haesleinhuepf.clij.clearcl.ClearCLImage;\n");
        builder.append("import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;\n");
        builder.append("import ij.measure.ResultsTable;\n");
        builder.append("import ij.gui.Roi;\n");

        for (Class klass : CLIJ2Plugins.classes) {
            builder.append("import " + klass.getName() + ";\n");
        }

        builder.append("// this is generated code. See src/test/java/net/haesleinhuepf/clijx/codegenerator for details\n");
        builder.append("public abstract class CLIJxOps {\n");
        builder.append("   protected CLIJ clij;\n");
        builder.append("   protected CLIJx clijx;\n");
        builder.append("   \n");

        int methodCount = 0;
        for (Class klass : CLIJ2Plugins.classes) {
            builder.append("\n    // " + klass.getName() + "\n");
            builder.append("    //----------------------------------------------------\n");
            for (Method method : klass.getMethods()) {
                if (Modifier.isStatic(method.getModifiers()) &&
                        Modifier.isPublic(method.getModifiers()) &&
                        method.getParameterCount() > 0 &&
                        (method.getParameters()[0].getType() == CLIJ.class ||
                         method.getParameters()[0].getType() == CLIJx.class
                        ) && blockListOk(klass, method)) {

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
                        } else if (
                                parameter.getType() == net.imglib2.realtransform.AffineTransform2D.class ||
                                parameter.getType() == net.imglib2.realtransform.AffineTransform3D.class
                        ) {
                            parametersHeader = parametersHeader + parameter.getType().getName() + " " + parameter.getName();
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

                    String documentation = findDocumentation(service, methodName);
                    //System.out.println(documentation);

                    builder.append("    /**\n");
                    builder.append("     * " + documentation.replace("\n", "\n     * ") + "\n");
                    builder.append("     */\n");

                    builder.append("    public " + returnType + " " + methodName + "(");
                    builder.append(parametersHeader);
                    builder.append(") {\n");
                    builder.append("        return " + klass.getSimpleName() + "." + methodName + "(" + parametersCall + ");\n");
                    builder.append("    }\n\n");

                    methodCount++;
                }
            }
        }
        builder.append("}\n");
        builder.append("// " + methodCount + " methods generated.\n");

        File outputTarget = new File("src/main/java/net/haesleinhuepf/clijx/utilities/CLIJxOps.java");

        FileWriter writer = new FileWriter(outputTarget);
        writer.write(builder.toString());
        writer.close();

    }

    static boolean blockListOk(Class klass, Method method) {
        String searchString = klass.getSimpleName() + "." + method.getName();
        if (CLIJ2Plugins.blockList.contains(";" + searchString + ";")) {
            //System.out.println("BLOCKING " + searchString);
            return false;
        }
        return true;
    }

    public static String[] guessParameterNames(CLIJMacroPluginService service, String methodName, String[] parametersHeader) {
        CLIJMacroPlugin plugin = findPlugin(service, methodName);
        if (plugin != null) {
            String[] parameters = plugin.getParameterHelpText().split(",");
            if (parameters.length != parametersHeader.length) {
                return new String[0];
            }
            String[] parameterNames = new String[parametersHeader.length];
            for (int i = 0; i < parameters.length; i++) {
                String typeA = parameters[i].trim().split(" ")[0];
                String typeB = parametersHeader[i].trim().split(" ")[0];

                if (((typeA.compareTo("String") == 0 || typeA.compareTo("Image") == 0) && (typeB.compareTo("ClearCLBuffer") == 0 || typeB.compareTo("ClearCLImage") == 0)) ||
                        ((typeA.compareTo("Number") == 0) && (typeB.compareTo("double") == 0))) {
                    parameterNames[i] = parameters[i].trim().split(" ")[1];
                } else {
                    return new String[0];
                }

            }
            return parameterNames;
        }
        return new String[0];
    }

    static String typeToString(Class klass) {
        String result = "" + klass.getSimpleName();
        if (result.compareTo("[F") == 0) {
            return "float[]";
        }
        return result;
    }

    static CLIJMacroPlugin findPlugin(CLIJMacroPluginService service, String methodName) {
        String[] potentialMethodNames = {
                "CLIJ_" + methodName,
                "CLIJ_" + methodName + "2D",
                "CLIJ_" + methodName + "3D",
                "CLIJ_" + methodName + "Images",
                "CLIJ_" + methodName.replace( "Sphere", "2DBox"),
                "CLIJ_" + methodName.replace( "Sphere", "3DBox"),
                "CLIJ_" + methodName.replace( "Box", "2DBox"),
                "CLIJ_" + methodName.replace( "Box", "3DBox"),
                "CLIJ_" + methodName.replace( "Pixels", "OfAllPixels"),
                "CLIJ_" + methodName.replace( "SliceBySlice", "3DSliceBySlice"),
                "CLIJx_" + methodName,
                "CLIJx_" + methodName + "2D",
                "CLIJx_" + methodName + "3D",
                "CLIJx_" + methodName + "Images",
                "CLIJx_" + methodName.replace( "Sphere", "2DBox"),
                "CLIJx_" + methodName.replace( "Sphere", "3DBox"),
                "CLIJx_" + methodName.replace( "Box", "2DBox"),
                "CLIJx_" + methodName.replace( "Box", "3DBox"),
                "CLIJx_" + methodName.replace( "Pixels", "OfAllPixels"),
                "CLIJx_" + methodName.replace( "SliceBySlice", "3DSliceBySlice")
        };

        for (String name : potentialMethodNames) {
            name = findName(service, name);
            CLIJMacroPlugin plugin = service.getCLIJMacroPlugin(name);
            if (plugin != null) {
                return plugin;
            }
        }
        return null;
    }

    static String findDocumentation(CLIJMacroPluginService service, String methodName) {
        if (methodName.endsWith("IJ")) {
            return "This method is deprecated. Consider using " + methodName.replace("IJ", "Box") + " or " + methodName.replace("IJ", "Sphere") + " instead.";
        }

        CLIJMacroPlugin plugin = findPlugin(service, methodName);

        if (plugin != null) {
            if (plugin instanceof OffersDocumentation) {
                return ((OffersDocumentation) plugin).getDescription();
            } else {
                return plugin.getParameterHelpText();
            }
        }

        System.out.println("No documentation found for " + methodName);
        return "";
    }

    protected static String findName(CLIJMacroPluginService service, String name) {
        for (String potentialName : service.getCLIJMethodNames()) {
            if (potentialName.toLowerCase().compareTo(name.toLowerCase()) == 0) {
                return potentialName;
            }
        }
        return name;
    }

}
