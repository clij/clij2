package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

import static net.haesleinhuepf.clij2.plugins.AddImagesWeighted.addImagesWeighted;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_subtractImages")
public class SubtractImages extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        return getCLIJ2().subtractImages((ClearCLImageInterface)( args[0]), (ClearCLImageInterface)(args[1]), (ClearCLImageInterface)(args[2]));
    }

    @Deprecated
    public static boolean subtract(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface src1, ClearCLImageInterface dst) {
        return subtractImages(clij2, src, src1, dst);
    }

    public static boolean subtractImages(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface src1, ClearCLImageInterface dst) {
        return addImagesWeighted(clij2, src, src1, dst, 1f, -1f);
    }


    @Override
    public String getParameterHelpText() {
        return "Image subtrahend, Image minuend, ByRef Image destination";
    }

    @Override
    public String getDescription() {
        return "Subtracts one image X from another image Y pixel wise.\n\n<pre>f(x, y) = x - y</pre>";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
