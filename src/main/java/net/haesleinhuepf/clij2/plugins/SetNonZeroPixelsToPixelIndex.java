package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_setNonZeroPixelsToPixelIndex")
public class SetNonZeroPixelsToPixelIndex extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        return setNonZeroPixelsToPixelIndex(getCLIJ2(), (ClearCLBuffer)args[0], (ClearCLBuffer)args[1]);
    }


    public static boolean setNonZeroPixelsToPixelIndex(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst) {
        HashMap<String, Object> parameters = new HashMap<>();

        parameters.clear();
        parameters.put("src", src);
        parameters.put("dst", dst);
        clij2.execute(SetNonZeroPixelsToPixelIndex.class, "set_nonzero_pixels_to_pixelindex_x.cl", "set_nonzero_pixels_to_pixelindex", dst.getDimensions(), dst.getDimensions(), parameters);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination";
    }

    @Override
    public String getDescription() {
        return "Sets all pixels in an image which are not zero to the index of the pixel. This can be used for Connected Components Analysis.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
