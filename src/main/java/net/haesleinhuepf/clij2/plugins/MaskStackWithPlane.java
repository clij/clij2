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

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_maskStackWithPlane")
public class MaskStackWithPlane extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        return maskStackWithPlane(getCLIJ2(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), (ClearCLBuffer)(args[2]));
    }


    public static boolean maskStackWithPlane(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface mask, ClearCLImageInterface dst) {
        assertDifferent(src, dst);
        assertDifferent(mask, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("mask", mask);
        parameters.put("dst", dst);

        clij2.execute(MaskStackWithPlane.class, "mask_stack_with_plane_3d_x.cl", "mask_stack_with_plane_3d", dst.getDimensions(), dst.getDimensions(), parameters);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image mask, ByRef Image destination";
    }

    @Override
    public String getDescription() {
        return "Computes a masked image by applying a 2D mask to an image stack. \n\nAll pixel values x of image X will be copied\n" +
                "to the destination image in case pixel value m at the same spatial position in the mask image is not equal to \n" +
                "zero.\n\n" +
                "<pre>f(x,m) = (x if (m != 0); (0 otherwise))</pre>";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D, 2D -> 3D";
    }
}
