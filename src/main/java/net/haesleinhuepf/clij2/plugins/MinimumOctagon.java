package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.clearcl.ClearCLKernel;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.CLIJUtilities;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

/**
 * Author: @haesleinhuepf
 *         December 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_minimumOctagon")
public class MinimumOctagon extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
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
        return getCLIJ2().minimumOctagon((ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asInteger(args[2]));
    }

    public static boolean minimumOctagon(CLIJ2 clij2, ClearCLBuffer src, ClearCLBuffer dst, Integer iterations) {

        ClearCLImage flip = clij2.create(dst.getDimensions(), CLIJUtilities.nativeToChannelType(dst.getNativeType()));
        ClearCLImage flop = clij2.create(flip);

        ClearCLKernel flipKernel = null;
        ClearCLKernel flopKernel = null;

        clij2.copy(src, flip);

        for (int i = 0 ; i < iterations; i++) {
            if (i % 2 == 0) {
                flipKernel = minimumBox(clij2, flip, flop, flipKernel);
            } else {
                flopKernel = minimumDiamond(clij2, flop, flip, flopKernel);
            }
        }
        if (iterations % 2 == 0) {
            clij2.copy(flip, dst);
        } else {
            clij2.copy(flop, dst);
        }

        if (flipKernel != null) {
            flipKernel.close();
        }
        if (flopKernel != null) {
            flopKernel.close();
        }
        flip.close();
        flop.close();

        return true;
    }

    @Deprecated
    public static ClearCLKernel minimumBox(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst, ClearCLKernel kernel)
    {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        return clij2.executeSubsequently(MinimumOctagon.class, "minimum_octagon_box_" + src.getDimension() + "d_x.cl", "minimum_octagon_box_" + src.getDimension() + "d", dst.getDimensions(), dst.getDimensions(), parameters, kernel);
    }

    @Deprecated
    public static ClearCLKernel minimumDiamond(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst, ClearCLKernel kernel)
    {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        return clij2.executeSubsequently(MinimumOctagon.class, "minimum_octagon_diamond_" + src.getDimension() + "d_x.cl", "minimum_octagon_diamond_" + src.getDimension() + "d", dst.getDimensions(), dst.getDimensions(), parameters, kernel);
    }


    @Override
    public String getParameterHelpText() {
        return "Image input, ByRef Image destination, Number iterations";
    }

    @Override
    public String getDescription() {
        return "Applies a minimum filter with kernel size 3x3 n times to an image iteratively. \n\n" +
                "Odd iterations are done with box neighborhood, even iterations with a diamond. " +
                "Thus, with n > 2, the filter shape is an octagon. The given number of iterations makes the filter " +
                "result very similar to minimum sphere. Approximately:" +
                "radius = iterations - 2";
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
