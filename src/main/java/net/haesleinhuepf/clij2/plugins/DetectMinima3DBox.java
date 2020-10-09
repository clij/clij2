package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 *         May 2020
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_detectMinima3DBox")
public class DetectMinima3DBox extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
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
        return getCLIJ2().detectMinimaBox((ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asInteger(args[2]), asInteger(args[3]), asInteger(args[4]));
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Image destination, Number radiusX, Number radiusY, Number radiusZ";
    }

    @Override
    public String getDescription() {
        return "Detects local minima in a given square/cubic neighborhood. \n\n" +
                "Pixels in the resulting image are set to 1 if there is no other pixel in a given radius which has a \n" +
                "lower intensity, and to 0 otherwise.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public String getCategories() {
        return "Binary, Detection";
    }
}
