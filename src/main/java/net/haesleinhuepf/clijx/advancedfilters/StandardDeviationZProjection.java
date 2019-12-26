package net.haesleinhuepf.clijx.advancedfilters;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

/**
 * Author: @haesleinhuepf
 * June 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_standardDeviationZProjection")
public class StandardDeviationZProjection extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        boolean result = standardDeviationZProjection(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]));
        return result;
    }

    public static boolean standardDeviationZProjection(CLIJ clij, ClearCLImageInterface input, ClearCLImageInterface output) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", input);
        parameters.put("dst", output);

        return clij.execute(StandardDeviationZProjection.class, "standard_deviation_z_projection_3d_2d_x.cl", "standard_deviation_z_projection_3d_2d", parameters);
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        return clij.createCLBuffer(new long[]{input.getWidth(), input.getHeight()}, input.getNativeType());
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
