package net.haesleinhuepf.clijx.plugins;

import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

import static net.haesleinhuepf.clijx.plugins.AddImagesWeighted.addImagesWeighted;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_subtractImages")
public class SubtractImages extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        return subtractImages(getCLIJx(), (ClearCLImageInterface)( args[0]), (ClearCLImageInterface)(args[1]), (ClearCLImageInterface)(args[2]));
    }

    @Deprecated
    public static boolean subtract(CLIJx clijx, ClearCLImageInterface src, ClearCLImageInterface src1, ClearCLImageInterface dst) {
        return subtractImages(clijx, src, src1, dst);
    }

    public static boolean subtractImages(CLIJx clijx, ClearCLImageInterface src, ClearCLImageInterface src1, ClearCLImageInterface dst) {
        return addImagesWeighted(clijx, src, src1, dst, 1f, -1f);
    }


    @Override
    public String getParameterHelpText() {
        return "Image subtrahend, Image minuend, Image destination";
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
