package net.haesleinhuepf.clij2.codegenerator;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPluginService;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.Context;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static net.haesleinhuepf.clij2.codegenerator.OpGenerator.*;

public class DocumentationGenerator {

    private static class DocumentationItem {
        Class klass;
        String methodName;
        String parametersJava;
        String parametersMacro;
        String description;
    }

    public static void main(String ... args) throws IOException {

        CLIJMacroPluginService service = new Context(CLIJMacroPluginService.class).getService(CLIJMacroPluginService.class);

        HashMap<String, DocumentationItem> methodMap = new HashMap<String, DocumentationItem>();

        int methodCount = 0;
        for(Class klass : CLIJ2Plugins.classes)
        {
            for (Method method : klass.getMethods()) {
                if (Modifier.isStatic(method.getModifiers()) &&
                        Modifier.isPublic(method.getModifiers()) &&
                        method.getParameterCount() > 0 &&
                        method.getParameters()[0].getType() == CLIJ.class) {



                    String methodName = method.getName();
                    String returnType = typeToString(method.getReturnType());
                    String parametersHeader = "";
                    String parametersCall = "";
                    for (Parameter parameter : method.getParameters()) {
                        if (parametersCall.length() == 0) { // first parameter
                            parametersCall = "clij";
                            continue;
                        }

                        if (parametersHeader.length() > 0) {
                            parametersHeader = parametersHeader + ", ";
                        }
                        parametersHeader = parametersHeader + parameter.getType().getSimpleName() + " " + parameter.getName();
                        parametersCall = parametersCall + ", " + parameter.getName();
                    }

                    CLIJMacroPlugin plugin = findPlugin(service, methodName);

                    DocumentationItem item = new DocumentationItem();

                    if (plugin != null) {
                        item.parametersMacro = plugin.getParameterHelpText();
                        if (plugin instanceof OffersDocumentation) {
                            item.description = ((OffersDocumentation) plugin).getDescription();
                        }
                    }

                    //System.out.println(documentation);

                    item.klass = klass;
                    item.methodName = methodName;
                    item.parametersJava = parametersHeader;

                    methodMap.put(methodName + "_" + methodCount, item);

                    methodCount++;
                }
            }
        }

        ArrayList<String> names = new ArrayList<String>();
        names.addAll(methodMap.keySet());
        Collections.sort(names);

        StringBuilder builder = new StringBuilder();
        builder.append("# CLIJ2 reference\n");
        builder.append("This reference contains all methods currently available in CLIJ2.\n\n");
        builder.append("__Please note:__ CLIJ2 is under heavy construction. This list may change at any point.");
        builder.append("Methods marked with ' were available in CLIJ1.\n\n");

        for (String sortedName : names) {
            DocumentationItem item = methodMap.get(sortedName);
            builder.append("<a name=\"" + item.methodName + "\"></a>\n");
            builder.append("## " + item.methodName);
            if (item.klass == Kernels.class) {
                builder.append("'");
            }
            builder.append("\n\n");
            builder.append(item.description);
            builder.append("\n\n");
            builder.append("Parameters (macro):\n");
            builder.append(item.parametersMacro);
            builder.append("\n\n");
            builder.append("Parameters (Java):\n");
            builder.append(item.parametersJava);
            builder.append("\n\n");

        }

        File outputTarget = new File("reference.md");

        FileWriter writer = new FileWriter(outputTarget);
            writer.write(builder.toString());
            writer.close();
    }
}
