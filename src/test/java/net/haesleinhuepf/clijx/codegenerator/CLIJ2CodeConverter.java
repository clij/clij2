package net.haesleinhuepf.clijx.codegenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CLIJ2CodeConverter {
    public static void main(String[] args) throws IOException {
        File folder = new File("src/main/java/net/haesleinhuepf/clij2/temp/");
        for (File file : folder.listFiles()) {
            if (!file.isDirectory() && file.getName().endsWith(".java")) {
                String content = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
                content = content.replace("import net.haesleinhuepf.clijx.CLIJx;", "import net.haesleinhuepf.clij2.CLIJ2;");
                content = content.replace("import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;", "import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;");
                content = content.replace("CLIJx", "CLIJ2");
                content = content.replace("clijx", "clij2");

                File outputTarget = new File(file.getAbsolutePath());

                try {
                    FileWriter writer = new FileWriter(outputTarget);
                    writer.write(content);
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
