package net.haesleinhuepf.clijx.advancedfilters;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_setColumn")
public class SetColumn extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        return setColumn(getCLIJx(), (ClearCLBuffer)( args[0]), asInteger(args[1]), asFloat(args[2]));
    }

    public static boolean setColumn(CLIJx clijx, ClearCLImageInterface clImage, Integer column, Float value) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("dst", clImage);
        parameters.put("value", value);
        parameters.put("column", column);

        long[] dimensions = Arrays.copyOf(clImage.getDimensions(), clImage.getDimensions().length);
        dimensions[0] = 1;

        clijx.execute(SetColumn.class, "set_column_" + clImage.getDimension() + "d_x.cl", "set_column_" + clImage.getDimension() + "d", clImage.getDimensions(), dimensions, parameters);
        return true;
    }


    @Override
    public String getParameterHelpText() {
        return "Image source, Number columnIndex, Number value";
    }

    @Override
    public String getDescription() {
        return "Sets all pixel values x of a given column in X to a constant value v.\n\n" +
                "<pre>f(x) = v</pre>";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
