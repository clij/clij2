package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;

/**
 * Author: @haesleinhuepf
 * July 2020
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_zPositionOfMaximumZProjection")
public class ZPositionOfMaximumZProjection extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Image";
    }

    @Override
    public String getOutputType() {
        return "Image";
    }

    @Override
    public boolean executeCL() {
        return zPositionOfMaximumZProjection(getCLIJ2() ,(ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]));
    }

    public static boolean zPositionOfMaximumZProjection(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst_arg) {
        assertDifferent(src, dst_arg);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst_arg", dst_arg);

        clij2.execute(ZPositionOfMaximumZProjection.class, "z_position_of_maximum_z_projection_x.cl", "z_position_of_maximum_z_projection", dst_arg.getDimensions(), dst_arg.getDimensions(), parameters);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Image destination";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        return getCLIJ2().create(new long[]{input.getWidth(), input.getHeight()}, input.getNativeType());
    }

    @Override
    public String getDescription() {
        return "Determines a Z-position of the maximum intensity along Z and writes it into the resulting image.\n\n" +
                "If there are multiple z-slices with the same value, the smallest Z will be chosen.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D -> 2D";
    }

    @Override
    public String getCategories() {
        return "Projection";
    }
}
