package net.haesleinhuepf.clijx.codegenerator;

import net.haesleinhuepf.clij.macro.CLIJMacroPluginService;
import org.scijava.Context;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CLIJ2WrapperGenerator {
    public static void main(String... args) {
        CLIJMacroPluginService service = new Context(CLIJMacroPluginService.class).getService(CLIJMacroPluginService.class);
        for (String pluginName : service.getCLIJMethodNames()) {
            if (pluginName.toLowerCase().contains("clij2")) {

                String methodName = pluginName.replace("CLIJ2", "CLIJx");
                String className = pluginName.replace("CLIJ2_", "");
                className = className.substring(0,1).toUpperCase() + className.substring(1);

                String template =
                        "package net.haesleinhuepf.clijx.clij2wrappers;\n" +
                                "\n" +
                                "\n" +
                                "import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;\n" +
                                "import org.scijava.plugin.Plugin;\n" +
                                "\n" +
                                "// this is generated code. See src/test/net.haesleinhuepf.clijx.codegenerator.CLIJ2WrapperGenerator for details.\n" +
                                "@Plugin(type = CLIJMacroPlugin.class, name = \"" + methodName + "\")\n" +
                                "public class " + className + " extends net.haesleinhuepf.clij2.plugins." + className + " {\n" +
                                "}\n";

                //System.out.println(template);

                File outputTarget = new File("src/main/java/net/haesleinhuepf/clijx/clij2wrappers/" + className + ".java");

                try {
                    FileWriter writer = new FileWriter(outputTarget);
                    writer.write(template);
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
