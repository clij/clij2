package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_flip2D")
public class Flip2D extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Image";
    }

    @Override
    public String getOutputType() {
        return "Image";
    }

    @Override
    public String getCategories() {
        return "Transform";
    }

    @Override
    public boolean executeCL() {
        Boolean flipX = asBoolean(args[2]);
        Boolean flipY = asBoolean(args[3]);

        return getCLIJ2().flip((ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), flipX, flipY);
    }

    public static boolean flip2D(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst, Boolean flipx, Boolean flipy) {
        return flip(clij2, src, dst, flipx, flipy);
    }

    public static boolean flip(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst, Boolean flipx, Boolean flipy) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("flipx", flipx ? 1 : 0);
        parameters.put("flipy", flipy ? 1 : 0);
        clij2.execute(Flip2D.class, "flip_2d_x.cl", "flip_2d", dst.getDimensions(), dst.getDimensions(), parameters);
        return true;
    }


    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Image destination, Boolean flip_x, Boolean flip_y";
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
