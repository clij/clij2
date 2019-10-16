package net.haesleinhuepf.clij.advancedfilters;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_localExtremaBox")
public class LocalExtremaBox extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image input, Image destination, Number radiusX, Number radiusY, Number radiusZ";
    }

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();
        boolean result = localExtremaBox(clij, (ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]), asInteger(args[2]), asInteger(args[3]), asInteger(args[4]));
        releaseBuffers(args);
        return result;
    }

    public static boolean localExtremaBox(CLIJ clij, ClearCLBuffer input, ClearCLBuffer destination, Integer radiusX, Integer radiusY, Integer radiusZ) {
        ClearCLBuffer temp1 = clij.create(input);
        ClearCLBuffer temp2 = clij.create(input);

        clij.op().minimumBox(input, temp1, radiusX, radiusY, radiusZ);
        clij.op().maximumBox(input, temp2, radiusX, radiusY, radiusZ);

        boolean result = Extrema.extrema(clij, temp1, temp2, destination);
        temp1.close();
        temp2.close();
        return result;
    }

    @Override
    public String getDescription() {
        return "Applies a local minimum and maximum filter. Afterwards, the value is returned which is more far from zero.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
