package net.haesleinhuepf.clij2.plugins;

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
import static net.haesleinhuepf.clij2.utilities.CLIJUtilities.checkDimensions;

/**
 * Author: @haesleinhuepf
 *         June 2020
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_replacePixelsIfZero")
public class ReplacePixelsIfZero extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Image";
    }

    @Override
    public String getOutputType() {
        return "Image";
    }


    @Override
    public boolean executeCL() {
        return replacePixelsIfZero(getCLIJ2(), (ClearCLImageInterface)( args[0]), (ClearCLImageInterface)(args[1]), (ClearCLImageInterface)(args[2]));
    }

    public static boolean replacePixelsIfZero(CLIJ2 clij2, ClearCLImageInterface src1, ClearCLImageInterface src2, ClearCLImageInterface dst) {
        assertDifferent(src1, dst);
        assertDifferent(src2, dst);
        assertDifferent(src2, src1);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src1", src1);
        parameters.put("src2", src2);
        parameters.put("dst", dst);

        clij2.execute(ReplacePixelsIfZero.class, "replace_pixels_if_zero_x.cl", "replace_pixels_if_zero", dst.getDimensions(), dst.getDimensions(), parameters);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image input1, Image input2, ByRef Image destination";
    }

    @Override
    public String getDescription() {
        return "Replaces pixel values x with y in case x is zero.\n\n" +
                "This functionality is comparable to ImageJs image calculator operator 'transparent zero'.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public String getCategories() {
        return "Filter";
    }
}
