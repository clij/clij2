package net.haesleinhuepf.clijx.advancedfilters;


import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_replaceIntensity")
public class ReplaceIntensity extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image input, Image destination, Number oldValue, number newValue";
    }

    @Override
    public boolean executeCL() {
        boolean result = replaceIntensity(clij, (ClearCLBuffer) (args[0]),(ClearCLBuffer) (args[1]), asFloat(args[2]), asFloat(args[3]));
        return result;
    }

    public static boolean replaceIntensity(CLIJ clij, ClearCLImageInterface src, ClearCLImageInterface dst, Float in, Float out) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.clear();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("in", in);
        parameters.put("out", out);
        return clij.execute(ConnectedComponentsLabeling.class, "cca.cl", "replace", parameters);
    }

    @Override
    public String getDescription() {
        return "Replaces a specific intensity in an image with a given new value.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
