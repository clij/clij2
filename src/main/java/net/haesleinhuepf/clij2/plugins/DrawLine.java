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
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_drawLine")
public class DrawLine extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "ByRef Image destination, Number x1, Number y1, Number z1, Number x2, Number y2, Number z2, Number thickness, Number value";
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
        Float value = asFloat(args[8]);

        return getCLIJ2().drawLine(input, x1, y1, z1, x2, y2, z2, thickness, value);
    }


    public static boolean drawLine(CLIJ2 clij2, ClearCLImageInterface output, Float x1, Float y1, Float z1, Float x2, Float y2, Float z2, Float thickness) {
        return drawLine(clij2, output, x1,y1,z1, x2,y2,z2, thickness, 1.0f);
    }

    public static boolean drawLine(CLIJ2 clij2, ClearCLImageInterface output, Float x1, Float y1, Float z1, Float x2, Float y2, Float z2, Float thickness, Float value) {

        long[] globalSizes;

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("x1", x1);
        parameters.put("y1", y1);
        parameters.put("x2", x2);
        parameters.put("y2", y2);
        if (output.getDimension() > 2) {
            parameters.put("z1", z1);
            parameters.put("z2", z2);
            globalSizes = new long[]{
                    (long)(Math.abs(x1 - x2) + 1 + thickness),
                    (long)(Math.abs(y1 - y2) + 1 + thickness),
                    (long)(Math.abs(z1 - z2) + 1 + thickness)
            };
        } else {
            globalSizes = new long[]{
                    (long)(Math.abs(x1 - x2) + 1 + thickness),
                    (long)(Math.abs(y1 - y2) + 1 + thickness)
            };
        }
        parameters.put("radius", new Float(thickness / 2));
        parameters.put("dst", output);
        parameters.put("value", value);

        //System.out.println("Draw line from " + x1 + "/" + y1 + "/" + z1 + " to "  + x2 + "/" + y2 + "/" + z2);
        clij2.execute(DrawLine.class, "drawline_" +output.getDimension() + "d_x.cl", "draw_line_" +output.getDimension() + "D", globalSizes, globalSizes, parameters);
        return true;
    }

    @Override
    public String getDescription() {
        return "Draws a line between two points with a given thickness. \n\n" +
                "All pixels other than on the line are untouched. Consider using `set(buffer, 0);` in advance.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
