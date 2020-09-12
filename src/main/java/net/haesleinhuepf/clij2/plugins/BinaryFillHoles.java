package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;

/**
 * Author: @haesleinhuepf
 *         March 2020
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_binaryFillHoles")
public class BinaryFillHoles extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized {

    @Override
    public boolean executeCL() {
        getCLIJ2().binaryFillHoles((ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]));
        return true;
    }


    public static boolean binaryFillHoles(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst) {
        assertDifferent(src, dst);

        long[] dims;
        if (src.getDimension() == 3) {
            dims = new long[3];
        } else {
            dims = new long[2];
        }
        for (int d = 0; d < dims.length; d++) {
            dims[d] = src.getDimensions()[d] + 2;
        }
        ClearCLBuffer temp = clij2.create(dims, dst.getNativeType());
        ClearCLBuffer temp2 = clij2.create(dims, dst.getNativeType());

        if (src.getDimension() == 3) {
            clij2.paste(src, temp, 1, 1, 1);
        }else {
            clij2.paste(src, temp, 1, 1);
        }

        clij2.setImageBorders(temp, 2);

        FloodFillDiamond.floodFillDiamond(clij2, temp, temp2, 0f, 2f);

        clij2.notEqualConstant(temp2, temp, 2f);

        if (src.getDimension() == 3) {
            clij2.crop(temp, dst, 1, 1, 1);
        } else {
            clij2.crop(temp, dst, 1, 1);
        }

        clij2.release(temp);
        clij2.release(temp2);

        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Image destination";
    }

    @Override
    public String getDescription() {
        return "Fills holes (pixels with value 0 surrounded by pixels with value 1) in a binary image.";
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
