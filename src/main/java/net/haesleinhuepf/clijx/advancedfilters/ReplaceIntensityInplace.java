package net.haesleinhuepf.clijx.advancedfilters;


import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLKernel;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_replaceIntensityInplace")
public class ReplaceIntensityInplace extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image input_and_destination, Number oldValue, number newValue";
    }

    @Override
    public boolean executeCL() {
        boolean result = replaceIntensityInplace(getCLIJx(), (ClearCLBuffer) (args[0]), asFloat(args[1]), asFloat(args[2]));
        return result;
    }

    public static boolean replaceIntensityInplace(CLIJx clijx, ClearCLBuffer src_dst, Float oldValue, Float newValue) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.clear();
        parameters.put("src", src_dst);
        parameters.put("dst", src_dst);
        parameters.put("to_be_replaced", oldValue);
        parameters.put("replacement", newValue);
        long[] dimensions = src_dst.getDimensions();
        clijx.execute(ReplaceIntensityInplace.class, "replace_intensity_inplace_" + src_dst.getDimension() + "d_x.cl", "replace_intensity_inplace_3d", dimensions, dimensions, parameters);
        return true;
    }

    @Override
    public String getDescription() {
        return "Replaces a specific intensity in an image with a given new value.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    public static void main(String... args){
        CLIJx clijx = CLIJx.getInstance();
        ClearCLBuffer clearCLBuffer = clijx.create(new long[]{5, 5, 5});
        clijx.replaceIntensityInplace(clearCLBuffer, 3, 4);
    }
}
