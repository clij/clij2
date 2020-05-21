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
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_setRow")
public class SetRow extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    protected Object[] getDefaultValues() {
        return new Object[]{null, 0, 0};
    }

    @Override
    public boolean executeCL() {
        return getCLIJ2().setRow((ClearCLBuffer)( args[0]), asInteger(args[1]), asFloat(args[2]));
    }

    public static boolean setRow(CLIJ2 clij2, ClearCLImageInterface clImage, Integer row, Float value) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("dst", clImage);
        parameters.put("value", value);
        parameters.put("row", row);

        long[] dimensions = Arrays.copyOf(clImage.getDimensions(), clImage.getDimensions().length);
        dimensions[1] = 1;

        clij2.activateSizeIndependentKernelCompilation();
        clij2.execute(SetRow.class, "set_row_" + clImage.getDimension() + "d_x.cl", "set_row_" + clImage.getDimension() + "d", clImage.getDimensions(), dimensions, parameters);
        return true;
    }


    @Override
    public String getParameterHelpText() {
        return "Image source, Number rowIndex, Number value";
    }

    @Override
    public String getDescription() {
        return "Sets all pixel values x of a given row in X to a constant value v.\n\n" +
                "<pre>f(x) = v</pre>";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
