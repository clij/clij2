package net.haesleinhuepf.clij.customconvolutionplugin;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;
import java.lang.Math;
import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;

import java.util.HashMap;

/**
 *
 *
 * Author: @haesleinhuepf
 * 12 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_convolve")
public class Sobel extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();
        boolean result = convolveWithSobel(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]));
        releaseBuffers(args);
        return result;
    }

    static boolean convolveWithSobel(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
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
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        return super.createOutputBufferFromSource((ClearCLBuffer)args[0]);
    }

}