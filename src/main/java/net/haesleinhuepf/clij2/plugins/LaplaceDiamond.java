package net.haesleinhuepf.clij2.plugins;


import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_laplaceDiamond")
public class LaplaceDiamond extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image input, Image destination";
    }

    @Override
    public boolean executeCL() {
        ClearCLBuffer input = (ClearCLBuffer) (args[0]);
        ClearCLBuffer output = (ClearCLBuffer) (args[1]);

        boolean result = laplaceSphere(clij, input, output);
        return result;
    }


    public static boolean laplaceSphere(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        return clij.execute(LaplaceDiamond.class, "laplace_diamond_" + src.getDimension() + "d.cl", "laplace_diamond_" + src.getDimension() + "d", dst.getDimensions(), parameters);
    }


    @Override
    public String getDescription() {
        return "Applies the Laplace operator (Diamond neighborhood) to an image.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
