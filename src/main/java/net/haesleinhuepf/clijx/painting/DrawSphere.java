package net.haesleinhuepf.clijx.painting;

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
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_drawSphere")
public class DrawSphere extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image destination, Number x, Number y, Number z, Number radius_x, Number radius_y, Number radius_z";
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

        return drawSphere(clij, input, x, y, z, rX, rY, rZ);
    }

    public static boolean drawSphere(CLIJ clij, ClearCLBuffer output, Float x, Float y, Float z, Float rx, Float ry, Float rz) {
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
            parameters.put("rzsq", Math.pow(rz, 2));
        }
        parameters.put("dst", output);

        return clij.execute(DrawSphere.class, "drawsphere.cl", "draw_sphere_" + output.getDimension() + "D", parameters);
    }

    public static boolean drawSphere(CLIJ clij, ClearCLBuffer output, Float x, Float y, Float rx, Float ry) {
        return drawSphere(clij, output, x, y, 0f, rx, ry, 0f);
    }

        @Override
    public String getDescription() {
        return "Draws a sphere around a given point with given radii in x, y and z (if 3D). All pixels other than in the sphere are untouched. Consider using clij.op.set(buffer, 0); in advance.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
