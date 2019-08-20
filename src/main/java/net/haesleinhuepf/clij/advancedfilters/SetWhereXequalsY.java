package net.haesleinhuepf.clij.advancedfilters;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_setWhereXequalsY")
public class SetWhereXequalsY extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        if (containsCLImageArguments()) {
            return setWhereXequalsY(clij, (ClearCLImage)( args[0]), asFloat(args[1]));
        } else {
            Object[] args = openCLBufferArgs();
            boolean result = setWhereXequalsY(clij, (ClearCLBuffer)( args[0]),  asFloat(args[1]));
            releaseBuffers(args);
            return result;
        }
    }


    public static boolean setWhereXequalsY(CLIJ clij, ClearCLImage clImage, Float value) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("dst", clImage);
        parameters.put("value", value);

        return clij.execute(SetWhereXequalsY.class, "setWhereXequalsY.cl", "set_where_x_equals_y_" + clImage.getDimension() + "d", parameters);
    }

    public static boolean setWhereXequalsY(CLIJ clij, ClearCLBuffer clImage, Float value) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("dst", clImage);
        parameters.put("value", value);

        return clij.execute(SetWhereXequalsY.class, "setWhereXequalsY.cl", "set_where_x_equals_y_" + clImage.getDimension() + "d", parameters);
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Number value";
    }

    @Override
    public String getDescription() {
        return "Sets all pixel values a of a given image A to a constant value v in case its coordinates x == y. " +
                "Otherwise the pixel is not overwritten.\nIf you want to initialize an identity transfrom matrix, set all pixels to 0 first.\n\n" +
                "<pre>f(a) = v</pre>";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
