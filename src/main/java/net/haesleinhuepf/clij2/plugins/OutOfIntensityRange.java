package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;

/**
 * Author: @haesleinhuepf
 *         August 2020
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_outOfIntensityRange")
public class OutOfIntensityRange extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Image";
    }

    @Override
    public String getOutputType() {
        return "Binary Image";
    }

    @Override
    public boolean executeCL() {
        outOfIntensityRange(getCLIJ2(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asFloat(args[2]), asFloat(args[3]));
        return true;
    }

    public static boolean outOfIntensityRange(CLIJ2 clij2, ClearCLBuffer src, ClearCLImageInterface dst, Float minimum, Float maximum) {
        assertDifferent(src, dst);
        ClearCLBuffer above = clij2.create(src.getDimensions(), NativeTypeEnum.UnsignedByte);
        ClearCLBuffer below = clij2.create(src.getDimensions(), NativeTypeEnum.UnsignedByte);
        clij2.greaterConstant(src, above, maximum);
        clij2.smallerConstant(src, below, minimum);
        clij2.binaryOr(above, below, dst);
        above.close();
        below.close();
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Image destination";
    }

    @Override
    public String getDescription() {
        return "Sets all pixels to 1 if their intensity lies out of a given range, and 0 otherwise.\n\n" +
                "Given minimum and maximum are considered part of the range.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public String getCategories() {
        return "Binary, Filter";
    }
}
