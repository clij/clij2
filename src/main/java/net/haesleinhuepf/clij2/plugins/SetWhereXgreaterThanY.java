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

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_setWhereXgreaterThanY")
public class SetWhereXgreaterThanY extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        boolean result = getCLIJ2().setWhereXgreaterThanY((ClearCLBuffer)( args[0]),  asFloat(args[1]));
        return result;
    }


    public static boolean setWhereXgreaterThanY(CLIJ2 clij2, ClearCLImageInterface clImage, Float value) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("dst", clImage);
        parameters.put("value", value);

        clij2.activateSizeIndependentKernelCompilation();
        clij2.execute(SetWhereXgreaterThanY.class, "set_where_x_greater_than_y_" + clImage.getDimension() + "d_x.cl", "set_where_x_greater_than_y_" + clImage.getDimension() + "d", clImage.getDimensions(), clImage.getDimensions(),  parameters);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Number value";
    }

    @Override
    public String getDescription() {
        return "Sets all pixel values a of a given image A to a constant value v in case its coordinates x > y. \n\n" +
                "Otherwise the pixel is not overwritten.\nIf you want to initialize an identity transfrom matrix, set all pixels to 0 first.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
