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

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_rotateClockwise")
public class RotateClockwise extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        return rotateRight(getCLIJ2(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]));
    }

    @Deprecated
    public static boolean rotateRight(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst) {
        return rotateClockwise(clij2, src, dst);
    }

    public static boolean rotateClockwise(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);

        clij2.execute(RotateClockwise.class, "rotate_clockwise_" + dst.getDimension() + "d_x.cl", "rotate_right_" + dst.getDimension() + "d", dst.getDimensions(), dst.getDimensions(), parameters);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Image destination";
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
