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
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_flip3D")
public class Flip3D extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        Boolean flipX = asBoolean(args[2]);
        Boolean flipY = asBoolean(args[3]);
        Boolean flipZ = asBoolean(args[4]);
        return flip(getCLIJx(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), flipX, flipY, flipZ);
    }


    public static boolean flip3D(CLIJx clijx, ClearCLImageInterface src, ClearCLImageInterface dst, Boolean flipx, Boolean flipy, Boolean flipz) {
        return flip(clijx, src, dst, flipx, flipy, flipz);
    }

    public static boolean flip(CLIJx clijx, ClearCLImageInterface src, ClearCLImageInterface dst, Boolean flipx, Boolean flipy, Boolean flipz) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("flipx", flipx ? 1 : 0);
        parameters.put("flipy", flipy ? 1 : 0);
        if (src.getDimension() > 2) {
            parameters.put("flipz", flipz ? 1 : 0);
        }
        clijx.execute(Flip3D.class, "flip_" + src.getDimension() +  "d_x.cl", "flip_" + src.getDimension() +  "d", dst.getDimensions(), dst.getDimensions(), parameters);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Boolean flipX, Boolean flipY, Boolean flipZ";
    }

    @Override
    public String getDescription() {
        return "Flips an image in X, Y and/or Z direction depending on boolean flags.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }
}
