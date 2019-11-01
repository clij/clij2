package net.haesleinhuepf.clij.advancedfilters;


import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
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

        clijx.execute(ConnectedComponentsLabeling.class, "replace_intensities_" + dst.getDimension() +  "d_x.cl", "replace_intensities_"  +dst.getDimension() + "d", dims, dims, parameters);
        return true;
    }

    @Override
    public String getDescription() {
        return "Replaces specific intensities specified in a vector image with given new values specified in a vector image.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
