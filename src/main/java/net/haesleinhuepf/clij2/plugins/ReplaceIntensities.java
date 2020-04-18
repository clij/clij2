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

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_replaceIntensities")
public class ReplaceIntensities extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image input, Image new_values_vector, ByRef Image destination";
    }

    @Override
    public boolean executeCL() {
        boolean result = replaceIntensities(getCLIJ2(), (ClearCLBuffer) (args[0]),(ClearCLBuffer) (args[1]), (ClearCLBuffer) (args[2]));
        return result;
    }

    public static boolean replaceIntensities(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface map, ClearCLImageInterface dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.clear();
        parameters.put("src", src);
        parameters.put("map", map);
        parameters.put("dst", dst);

        long[] dims = dst.getDimensions();

        clij2.activateSizeIndependentKernelCompilation();
        clij2.execute(ReplaceIntensities.class, "replace_intensities_x.cl", "replace_intensities", dims, dims, parameters);
        return true;
    }

    @Override
    public String getDescription() {
        return "Replaces integer intensities specified in a vector image. \n\nThe vector image must be 3D with size (m, 1, 1) " +
                "where m corresponds to the maximum intensity in the original image. Assuming the vector image contains values (0, 1, 0, 2) means: \n" +
                " * All pixels with value 0 (first entry in the vector image) get value 0\n" +
                " * All pixels with value 1 get value 1\n" +
                " * All pixels with value 2 get value 0\n" +
                " * All pixels with value 3 get value 2\n";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
