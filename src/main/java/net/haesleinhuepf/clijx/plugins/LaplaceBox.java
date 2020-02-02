package net.haesleinhuepf.clijx.plugins;


import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_laplaceBox")
public class LaplaceBox extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image input, Image destination";
    }

    @Override
    public boolean executeCL() {
        ClearCLBuffer input = (ClearCLBuffer) (args[0]);
        ClearCLBuffer output = (ClearCLBuffer) (args[1]);

        boolean result = laplaceBox(clij, input, output);
        return result;
    }


    public static boolean laplaceBox(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        return clij.execute(ConnectedComponentsLabeling.class, "laplace" + src.getDimension() + "d.cl", "laplace_box_image" + src.getDimension() + "d", dst.getDimensions(), parameters);
    }


    @Override
    public String getDescription() {
        return "Applies the Laplace operator (Box neighborhood) to an image.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
