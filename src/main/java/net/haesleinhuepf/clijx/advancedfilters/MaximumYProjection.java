package net.haesleinhuepf.clijx.advancedfilters;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_maximumYProjection")
public class MaximumYProjection extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();
        boolean result = maximumYProjection(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]));
        releaseBuffers(args);
        return result;
    }

    public static boolean maximumYProjection(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst_max) {
        assertDifferent(src, dst_max);
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst_max", dst_max);
        return clij.execute(ResliceRadial.class, "maximum_y_projection.cl", "maximum_y_projection", parameters);
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination_max";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        return clij.createCLBuffer(new long[]{input.getWidth(), input.getDepth()}, input.getNativeType());
    }

    @Override
    public String getDescription() {
        return "Determines the maximum projection of an image along X.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }

}
