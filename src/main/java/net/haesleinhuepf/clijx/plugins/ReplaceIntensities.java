package net.haesleinhuepf.clijx.plugins;


import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_replaceIntensities")
public class ReplaceIntensities extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image input, Image new_values_vector, Image destination";
    }

    @Override
    public boolean executeCL() {
        boolean result = replaceIntensities(getCLIJx(), (ClearCLBuffer) (args[0]),(ClearCLBuffer) (args[1]), (ClearCLBuffer) (args[2]));
        return result;
    }

    public static boolean replaceIntensities(CLIJx clijx, ClearCLImageInterface src, ClearCLImageInterface map, ClearCLImageInterface dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.clear();
        parameters.put("src", src);
        parameters.put("map", map);
        parameters.put("dst", dst);

        long[] dims = dst.getDimensions();

        clijx.activateSizeIndependentKernelCompilation();
        clijx.execute(ConnectedComponentsLabeling.class, "replace_intensities_" + dst.getDimension() +  "d_x.cl", "replace_intensities_"  +dst.getDimension() + "d", dims, dims, parameters);
        return true;
    }

    @Override
    public String getDescription() {
        return "Replaces integer intensities specified in a vector image. The vector image must be 3D with size (m, 1, 1) " +
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
