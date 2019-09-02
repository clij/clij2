package net.haesleinhuepf.clij.painting;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.advancedfilters.Extrema;
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
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_drawLine")
public class DrawLine extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image destination, Number x1, Number y1, Number z1, Number x2, Number y2, Number z2, Number thickness";
    }

    @Override
    public boolean executeCL() {
        ClearCLBuffer input = (ClearCLBuffer)args[0];
        Float x1 = asFloat(args[1]);
        Float y1 = asFloat(args[2]);
        Float z1 = asFloat(args[3]);
        Float x2 = asFloat(args[4]);
        Float y2 = asFloat(args[5]);
        Float z2 = asFloat(args[6]);
        Float thickness = asFloat(args[7]);

        return drawLine(clij, input, x1, y1, z1, x2, y2, z2, thickness);
    }

    public static boolean drawLine(CLIJ clij, ClearCLBuffer output, Float x1, Float y1, Float z1, Float x2, Float y2, Float z2, Float thickness) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("x1", x1);
        parameters.put("y1", y1);
        parameters.put("x2", x2);
        parameters.put("y2", y2);
        if (output.getDimension() > 2) {
            parameters.put("z1", z1);
            parameters.put("z2", z2);
        }
        parameters.put("radius", new Float(thickness / 2));
        parameters.put("dst", output);

        //System.out.println("Draw line from " + x1 + "/" + y1 + "/" + z1 + " to "  + x2 + "/" + y2 + "/" + z2);
        return clij.execute(DrawLine.class, "drawline.cl", "draw_line_" +output.getDimension() + "D", parameters);
    }

    @Override
    public String getDescription() {
        return "Draws a line between two points with a given thickness. All pixels other than on the line are untouched. Consider using clij.op.set(buffer, 0); in advance.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
