package net.haesleinhuepf.clijx.advancedfilters;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_downsample2D")
public class Downsample2D extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        float downsampleX = asFloat(args[2]);
        float downsampleY = asFloat(args[3]);

        return downsample(getCLIJx(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), downsampleX, downsampleY);
    }

    public static boolean downsample2D(CLIJx clijx, ClearCLImageInterface src, ClearCLImageInterface dst, Float factorX, Float factorY) {
        return downsample(clijx, src, dst, factorX, factorY);
    }

    public static boolean downsample(CLIJx clijx, ClearCLImageInterface src, ClearCLImageInterface dst, Float factorX, Float factorY) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("factor_x", 1.f / factorX);
        parameters.put("factor_y", 1.f / factorY);
        clijx.execute(Downsample2D.class, "downsample_2d_x.cl", "downsample_2d", dst.getDimensions(), dst.getDimensions(), parameters);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number factorX, Number factorY";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        float downsampleX = asFloat(args[2]);
        float downsampleY = asFloat(args[3]);

        return getCLIJx().create(new long[]{(long)(input.getWidth() * downsampleX), (long)(input.getHeight() * downsampleY)}, input.getNativeType());
    }

    @Override
    public String getDescription() {
        return "Scales an image using given scaling factors for X and Y dimensions. The nearest-neighbor method\n" +
                "is applied. In ImageJ the method which is similar is called 'Interpolation method: none'.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }
}
