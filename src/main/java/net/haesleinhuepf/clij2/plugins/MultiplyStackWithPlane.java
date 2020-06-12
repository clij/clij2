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

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_multiplyStackWithPlane")
public class MultiplyStackWithPlane extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized {
    @Override
    public String getCategories() {
        return "Math";
    }
    @Override
    public boolean executeCL() {
        return getCLIJ2().multiplyStackWithPlane((ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), (ClearCLBuffer)(args[2]));
    }

    public static boolean multiplyStackWithPlane(CLIJ2 clij2, ClearCLImageInterface input3d, ClearCLImageInterface input2d, ClearCLImageInterface output3d) {
        assertDifferent(input2d, output3d);
        assertDifferent(input3d, output3d);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", input3d);
        parameters.put("src1", input2d);
        parameters.put("dst", output3d);
        clij2.execute(MultiplyStackWithPlane.class, "multiply_stack_with_plane_3d_x.cl", "multiply_stack_with_plane_3d", output3d.getDimensions(), output3d.getDimensions(), parameters);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image sourceStack, Image sourcePlane, ByRef Image destination";
    }

    @Override
    public String getDescription() {
        return "Multiplies all pairs of pixel values x and y from an image stack X and a 2D image Y. \n\nx and y are at \n" +
                "the same spatial position within a plane.\n\n<pre>f(x, y) = x * y</pre>";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D (first parameter), 2D (second parameter), 3D (result)";
    }
}
