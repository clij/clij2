package net.haesleinhuepf.clijx.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_set")
public class Set extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        return set(getCLIJx(), (ClearCLBuffer)( args[0]),  asFloat(args[1]));
    }

    public static boolean set(CLIJx clijx, ClearCLImageInterface clImage, Float value) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("dst", clImage);
        parameters.put("value", value);

        clijx.execute(Set.class, "set_" + clImage.getDimension() + "d_x.cl", "set_" + clImage.getDimension() + "d", clImage.getDimensions(), clImage.getDimensions(), parameters);
        return true;
    }


    @Override
    public String getParameterHelpText() {
        return "Image source, Number value";
    }

    @Override
    public String getDescription() {
        return "Sets all pixel values x of a given image X to a constant value v.\n\n" +
                "<pre>f(x) = v</pre>";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
