package net.haesleinhuepf.clij2.plugins;


import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_replaceIntensity")
public class ReplaceIntensity extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image input, Image destination, Number oldValue, Number newValue";
    }

    @Override
    public boolean executeCL() {
        boolean result = replaceIntensity(getCLIJ2(), (ClearCLBuffer) (args[0]),(ClearCLBuffer) (args[1]), asFloat(args[2]), asFloat(args[3]));
        return result;
    }

    public static boolean replaceIntensity(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst, Float in, Float out) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.clear();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("in", in);
        parameters.put("out", out);
        clij2.execute(ReplaceIntensity.class, "replace_intensity_x.cl", "replace", dst.getDimensions(), dst.getDimensions(), parameters);
        return true;
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
