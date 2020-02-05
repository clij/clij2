package net.haesleinhuepf.clijx.codegenerator;

import java.io.IOException;

/**
 * RunAll
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 12 2019
 */
public class RunAll {
    public static void main(String ... args) throws IOException {
        OpGenerator.main(args);
        DocumentationGenerator.main(args);
    }
}
