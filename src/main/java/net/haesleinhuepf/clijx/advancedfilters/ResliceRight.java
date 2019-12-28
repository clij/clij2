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

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_resliceRight")
public class ResliceRight extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        return resliceRight(getCLIJx(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]));
    }


    public static boolean resliceRight(CLIJx clijx, ClearCLImageInterface src, ClearCLImageInterface dst) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);

        clijx.execute(ResliceRight.class, "reslice_right_3d_x.cl", "reslice_right_3d", dst.getDimensions(), dst.getDimensions(), parameters);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        return clij.createCLBuffer(new long[]{input.getHeight(), input.getDepth(), input.getWidth()}, input.getNativeType());
    }

    @Override
    public String getDescription() {
        return "Flippes X, Y and Z axis of an image stack. This operation is similar to ImageJs 'Reslice [/]' method \n" +
                " but offers less flexibility such as interpolation.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }
}
