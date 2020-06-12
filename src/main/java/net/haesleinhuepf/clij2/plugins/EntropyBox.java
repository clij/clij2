package net.haesleinhuepf.clij2.plugins;


import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.HasAuthor;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_entropyBox")
public class EntropyBox extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, HasAuthor, IsCategorized {
    @Override
    public String getCategories() {
        return "Filter";
    }

    @Override
    public String getParameterHelpText() {
        return "Image input, ByRef Image destination, Number radiusX, number radiusY, Number radiusZ";
    }

    @Override
    public boolean executeCL() {
        ClearCLBuffer input = (ClearCLBuffer) (args[0]);
        ClearCLBuffer output = (ClearCLBuffer) (args[1]);
        int radiusX = asInteger(args[2]);
        int radiusY = asInteger(args[3]);
        int radiusZ = asInteger(args[4]);

        boolean result = getCLIJ2().entropyBox(input, output, radiusX, radiusY, radiusZ);
        return result;
    }

    public static boolean entropyBox(CLIJ2 clij2, ClearCLBuffer src, ClearCLBuffer dst, Integer radiusX, Integer radiusY, Integer radiusZ) {
        float minIntensity = (float) clij2.minimumOfAllPixels(src);
        float maxIntensity = (float) clij2.maximumOfAllPixels(src);
        return entropyBox(clij2, src, dst, radiusX, radiusY, radiusZ, minIntensity, maxIntensity);
    }

    public static boolean entropyBox(CLIJ2 clij2, ClearCLBuffer src, ClearCLBuffer dst, Integer radiusX, Integer radiusY, Integer radiusZ, Float minIntensity, Float maxIntensity ) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("radiusX", radiusX);
        parameters.put("radiusY", radiusY);
        if (src.getDimension() == 3) {
            parameters.put("radiusZ", radiusZ);
        }

        parameters.put("minIntensity", minIntensity);
        parameters.put("maxIntensity", maxIntensity);
        clij2.execute(EntropyBox.class, "entropy_" + src.getDimension() + "d_x.cl", "entropy_" + src.getDimension() + "d", dst.getDimensions(), dst.getDimensions(), parameters);
        return true;
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        return getCLIJ2().create(input.getDimensions(), NativeTypeEnum.Float);
    }

    @Override
    public String getDescription() {
        return "Determines the local entropy in a box with a given radius around every pixel.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public String getAuthorName() {
        return "Pit Kludig and Robert Haase";
    }
}
