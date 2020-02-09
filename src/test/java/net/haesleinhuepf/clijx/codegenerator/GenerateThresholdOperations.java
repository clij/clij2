package net.haesleinhuepf.clijx.codegenerator;

import net.haesleinhuepf.clij2.plugins.AutoThresholderImageJ1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GenerateThresholdOperations {
    public static void main(String[] args) throws IOException {
        String templateFilename = "src/main/java/net/haesleinhuepf/clij2/plugins/ThresholdOtsu.java";
        String template = new String(Files.readAllBytes(Paths.get(templateFilename)));

        for (String method  : AutoThresholderImageJ1.getMethods()) {
            if (method.compareTo("Otsu") != 0)
            {
                String content = template + "";
                content = content.replace("Otsu", method);
                content = content.replace("public class", "// This is generated code. See net.haesleinhuepf.clijx.codegenerator.GenerateThresholdOperations for details\npublic class");

                File outputTarget = new File(templateFilename.replace("Otsu", method));
                System.out.println(outputTarget.getName());
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
