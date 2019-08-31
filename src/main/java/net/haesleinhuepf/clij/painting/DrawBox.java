package net.haesleinhuepf.clij.painting;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
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
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_drawBox")
public class DrawBox extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image destination, Number x, Number y, Number z, Number width, Number height, Number depth";
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

        return drawBox(clij, input, x, y, z, width, height, depth);
    }

    public static boolean drawBox(CLIJ clij, ClearCLBuffer output, Float x, Float y, Float z, Float width, Float height, Float depth) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("x1", x);
        parameters.put("y1", y);
        parameters.put("x2", x + width - 1);
        parameters.put("y2", y + height - 1);
        if (output.getDimension() > 2) {
            parameters.put("z1", z);
            parameters.put("z2", z + depth - 1);
        }
        parameters.put("dst", output);

        return clij.execute(DrawBox.class, "drawbox.cl", "draw_box_" + output.getDimension() + "D", parameters);
    }

    public static boolean drawBox(CLIJ clij, ClearCLBuffer output, Float x, Float y, Float width, Float height) {
        return drawBox(clij, output, x, y, 0f, width, height, 0f);
    }


        @Override
    public String getDescription() {
        return "Draws a box at a given start point with given size. All pixels other than in the box are untouched. Consider using clij.op.set(buffer, 0); in advance.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
