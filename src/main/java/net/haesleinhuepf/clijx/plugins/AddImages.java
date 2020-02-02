package net.haesleinhuepf.clijx.plugins;

import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

import static net.haesleinhuepf.clij2.plugins.AddImagesWeighted.addImagesWeighted;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_addImages")
public class AddImages extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        return addImages(getCLIJx(), (ClearCLImageInterface)( args[0]), (ClearCLImageInterface)(args[1]), (ClearCLImageInterface)(args[2]));
    }

    public static boolean addImages(CLIJx clijx, ClearCLImageInterface src, ClearCLImageInterface src1, ClearCLImageInterface dst) {
        return addImagesWeighted(clijx, src, src1, dst, 1f, 1f);
    }

    @Override
    public String getParameterHelpText() {
        return "Image summand1, Image summand2, Image destination";
    }

    @Override
    public String getDescription() {
        return "Calculates the sum of pairs of pixels x and y of two images X and Y.\n\n<pre>f(x, y) = x + y</pre>";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
