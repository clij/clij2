package net.haesleinhuepf.clijx.advancedfilters;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.clearcl.ClearCLKernel;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import net.haesleinhuepf.clijx.utilities.CLIJUtilities;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

/**
 * Author: @haesleinhuepf
 *         December 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_maximumOctagon")
public class MaximumOctagon extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        return maximumOctagon(getCLIJx(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asInteger(args[2]));
    }

    public static boolean maximumOctagon(CLIJx clijx, ClearCLBuffer src, ClearCLBuffer dst, Integer iterations) {

        ClearCLImage flip = clijx.create(dst.getDimensions(), CLIJUtilities.nativeToChannelType(dst.getNativeType()));
        ClearCLImage flop = clijx.create(flip);

        ClearCLKernel flipKernel = null;
        ClearCLKernel flopKernel = null;

        clijx.copy(src, flip);

        for (int i = 0 ; i < iterations; i++) {
            if (i % 2 == 0) {
                flipKernel = maximumBox(clijx, flip, flop, flipKernel);
            } else {
                flopKernel = maximumDiamond(clijx, flop, flip, flopKernel);
            }
        }
        if (iterations % 2 == 0) {
            clijx.copy(flip, dst);
        } else {
            clijx.copy(flop, dst);
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

    static ClearCLKernel maximumBox(CLIJx clijx, ClearCLImageInterface src, ClearCLImageInterface dst, ClearCLKernel kernel)
    {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        return clijx.executeSubsequently(MaximumOctagon.class, "maximum_octagon_box_" + src.getDimension() + "d_x.cl", "maximum_octagon_box_" + src.getDimension() + "d", dst.getDimensions(), dst.getDimensions(), parameters, kernel);
    }

    static ClearCLKernel maximumDiamond(CLIJx clijx, ClearCLImageInterface src, ClearCLImageInterface dst, ClearCLKernel kernel)
    {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        return clijx.executeSubsequently(MaximumOctagon.class, "maximum_octagon_diamond_" + src.getDimension() + "d_x.cl", "maximum_octagon_diamond_" + src.getDimension() + "d", dst.getDimensions(), dst.getDimensions(), parameters, kernel);
    }


    @Override
    public String getParameterHelpText() {
        return "Image input, Image destination, Number iterations";
    }

    @Override
    public String getDescription() {
        return "Applies a maximum filter with kernel size 3x3 n times to an image iteratively. " +
                "Odd iterations are done with box neighborhood, even iterations with a diamond. " +
                "Thus, with n > 2, the filter shape is an octagon. The given number of iterations makes the filter " +
                "result very similar to minimum sphere. Approximately:" +
                "radius = iterations - 2";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
