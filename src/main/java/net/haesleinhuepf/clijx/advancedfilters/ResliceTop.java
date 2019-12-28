package net.haesleinhuepf.clijx.advancedfilters;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_resliceTop")
public class ResliceTop extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        return resliceTop(getCLIJx(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]));
    }


    public static boolean resliceTop(CLIJx clijx, ClearCLImageInterface src, ClearCLImageInterface dst) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);

        clijx.execute(ResliceTop.class, "reslice_top_3d_x.cl", "reslice_top_3d", dst.getDimensions(), dst.getDimensions(), parameters);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        return clij.createCLBuffer(new long[]{input.getWidth(), input.getDepth(), input.getHeight()}, input.getNativeType());
    }

    @Override
    public String getDescription() {
        return "Flippes Y and Z axis of an image stack. This operation is similar to ImageJs 'Reslice [/]' method but\n" +
                "offers less flexibility such as interpolation.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }
}
