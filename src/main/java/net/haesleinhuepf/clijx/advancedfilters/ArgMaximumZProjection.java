package net.haesleinhuepf.clijx.advancedfilters;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.converters.implementations.ClearCLImageToImagePlusConverter;
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
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_argMaximumZProjection")
public class ArgMaximumZProjection extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        return argMaximumZProjection(getCLIJx(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), (ClearCLBuffer)(args[2]));
    }

    public static boolean argMaximumZProjection(CLIJx clijx, ClearCLImageInterface src, ClearCLImageInterface dst_max, ClearCLImageInterface dst_arg) {
        assertDifferent(src, dst_max);
        assertDifferent(src, dst_arg);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst_max", dst_max);
        parameters.put("dst_arg", dst_arg);

        clijx.execute(ArgMaximumZProjection.class, "arg_maximum_z_projection_3d_2d_x.cl", "arg_maximum_z_projection_3d_2d", dst_arg.getDimensions(), dst_arg.getDimensions(), parameters);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination_max, Image destination_arg_max";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        return clij.createCLBuffer(new long[]{input.getWidth(), input.getHeight()}, input.getNativeType());
    }

    @Override
    public String getDescription() {
        return "Determines the maximum projection of an image stack along Z." +
                "\nFurthermore, another 2D image is generated with pixels containing the z-index where the maximum was found (zero based).";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }
}
