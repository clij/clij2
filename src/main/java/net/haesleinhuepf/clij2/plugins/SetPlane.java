package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_setPlane")
public class SetPlane extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        return setPlane(getCLIJ2(), (ClearCLBuffer)( args[0]), asInteger(args[1]), asFloat(args[2]));
    }

    public static boolean setPlane(CLIJ2 clij2, ClearCLImageInterface clImage, Integer plane, Float value) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("dst", clImage);
        parameters.put("value", value);
        parameters.put("plane", plane);

        long[] dimensions = Arrays.copyOf(clImage.getDimensions(), clImage.getDimensions().length);
        if (dimensions.length > 2) {
            dimensions[2] = 1;
        }

        clij2.execute(SetPlane.class, "set_plane_" + clImage.getDimension() + "d_x.cl", "set_plane_" + clImage.getDimension() + "d", clImage.getDimensions(), dimensions, parameters);
        return true;
    }


    @Override
    public String getParameterHelpText() {
        return "Image source, Number rowIndex, Number value";
    }

    @Override
    public String getDescription() {
        return "Sets all pixel values x of a given plane in X to a constant value v.\n\n" +
                "<pre>f(x) = v</pre>";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
