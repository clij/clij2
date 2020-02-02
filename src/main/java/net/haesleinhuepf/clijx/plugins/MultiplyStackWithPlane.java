package net.haesleinhuepf.clijx.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_multiplyStackWithPlane")
public class MultiplyStackWithPlane extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {
    @Override
    public boolean executeCL() {
        return multiplyStackWithPlane(getCLIJx(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), (ClearCLBuffer)(args[2]));
    }

    public static boolean multiplyStackWithPlane(CLIJx clijx, ClearCLImageInterface input3d, ClearCLImageInterface input2d, ClearCLImageInterface output3d) {
        assertDifferent(input2d, output3d);
        assertDifferent(input3d, output3d);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", input3d);
        parameters.put("src1", input2d);
        parameters.put("dst", output3d);
        clijx.execute(MultiplyStackWithPlane.class, "multiply_stack_with_plane_3d_x.cl", "multiply_stack_with_plane_3d", output3d.getDimensions(), output3d.getDimensions(), parameters);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image sourceStack, Image sourcePlane, Image destination";
    }

    @Override
    public String getDescription() {
        return "Multiplies all pairs of pixel values x and y from an image stack X and a 2D image Y. x and y are at \n" +
                "the same spatial position within a plane.\n\n<pre>f(x, y) = x * y</pre>";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D (first parameter), 2D (second parameter), 3D (result)";
    }
}
