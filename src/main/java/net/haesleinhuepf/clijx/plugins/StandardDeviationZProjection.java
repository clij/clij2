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

/**
 * Author: @haesleinhuepf
 * June 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_standardDeviationZProjection")
public class StandardDeviationZProjection extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        boolean result = standardDeviationZProjection(getCLIJx(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]));
        return result;
    }

    public static boolean standardDeviationZProjection(CLIJx clijx, ClearCLImageInterface input, ClearCLImageInterface output) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", input);
        parameters.put("dst", output);

        clijx.execute(StandardDeviationZProjection.class, "standard_deviation_z_projection_x.cl", "standard_deviation_z_projection", output.getDimensions(), output.getDimensions(), parameters);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        return getCLIJx().create(new long[]{input.getWidth(), input.getHeight()}, input.getNativeType());
    }

    @Override
    public String getDescription() {
        return "Determines the standard deviation projection of an image stack along Z.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }

}
