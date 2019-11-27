package net.haesleinhuepf.clijx.codegenerator;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPluginService;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import org.scijava.Context;

import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static net.haesleinhuepf.clijx.codegenerator.OpGenerator.*;

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
                        (method.getParameters()[0].getType() == CLIJ.class ||
                         method.getParameters()[0].getType() == CLIJx.class) &&
                        OpGenerator.blockListOk(klass, method)) {



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

                    String[] variableNames = guessParameterNames(service, methodName, parametersHeader.split(","));
                    if (variableNames.length > 0) {
                        for (int i = 0; i < variableNames.length; i++) {
                            parametersCall = parametersCall.replace("arg" + (i + 1), variableNames[i]);
                            parametersHeader = parametersHeader.replace("arg" + (i + 1), variableNames[i]);
                        }
                    }

                    if (!parametersHeader.contains("ClearCLImage ")) { // we document only  buffer methods for now
                        CLIJMacroPlugin plugin = findPlugin(service, methodName);

                        DocumentationItem item = new DocumentationItem();

                        if (plugin != null) {
                            item.parametersMacro = plugin.getParameterHelpText();
                            if (plugin instanceof OffersDocumentation) {
                                item.description = ((OffersDocumentation) plugin).getDescription();
                                item.description = item.description.replace("deprecated", "<b>deprecated</b>");
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
        }

        ArrayList<String> names = new ArrayList<String>();
        names.addAll(methodMap.keySet());
        Collections.sort(names);

        StringBuilder builder = new StringBuilder();
        builder.append("# CLIJx reference\n");
        builder.append("This reference contains all methods currently available in CLIJx.\n\n");
        builder.append("__Please note:__ CLIJx is under heavy construction. This list may change at any point.");
        builder.append("Methods marked with ' were available in CLIJ1.\n\n");


        for (String sortedName : names) {
            DocumentationItem item = methodMap.get(sortedName);
            builder.append("* <a href=\"#" + item.methodName + "\">");
            builder.append(item.methodName);
            if (item.klass == Kernels.class) {
                builder.append("'");
            }
            builder.append("</a>\n");

        }

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

            String linkToExamples = searchForExampleScripts("CLIJx_" + item.methodName, "src/main/macro/", "https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/");
            if(linkToExamples.length() > 0) {
                builder.append("\n\n### Example scripts\n" + linkToExamples + "\n\n");
            }
        }

        File outputTarget = new File("reference.md");

        FileWriter writer = new FileWriter(outputTarget);
            writer.write(builder.toString());
            writer.close();

        // auto-completion list
        buildAutoCompletion(names, methodMap);
    }

    private static void buildAutoCompletion(ArrayList<String> names, HashMap<String, DocumentationItem> methodMap) {

        StringBuilder builder = new StringBuilder();
        builder.append("package net.haesleinhuepf.clijx.jython;\n");
        builder.append("import org.fife.ui.autocomplete.BasicCompletion;\n");
        builder.append("import net.haesleinhuepf.clijx.jython.ScriptingAutoCompleteProvider;\n");
        builder.append("import java.util.ArrayList;");

        builder.append("// this is generated code. See src/test/java/net/haesleinhuepf/clijx/codegenerator for details\n");
        builder.append("class CLIJxAutoComplete {\n");
        builder.append("   \n");
        builder.append("   public static ArrayList<BasicCompletion> getCompletions(final ScriptingAutoCompleteProvider provider) {\n");

        builder.append("       ArrayList<BasicCompletion> list = new ArrayList<BasicCompletion>();\n");
        builder.append("       String headline;\n");
        builder.append("       String description;\n");

        int methodCount = 0;
        for (String name : names) {
            DocumentationItem item = methodMap.get(name);

            String htmlDescription = item.description;
            if (htmlDescription != null) {
                htmlDescription = htmlDescription.replace("\n", "<br>");
                htmlDescription = htmlDescription.replace("\"", "&quot;");
                htmlDescription = htmlDescription + "<br><br>Parameters:<br>" + item.parametersJava;
            }
            builder.append("       headline = \"clijx." + item.methodName + "(" + item.parametersJava + ")\";\n");
            builder.append("       description = \"<b>" + item.methodName + "</b><br><br>" + htmlDescription + "\";\n");
            builder.append("       list.add(new BasicCompletion(provider, headline, null, description));\n");

            methodCount++;
        }

        builder.append("        return list;\n");
        builder.append("    }\n");
        builder.append("}\n");
        builder.append("// " + methodCount + " methods generated.\n");

        File outputTarget = new File("src/main/java/net/haesleinhuepf/clijx/jython/CLIJxAutoComplete.java");

        try {
            FileWriter writer = new FileWriter(outputTarget);
            writer.write(builder.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static String searchForExampleScripts(String searchFor, String searchinFolder, String baseLink) {
        StringBuilder result = new StringBuilder();
        for (File file : new File(searchinFolder).listFiles()) {
            if (!file.isDirectory()) {
                String content = readFile(file.getAbsolutePath());
                if (content.contains(searchFor)) {
                    result.append("* [" + file.getName() + "](" + baseLink + file.getName() + ")\n");
                }
            }
        }
        return result.toString();
    }

    public static String readFile(String filename) {
        System.out.println("Reading " + filename);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            br.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
