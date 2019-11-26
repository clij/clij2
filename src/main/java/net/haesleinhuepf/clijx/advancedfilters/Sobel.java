package net.haesleinhuepf.clijx.advancedfilters;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.utilities.HasAuthor;
import org.scijava.plugin.Plugin;
import java.lang.Math;
import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;

import java.util.HashMap;

/**
 *
 *
 * Author: @ruthhwj, @haesleinhuepf
 *         November 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_sobel")
public class Sobel extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, HasAuthor {

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();
        boolean result = convolveWithSobel(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]));
        releaseBuffers(args);
        return result;
    }

    public static boolean convolveWithSobel(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);

        return clij.execute(Sobel.class,
                "sobel.cl",
                "sobel_" + src.getDimension() + "d",
                parameters);
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination";
    }

    @Override
    public String getDescription() {
        return "Convolve the image with the Sobel kernel.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public String getAuthorName() {
        return "Ruth Whelan-Jeans";
    }
}