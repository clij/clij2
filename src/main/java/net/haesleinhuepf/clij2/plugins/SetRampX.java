package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

/**
 * Author: @haesleinhuepf
 *         February 2020
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_setRampX")
public class SetRampX extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        boolean result = setRampX(getCLIJ2(), (ClearCLBuffer)( args[0]));
        return result;
    }


    public static boolean setRampX(CLIJ2 clij2, ClearCLImageInterface clImage) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("dst", clImage);

        clij2.execute(SetRampX.class, "set_ramp_x_" + clImage.getDimension() + "d_x.cl", "set_ramp_x_" + clImage.getDimension() + "d", clImage.getDimensions(), clImage.getDimensions(),  parameters);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Number value";
    }

    @Override
    public String getDescription() {
        return "Sets all pixel values to their X coordinate";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
