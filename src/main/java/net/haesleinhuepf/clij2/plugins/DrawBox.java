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
 * DrawLine
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 08 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_drawBox")
public class DrawBox extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {


    @Override
    protected Object[] getDefaultValues() {
        return new Object[]{null, 0, 0, 0, 10, 10, 10, 255};
    }

    @Override
    public String getParameterHelpText() {
        return "ByRef Image destination, Number x, Number y, Number z, Number width, Number height, Number depth, Number value";
    }

    @Override
    public boolean executeCL() {
        ClearCLBuffer input = (ClearCLBuffer)args[0];
        Float x = asFloat(args[1]);
        Float y = asFloat(args[2]);
        Float z = asFloat(args[3]);
        Float width = asFloat(args[4]);
        Float height = asFloat(args[5]);
        Float depth = asFloat(args[6]);
        Float value = asFloat(args[7]);

        return getCLIJ2().drawBox(input, x, y, z, width, height, depth, value);
    }

    public static boolean drawBox(CLIJ2 clij2, ClearCLImageInterface output, Float x, Float y, Float z, Float width, Float height, Float depth) {
        return drawBox(clij2, output, x,y,z, width,height,depth,1.0f);
    }

    public static boolean drawBox(CLIJ2 clij2, ClearCLImageInterface output, Float x, Float y, Float z, Float width, Float height, Float depth, Float value) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("x1", x);
        parameters.put("y1", y);
        parameters.put("x2", x + width);
        parameters.put("y2", y + height);
        if (output.getDimension() > 2) {
            parameters.put("z1", z);
            parameters.put("z2", z + depth);
        }
        parameters.put("dst", output);
        parameters.put("value", value);

        clij2.execute(DrawBox.class, "draw_box_" + output.getDimension() + "d_x.cl", "draw_box_" + output.getDimension() + "d", output.getDimensions(), output.getDimensions(), parameters);
        return true;
    }

    public static boolean drawBox(CLIJ2 clij2, ClearCLImageInterface output, Float x, Float y, Float width, Float height) {
        return drawBox(clij2, output, x, y, 0f, width, height, 0f);
    }


        @Override
    public String getDescription() {
        return "Draws a box at a given start point with given size. \n" +
                "All pixels other than in the box are untouched. Consider using `set(buffer, 0);` in advance.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
