package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_setColumn")
public class SetColumn extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized {
    @Override
    public String getCategories() {
        return "Math";
    }

    @Override
    protected Object[] getDefaultValues() {
        return new Object[]{null, 0, 0};
    }

    @Override
    public boolean executeCL() {
        return getCLIJ2().setColumn((ClearCLBuffer)( args[0]), asInteger(args[1]), asFloat(args[2]));
    }

    public static boolean setColumn(CLIJ2 clij2, ClearCLImageInterface clImage, Integer column, Float value) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("dst", clImage);
        parameters.put("value", value);
        parameters.put("column", column);

        long[] dimensions = Arrays.copyOf(clImage.getDimensions(), clImage.getDimensions().length);
        dimensions[0] = 1;

        clij2.activateSizeIndependentKernelCompilation();
        clij2.execute(SetColumn.class, "set_column_" + clImage.getDimension() + "d_x.cl", "set_column_" + clImage.getDimension() + "d", clImage.getDimensions(), dimensions, parameters);
        return true;
    }


    @Override
    public String getParameterHelpText() {
        return "Image source, Number column_index, Number value";
    }

    @Override
    public String getDescription() {
        return "Sets all pixel values x of a given column in X to a constant value v.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
