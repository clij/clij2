package net.haesleinhuepf.clijx.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.clearcl.ClearCLKernel;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import net.haesleinhuepf.clij2.utilities.CLIJUtilities;
import org.scijava.plugin.Plugin;

import static net.haesleinhuepf.clij2.plugins.MaximumOctagon.maximumBox;
import static net.haesleinhuepf.clij2.plugins.MaximumOctagon.maximumDiamond;
import static net.haesleinhuepf.clij2.plugins.MinimumOctagon.minimumBox;
import static net.haesleinhuepf.clij2.plugins.MinimumOctagon.minimumDiamond;

/**
 * Author: @haesleinhuepf
 *         December 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_topHatOctagonSliceBySlice")
public class TopHatOctagonSliceBySlice extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        return topHatOctagonSliceBySlice(getCLIJx(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asInteger(args[2]));
    }

    public static boolean topHatOctagonSliceBySlice(CLIJx clijx, ClearCLBuffer src3D, ClearCLBuffer dst3D, Integer iterations) {

        ClearCLImage flip = clijx.create(new long[]{src3D.getWidth(), src3D.getHeight()}, CLIJUtilities.nativeToChannelType(src3D.getNativeType()));
        ClearCLImage flop = clijx.create(flip);
        ClearCLImage src2D = clijx.create(flip);

        ClearCLKernel flipMinKernel = null;
        ClearCLKernel flopMinKernel = null;
        ClearCLKernel flipMaxKernel = null;
        ClearCLKernel flopMaxKernel = null;

        for (int z = 0; z < dst3D.getDepth(); z++) {



            clijx.copySlice(src3D, src2D, z);
            clijx.copy(src2D, flip);

            for (int i = 0; i < iterations; i++) {
                if (i % 2 == 0) {
                    flipMinKernel = minimumBox(clijx, flip, flop, flipMinKernel);
                } else {
                    flopMinKernel = minimumDiamond(clijx, flop, flip, flopMinKernel);
                }
            }
            if (iterations % 2 == 0) {
                //clijx.copy(flip, dst);
            } else {
                clijx.copy(flop, flip);
            }

            for (int i = 0; i < iterations; i++) {
                if (i % 2 == 0) {
                    flipMaxKernel = maximumBox(clijx, flip, flop, flipMaxKernel);
                } else {
                    flopMaxKernel = maximumDiamond(clijx, flop, flip, flopMaxKernel);
                }
            }
            if (iterations % 2 == 0) {
                clijx.subtractImages(src2D, flip, flop);
                clijx.copySlice(flop, dst3D, z);
            } else {
                clijx.subtractImages(src2D, flop, flip);
                clijx.copySlice(flop, dst3D, z);
            }
        }
        if (flipMinKernel != null) {
            flipMinKernel.close();
        }
        if (flopMinKernel != null) {
            flopMinKernel.close();
        }
        if (flipMaxKernel != null) {
            flipMaxKernel.close();
        }
        if (flopMaxKernel != null) {
            flopMaxKernel.close();
        }
        flip.close();
        flop.close();
        src2D.close();

        return true;
    }


    @Override
    public String getParameterHelpText() {
        return "Image input, Image destination, Number iterations";
    }

    @Override
    public String getDescription() {
        return "Applies a minimum filter with kernel size 3x3 n times to an image iteratively. " +
                "Odd iterations are done with box neighborhood, even iterations with a diamond. " +
                "Thus, with n > 2, the filter shape is an octagon. The given number of iterations - 2 makes the filter " +
                "result very similar to minimum sphere.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
