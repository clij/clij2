package net.haesleinhuepf.clijx.codegenerator;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPluginService;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clij2.utilities.HasAuthor;
import net.haesleinhuepf.clij2.utilities.HasLicense;
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

    private final static String HTTP_ROOT = "https://clij.github.io/clij-advanced-filters/";

    private static class DocumentationItem {
        public String parametersHeader;
        public String parametersCall;
        public String returnType;
        Class klass;
        String methodName;
        String parametersJava;
        String parametersMacro;
        String description;
        String author;
        String license;
    }

    private static boolean isCLIJ2;
    private static CLIJMacroPluginService service;

    public static void main(String ... args) throws IOException {

        service = new Context(CLIJMacroPluginService.class).getService(CLIJMacroPluginService.class);
        boolean[] booleans = new boolean[]{false, true};

        for (int b = 0; b < booleans.length; b++) {
            isCLIJ2 = booleans[b];
            HashMap<String, DocumentationItem> methodMap = new HashMap<String, DocumentationItem>();

            String processedNames = ";";

            int methodCount = 0;
            for (Class klass : CLIJxPlugins.classes) {
                for (Method method : klass.getMethods()) {
                    if (Modifier.isStatic(method.getModifiers()) &&
                            Modifier.isPublic(method.getModifiers()) &&
                            method.getParameterCount() > 0 &&
                            (method.getParameters()[0].getType() == CLIJ.class ||
                                    method.getParameters()[0].getType() == CLIJ2.class||
                                    method.getParameters()[0].getType() == CLIJx.class) &&
                            OpGenerator.blockListOk(klass, method) &&
                            !processedNames.contains(";" + method.getName() + ";")
                    ) {
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
                                if (plugin instanceof HasAuthor) {
                                    item.author = ((HasAuthor) plugin).getAuthorName();
                                }
                                if (plugin instanceof HasLicense) {
                                    item.license = ((HasLicense) plugin).getLicense();
                                }
                            }

                            //System.out.println(documentation);

                            item.klass = klass;
                            item.methodName = methodName;
                            item.parametersJava = parametersHeader;
                            item.parametersHeader = parametersHeader;
                            item.parametersCall = parametersCall;
                            item.returnType = returnType;

                            methodMap.put(methodName + "_" + methodCount, item);

                            methodCount++;
                            processedNames = processedNames + method.getName() + ";";
                        }
                    }
                }
            }

            ArrayList<String> names = new ArrayList<String>();
            names.addAll(methodMap.keySet());
            Collections.sort(names);


            // auto-completion list
            buildAutoCompletion(names, methodMap);
            buildReference(names, methodMap);
            buildIndiviualOperationReferences(names, methodMap);
        }
    }

    private static void buildIndiviualOperationReferences(ArrayList<String> names, HashMap<String, DocumentationItem> methodMap) throws IOException {

        for (String sortedName : names) {
            StringBuilder builder = new StringBuilder();
            DocumentationItem item = methodMap.get(sortedName);
            builder.append("## " + item.methodName + "\n");
            if (item.klass == Kernels.class) {
                builder.append("![Image](images/mini_clij1_logo.png)");
            }
            if (item.klass.getPackage().toString().contains("clij2") || item.klass.getPackage().toString().contains("clijx")) {
                // TODO: Check if it already existed in CLIJ1
                if (service.getCLIJMacroPlugin("CLIJ_" + item.methodName) != null) {
                    builder.append("![Image](images/mini_clij1_logo.png)");
                }
                builder.append("![Image](images/mini_clij2_logo.png)");
                builder.append("![Image](images/mini_clijx_logo.png)");
            }
            if (item.klass.getPackage().toString().contains("clijx")) {
                builder.append("![Image](images/mini_clijx_logo.png)");
            }
            builder.append("\n\n");
            if (item.author != null && item.author.length() > 0) {
                builder.append("By " + item.author + "\n\n");
            }
            builder.append(item.description);
            builder.append("\n\n");
            builder.append("### Usage in ImageJ macro\n");
            builder.append("```\n");
            builder.append("Ext.CLIJ" + (item.klass == Kernels.class?"":"x") + "_" + item.methodName + "(" + item.parametersMacro + ");\n");
            builder.append("```\n");
            builder.append("\n\n");
            builder.append("### Usage in Java\n");
            builder.append("```\n");
            builder.append(generateJavaExampleCode(item.methodName, item.parametersHeader, item.parametersCall, item.returnType));
            builder.append("```\n");
            builder.append("\n\n");

            String linkToExamples =
                searchForExampleScripts("CLIJx_" + item.methodName, "src/main/macro/", "https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/", "macro") +
                searchForExampleScripts("CLIJ2_" + item.methodName, "src/main/macro/", "https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/", "macro") +
                //searchForExampleScripts("CLIJ_" + item.methodName, "src/main/macro/", "https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/", "macro") +
                //searchForExampleScripts("CLIJ_" + item.methodName, "../clij-docs/src/main/macro/", "https://github.com/clij/clij-docs/blob/master/src/main/macro/", "macro") +
                searchForExampleScripts("clijx." + item.methodName, "src/main/jython/", "https://github.com/clij/clij-advanced-filters/blob/master/src/main/jython/", "jython") +

                //searchForExampleScripts("clij.op()." + item.methodName, "../clij-docs/src/main/groovy/", "https://github.com/clij/clij-docs/blob/master/src/main/groovy/", "groovy") +
                //searchForExampleScripts("clij.op()." + item.methodName, "../clij-docs/src/main/jython/", "https://github.com/clij/clij-docs/blob/master/src/main/jython/", "jython") +
                searchForExampleScripts("clijx." + item.methodName, "../clijpy/python/", "https://github.com/clij/clijpy/blob/master/python/", "python") +
                searchForExampleScripts("clij.op()." + item.methodName, "../clij-docs/src/main/java/net/haesleinhuepf/clij/examples/", "https://github.com/clij/clij-docs/blob/master/src/main/java/net/haesleinhuepf/clij/examples/", "java") +

                searchForExampleScripts("clij.op()." + item.methodName, "../clij-docs/src/main/javascript/", "https://github.com/clij/clij-docs/blob/master/src/main/javascript/", "javascript") +
                searchForExampleScripts("clij.op()." + item.methodName, "../clij-docs/src/main/beanshell/", "https://github.com/clij/clij-docs/blob/master/src/main/beanshell/", "beanshell") +
                searchForExampleScripts("clijx." + item.methodName, "../clatlab/src/main/matlab/", "https://github.com/clij/clatlab/blob/master/src/main/matlab/", "matlab");





            if(linkToExamples.length() > 0) {
                builder.append("\n\n### Example scripts\n" + linkToExamples + "\n\n");
            }

            if (item.license != null && item.license.length() > 0) {
                builder.append("\n\n### License terms\n");
                builder.append(item.license.replace("\n", "  \n") + "\n\n");
            }


            builder.append("[Back to CLIJ documentation](https://clij.github.io/)\n" +
                    "\n" +
                    "[Imprint](https://clij.github.io/imprint)\n");

            File outputTarget = new File("reference_" + item.methodName + ".md");
            FileWriter writer = new FileWriter(outputTarget);
            writer.write(builder.toString());
            writer.close();
        }
    }

    private static String generateJavaExampleCode(String methodName, String parametersWithType, String parameters, String returnType) {

        // just some example numbers for example code
        float[] floatParameterValues = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17};
        int[] integerParameterValues = {10,20,30,40,50,60,70,80,90,100,110,120,130,140,150,160,170};
        boolean[] booleanParameterValues = {true, false, false, true};
        int floatParameterIndex = 0;
        int integerParameterIndex = 0;
        int booleanParameterIndex = 0;

        StringBuilder code = new StringBuilder();

        code.append("// init CLIJ and GPU\n");

        String clijObjectName;

        if (isCLIJ2) {
            code.append("import net.haesleinhuepf.clij2.CLIJ;\n");
            code.append("import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;\n");
            code.append("CLIJ2 clij2 = CLIJ2.getInstance();\n\n");

            clijObjectName = "clij2";
        } else {
            code.append("import net.haesleinhuepf.clijx.CLIJ;\n");
            code.append("import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;\n");
            code.append("CLIJx clijx = CLIJx.getInstance();\n\n");

            clijObjectName = "clijx";
        }
        code.append("// get input parameters\n");
        String[] parametersArray = parametersWithType.split(",");
        String inputImage = "";
        for (String parameter : parametersArray) {
            parameter = parameter.trim();
            String parameterName = parameter.split(" ")[1];
            if (isInputParameter(parameter)) {
                if (inputImage.length() == 0) {
                    inputImage = parameterName;
                }
                code.append("ClearCLBuffer " + parameterName + " = " + clijObjectName + ".push(" + parameterName + "ImagePlus);\n");
            } else if (isOutputParameter(parameter)) {
                code.append(createOutputImageCode(methodName, parameterName, inputImage));
            } else if (parameter.startsWith("Float")) {
                code.append("float " + parameterName + " = " + floatParameterValues[floatParameterIndex]+ ";\n");
                floatParameterIndex++;
            } else if (parameter.startsWith("Integer")) {
                code.append("int " + parameterName + " = " + integerParameterValues[integerParameterIndex] + ";\n");
                integerParameterIndex++;
            } else if (parameter.startsWith("Boolean")) {
                code.append("boolean " + parameterName + " = " + booleanParameterValues[booleanParameterIndex] + ";\n");
                booleanParameterIndex++;
            } else if (parameter.startsWith("AffineTransform3D")) {
                code.append("import net.imglib2.realtransform.AffineTransform3D;\n");
                code.append("at = new AffineTransform3D();\n" +
                        "at.translate(4, 0, 0);\n");
            } else if (parameter.startsWith("AffineTransform2D")) {
                code.append("import net.imglib2.realtransform.AffineTransform2D;\n");
                code.append("at = new AffineTransform2D();\n" +
                        "at.translate(4, 0);\n");
            }
        }


        code.append("```\n\n```");
        code.append("\n// Execute operation on GPU\n");
        if (returnType.toLowerCase().compareTo("boolean") != 0) {
            code.append(returnType + " result" + methodName.substring(0,1).toUpperCase() + methodName.substring(1, methodName.length()) + " = ");
        }
        code.append(clijObjectName + "." + methodName + "(" + parameters + ");\n");
        code.append("```\n\n```");

        code.append("\n//show result\n");
        if (returnType.toLowerCase().compareTo("boolean") != 0) {
            code.append("System.out.println(result" + methodName.substring(0,1).toUpperCase() + methodName.substring(1, methodName.length()) + ");\n");
        }

        for (String parameter : parametersArray) {
            parameter = parameter.trim();
            String parameterName = parameter.split(" ")[1];
            if (isOutputParameter(parameter)) {
                code.append(parameterName + "ImagePlus = " + clijObjectName + ".pull(" + parameterName + ");\n");
                code.append(parameterName + "ImagePlus.show();\n");
            }
        }

        code.append("\n// cleanup memory on GPU\n");
        for (String parameter : parametersArray) {
            parameter = parameter.trim();
            String parameterName = parameter.split(" ")[1];
            if (isInputParameter(parameter) || isOutputParameter(parameter)) {
                code.append(clijObjectName + ".release(" + parameterName + ");\n");
            }
        }

        return code.toString();
    }

    private static void buildReference(ArrayList<String> names, HashMap<String, DocumentationItem> methodMap) throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append("# CLIJ reference\n");
        builder.append("This reference contains all methods currently available in CLIJx.\n\n");
        builder.append("__Please note:__ CLIJx is under heavy construction. This list may change at any point.");
        builder.append("\n\n");
        builder.append("<img src=\"images/mini_clij1_logo.png\" width=\"18\" height=\"18\"/> Method is available in CLIJ (stable release)\n");
        builder.append("<img src=\"images/mini_clij2_logo.png\" width=\"18\" height=\"18\"/> Method is available in CLIJ2 (upcoming beta release)\n");
        builder.append("<img src=\"images/mini_clijx_logo.png\" width=\"18\" height=\"18\"/> Method is available in CLIJx (experimental version)\n");
        builder.append("\n\n##ALPHABET##\n\n");

        String firstChar = " ";
        String listOfChars = " A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z";
        for (String sortedName : names) {
            if (sortedName.substring(0,1).toUpperCase().compareTo(firstChar.trim()) != 0) {
                firstChar = sortedName.substring(0,1).toUpperCase();
                builder.append("<a name=\"" + firstChar + "\"></a>\n");
                builder.append("\n## " + firstChar + "\n");

                listOfChars = listOfChars.replace(" " + firstChar, "<a href=\"#" + firstChar + "\">\\[" + firstChar + "\\]</a>");
            }
            DocumentationItem item = methodMap.get(sortedName);
            builder.append(" * ");

            if (item.klass == Kernels.class) {
                builder.append("<img src=\"images/mini_clij1_logo.png\" width=\"18\" height=\"18\"/>");
                builder.append("<img src=\"images/mini_empty_logo.png\" width=\"18\" height=\"18\"/>");
                builder.append("<img src=\"images/mini_empty_logo.png\" width=\"18\" height=\"18\"/>");
            } else {
                if (service.getCLIJMacroPlugin("CLIJ_" + item.methodName) != null) {
                    builder.append("<img src=\"images/mini_clij1_logo.png\" width=\"18\" height=\"18\"/>");
                } else {
                    builder.append("<img src=\"images/mini_empty_logo.png\" width=\"18\" height=\"18\"/>");
                }
                if (service.getCLIJMacroPlugin("CLIJ2_" + item.methodName) != null) {
                    builder.append("<img src=\"images/mini_clij2_logo.png\" width=\"18\" height=\"18\"/>");
                } else {
                    builder.append("<img src=\"images/mini_empty_logo.png\" width=\"18\" height=\"18\"/>");
                }
                if (service.getCLIJMacroPlugin("CLIJx_" + item.methodName) != null) {
                    builder.append("<img src=\"images/mini_clijx_logo.png\" width=\"18\" height=\"18\"/>");
                } else {
                    builder.append("<img src=\"images/mini_empty_logo.png\" width=\"18\" height=\"18\"/>");
                }
            }

            builder.append("<a href=\"" + HTTP_ROOT + "reference_" + item.methodName + "\">");
            builder.append(item.methodName);
            if (item.klass == Kernels.class) {
                builder.append("'");
            }
            builder.append("</a>\n");
        }


        File outputTarget = new File("reference.md");

        FileWriter writer = new FileWriter(outputTarget);
        writer.write(builder.toString().replace("##ALPHABET##", listOfChars));
        writer.close();

    }

    private static void buildAutoCompletion(ArrayList<String> names, HashMap<String, DocumentationItem> methodMap) {

        StringBuilder builder = new StringBuilder();
        builder.append("package net.haesleinhuepf.clijx.jython;\n");
        builder.append("import org.fife.ui.autocomplete.BasicCompletion;\n");
        builder.append("import net.haesleinhuepf.clijx.jython.ScriptingAutoCompleteProvider;\n");
        builder.append("import java.util.ArrayList;");

        builder.append("// this is generated code. See src/test/java/net/haesleinhuepf/clijx/codegenerator for details\n");
        if (isCLIJ2) {
            builder.append("class CLIJ2AutoComplete {\n");
        } else {
            builder.append("class CLIJxAutoComplete {\n");
        }
        builder.append("   \n");
        builder.append("   public static ArrayList<BasicCompletion> getCompletions(final ScriptingAutoCompleteProvider provider) {\n");

        builder.append("       ArrayList<BasicCompletion> list = new ArrayList<BasicCompletion>();\n");
        builder.append("       String headline;\n");
        builder.append("       String description;\n");

        int methodCount = 0;
        for (String name : names) {
            DocumentationItem item = methodMap.get(name);
            if (
                    (isCLIJ2 && item.klass.getPackage().toString().contains(".clij2.")) ||
                    ((!isCLIJ2) && (item.klass.getPackage().toString().contains(".clijx.") || item.klass.getPackage().toString().contains(".clij2.")))
            ) {

                String htmlDescription = item.description;
                if (htmlDescription != null) {
                    htmlDescription = htmlDescription.replace("\n", "<br>");
                    htmlDescription = htmlDescription.replace("\"", "&quot;");
                    htmlDescription = htmlDescription + "<br><br>Parameters:<br>" + item.parametersJava;
                }
                if (isCLIJ2) {
                    builder.append("       headline = \"clij2." + item.methodName + "(" + item.parametersJava + ")\";\n");
                } else {
                    builder.append("       headline = \"clijx." + item.methodName + "(" + item.parametersJava + ")\";\n");
                }
                builder.append("       description = \"<b>" + item.methodName + "</b><br><br>" + htmlDescription + "\";\n");
                builder.append("       list.add(new BasicCompletion(provider, headline, null, description));\n");

                methodCount++;
            }
        }

        builder.append("        return list;\n");
        builder.append("    }\n");
        builder.append("}\n");
        builder.append("// " + methodCount + " methods generated.\n");

        File outputTarget;
        if (isCLIJ2) {
            outputTarget = new File("src/main/java/net/haesleinhuepf/clijx/jython/CLIJ2AutoComplete.java");
        } else {
            outputTarget = new File("src/main/java/net/haesleinhuepf/clijx/jython/CLIJxAutoComplete.java");
        }
        try {
            FileWriter writer = new FileWriter(outputTarget);
            writer.write(builder.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static String searchForExampleScripts(String searchFor, String searchinFolder, String baseLink, String language) {
        StringBuilder result = new StringBuilder();
        //System.out.println(searchinFolder);
        for (File file : new File(searchinFolder).listFiles()) {
            if (!file.isDirectory()) {
                String content = readFile(file.getAbsolutePath());
                if (content.contains(searchFor)) {
                    result.append("<a href=\"" + baseLink + "\"><img src=\"images/language_" + language + ".png\" height=\"20\"/></a> [" + file.getName() + "](" + baseLink + file.getName() + ")  \n");
                }
            }
        }
        return result.toString();
    }

    public static String readFile(String filename) {
        //System.out.println("Reading " + filename);
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

    protected static boolean isOutputParameter(String parameter) {
        return (parameter.contains("ClearCLImage") || parameter.contains("ClearCLBuffer")) && (parameter.contains("destination") || parameter.contains("dst") || parameter.contains("output"));
    }

    protected static boolean isInputParameter(String parameter) {
        return (parameter.contains("ClearCLImage") || parameter.contains("ClearCLBuffer")) && (!parameter.contains("destination") && !parameter.contains("dst") && !parameter.contains("output"));
    }


    protected static String createOutputImageCode(String methodName, String parameterName, String inputImage) {
        if (methodName.compareTo("resliceTop") == 0 ||
                methodName.compareTo("resliceBottom") == 0 ) {
            return parameterName + " = clij.create(new long[]{" + inputImage + ".getWidth(), " + inputImage + ".getDepth(), " + inputImage + ".getHeight()}, " + inputImage + ".getNativeType());\n";
        } else if (methodName.compareTo("resliceLeft") == 0 ||
                methodName.compareTo("resliceRight") == 0 ) {
            return parameterName + " = clij.create(new long[]{" + inputImage + ".getHeight(), " + inputImage + ".getDepth(), " + inputImage + ".getWidth()}, " + inputImage + ".getNativeType());\n";
        } else if (methodName.compareTo("maximumZProjection") == 0 ||
                methodName.compareTo("maximumXYZProjection") == 0  ||
                methodName.compareTo("meanZProjection") == 0  ||
                methodName.compareTo("copySlice") == 0  ||
                methodName.compareTo("minimumZProjection") == 0 ) {
            return parameterName + " = clij.create(new long[]{" + inputImage + ".getWidth(), " + inputImage + ".getHeight()}, " + inputImage + ".getNativeType());\n";
        } else if (methodName.compareTo("convertToImageJBinary") == 0) {
            return "from net.haesleinhuepf.clij.coremem.enums import NativeTypeEnum;\n" +
                    "ClearCLBuffer " + parameterName + " = clij.create(" + inputImage + ".getDimensions(), " + inputImage + ".getHeight()], NativeTypeEnum.UnsignedByte);\n";
        } else {
            return parameterName + " = clij.create(" + inputImage + ");\n";
        }
    }
}
