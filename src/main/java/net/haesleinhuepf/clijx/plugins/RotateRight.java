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
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_rotateRight")
public class RotateRight extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        return rotateRight(getCLIJx(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]));
    }


    public static boolean rotateRight(CLIJx clijx, ClearCLImageInterface src, ClearCLImageInterface dst) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);

        clijx.execute(RotateRight.class, "rotate_right_" + dst.getDimension() + "d_x.cl", "rotate_right_" + dst.getDimension() + "d", dst.getDimensions(), dst.getDimensions(), parameters);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        if (input.getDimension() == 2) {
            return clij.createCLBuffer(new long[]{input.getHeight(), input.getWidth()}, input.getNativeType());
        } else {
            return clij.createCLBuffer(new long[]{input.getHeight(), input.getWidth(), input.getDepth()}, input.getNativeType());
        }
    }

    @Override
    public String getDescription() {
        return "Rotates a given input image by 90 degrees clockwise. For that, X and Y axis of an image stack\n" +
                "are flipped. This operation is similar to ImageJs 'Reslice [/]' method but offers less flexibility \n" +
                "such as interpolation.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
