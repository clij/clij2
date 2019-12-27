package net.haesleinhuepf.clijx.advancedfilters;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
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
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_crop3D")
public class Crop3D extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        return crop(getCLIJx(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asInteger(args[2]), asInteger(args[3]), asInteger(args[4]));
    }

    public static boolean crop3D(CLIJx clijx, ClearCLImageInterface src, ClearCLImageInterface dst, Integer startX, Integer startY, Integer startZ) {
        return crop(clijx, src, dst, startX, startY, startZ);
    }

    public static boolean crop(CLIJx clijx, ClearCLImageInterface src, ClearCLImageInterface dst, Integer startX, Integer startY, Integer startZ) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("start_x", startX);
        parameters.put("start_y", startY);
        parameters.put("start_z", startZ);
        clijx.execute(Crop3D.class, "crop_3d_x.cl", "crop_3d", dst.getDimensions(), dst.getDimensions(), parameters);
        return true;
    }



    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number startX, Number startY, Number startZ, Number width, Number height, Number depth";
    }


    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        int width = asInteger(args[5]);
        int height = asInteger(args[6]);
        int depth = asInteger(args[7]);

        return clij.createCLBuffer(new long[]{width, height, depth}, input.getNativeType());
    }

    @Override
    public String getDescription() {
        return "Crops a given sub-stack out of a given image stack.\n\n" +
                "Note: If the destination image pre-exists already, it will be overwritten and keep it's dimensions.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }
}
