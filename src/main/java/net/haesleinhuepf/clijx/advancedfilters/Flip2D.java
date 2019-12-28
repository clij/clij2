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
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_flip2D")
public class Flip2D extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        Boolean flipX = asBoolean(args[2]);
        Boolean flipY = asBoolean(args[3]);

        return flip(getCLIJx(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), flipX, flipY);
    }

    public static boolean flip2D(CLIJx clijx, ClearCLImageInterface src, ClearCLImageInterface dst, Boolean flipx, Boolean flipy) {
        return flip(clijx, src, dst, flipx, flipy);
    }

    public static boolean flip(CLIJx clijx, ClearCLImageInterface src, ClearCLImageInterface dst, Boolean flipx, Boolean flipy) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("flipx", flipx ? 1 : 0);
        parameters.put("flipy", flipy ? 1 : 0);
        clijx.execute(Flip2D.class, "flip_2d_x.cl", "flip_2d", dst.getDimensions(), dst.getDimensions(), parameters);
        return true;
    }


    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Boolean flipX, Boolean flipY";
    }

    @Override
    public String getDescription() {
        return "Flips an image in X and/or Y direction depending on boolean flags.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }
}
