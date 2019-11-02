package net.haesleinhuepf.clijx.advancedfilters;


import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_extrema")
public class Extrema extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image input1, Image input2, Image destination";
    }

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();
        boolean result = extrema(clij, (ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]),(ClearCLBuffer) (args[2]));
        releaseBuffers(args);
        return result;
    }

    public static boolean extrema(CLIJ clij, ClearCLBuffer input1, ClearCLBuffer input2, ClearCLBuffer output) {

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src1", input1);
        parameters.put("src2", input2);
        parameters.put("dst", output);

        return clij.execute(Extrema.class, "extrema.cl", "extrema", parameters);
    }

    @Override
    public String getDescription() {
        return "Returns an image with pixel values most distant from 0: f(x, y) = x if abs(x) > abs(y), y else.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
