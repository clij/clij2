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

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_downsample2D")
public class Downsample2D extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        float downsampleX = asFloat(args[2]);
        float downsampleY = asFloat(args[3]);

        return downsample(getCLIJ2(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), downsampleX, downsampleY);
    }

    @Deprecated
    public static boolean downsample2D(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst, Float factorX, Float factorY) {
        return downsample(clij2, src, dst, factorX, factorY);
    }

    @Deprecated
    public static boolean downsample(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst, Float factorX, Float factorY) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("factor_x", 1.f / factorX);
        parameters.put("factor_y", 1.f / factorY);
        clij2.execute(Downsample2D.class, "downsample_2d_x.cl", "downsample_2d", dst.getDimensions(), dst.getDimensions(), parameters);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Image destination, Number factorX, Number factorY";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        float downsampleX = asFloat(args[2]);
        float downsampleY = asFloat(args[3]);

        return getCLIJ2().create(new long[]{(long)(input.getWidth() * downsampleX), (long)(input.getHeight() * downsampleY)}, input.getNativeType());
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
