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

import java.util.HashMap;

/**
 * DrawLine
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 08 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_drawSphere")
public class DrawSphere extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized {


    @Override
    public Object[] getDefaultValues() {
        return new Object[]{null, 5, 5, 5, 3, 3, 3, 255};
    }

    @Override
    public String getParameterHelpText() {
        return "ByRef Image destination, Number x, Number y, Number z, Number radius_x, Number radius_y, Number radius_z, Number value";
    }

    @Override
    public boolean executeCL() {
        ClearCLBuffer input = (ClearCLBuffer)args[0];
        Float x = asFloat(args[1]);
        Float y = asFloat(args[2]);
        Float z = asFloat(args[3]);
        Float rX = asFloat(args[4]);
        Float rY = asFloat(args[5]);
        Float rZ = asFloat(args[6]);
        Float value = asFloat(args[7]);

        return getCLIJ2().drawSphere(input, x, y, z, rX, rY, rZ, value);
    }

    public static boolean drawSphere(CLIJ2 clij2, ClearCLImageInterface output, Float x, Float y, Float z, Float rx, Float ry, Float rz) {
        return drawSphere(clij2, output, x,y,z,rx,ry,rz, 1.0f);
    }
    public static boolean drawSphere(CLIJ2 clij2, ClearCLImageInterface output, Float x, Float y, Float z, Float rx, Float ry, Float rz, Float value) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("cx", x);
        parameters.put("cy", y);
        parameters.put("rx", rx);
        parameters.put("ry", ry);
        parameters.put("rxsq", new Float(Math.pow(rx, 2)));
        parameters.put("rysq", new Float(Math.pow(ry, 2)));
        System.out.print("rxsq " + parameters.get("rxsq"));
        System.out.print("rysq " + parameters.get("rysq"));

        if(output.getDimension() > 2) {
            parameters.put("cz", z);
            parameters.put("rz", rz);
            parameters.put("rzsq", new Float(Math.pow(rz, 2)));
        }
        parameters.put("dst", output);
        parameters.put("value", value);

        clij2.execute(DrawSphere.class, "draw_sphere_" + output.getDimension() + "d_x.cl", "draw_sphere_" + output.getDimension() + "d", output.getDimensions(), output.getDimensions(), parameters);
        return true;
    }

    public static boolean drawSphere(CLIJ2 clij2, ClearCLImageInterface output, Float x, Float y, Float rx, Float ry) {
        return drawSphere(clij2, output, x, y, 0f, rx, ry, 0f, 1.0f);
    }

    public static boolean drawSphere(CLIJ2 clij2, ClearCLImageInterface output, Float x, Float y, Float rx, Float ry, Float value) {
        return drawSphere(clij2, output, x, y, 0f, rx, ry, 0f, value);
    }

    @Override
    public String getDescription() {
        return "Draws a sphere around a given point with given radii in x, y and z (if 3D). \n\n" +
                " All pixels other than in the sphere are untouched. Consider using `set(buffer, 0);` in advance.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public String getCategories() {
        return "Visualisation";
    }
}
