package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_localThreshold")
public class LocalThreshold extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        return getCLIJ2().localThreshold((ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), (ClearCLBuffer)(args[2]));
    }

    public static boolean localThreshold(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface threshold, ClearCLImageInterface dst) {
        assertDifferent(src, dst);
        assertDifferent(threshold, dst);

        return clij2.greaterOrEqual(src, threshold, dst);
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image localThreshold, ByRef Image destination";
    }

    @Override
    public String getDescription() {
        return "Computes a binary image with pixel values 0 and 1 depending on if a pixel value x in image X \n" +
                "was above of equal to the pixel value m in mask image M.\n\n" +
                "<pre>f(x) = (1 if (x >=  m)); (0 otherwise)</pre>";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
