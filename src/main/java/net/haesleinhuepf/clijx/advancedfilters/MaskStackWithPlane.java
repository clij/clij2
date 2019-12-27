package net.haesleinhuepf.clijx.advancedfilters;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
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

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_maskStackWithPlane")
public class MaskStackWithPlane extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        return maskStackWithPlane(getCLIJx(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), (ClearCLBuffer)(args[2]));
    }


    public static boolean maskStackWithPlane(CLIJx clijx, ClearCLImageInterface src, ClearCLImageInterface mask, ClearCLImageInterface dst) {
        assertDifferent(src, dst);
        assertDifferent(mask, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("mask", mask);
        parameters.put("dst", dst);

        clijx.execute(Kernels.class, "mask_stack_with_plane_3d_x.cl", "mask_stack_with_plane_3d", dst.getDimensions(), dst.getDimensions(), parameters);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source_3d, Image mask_2d, Image destination_3d";
    }

    @Override
    public String getDescription() {
        return "Computes a masked image by applying a 2D mask to an image stack. All pixel values x of image X will be copied\n" +
                "to the destination image in case pixel value m at the same spatial position in the mask image is not equal to \n" +
                "zero.\n\n" +
                "<pre>f(x,m) = (x if (m != 0); (0 otherwise))</pre>";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D / 2D -> 3D";
    }
}
