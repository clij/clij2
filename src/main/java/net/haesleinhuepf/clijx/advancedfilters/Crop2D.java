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
import net.haesleinhuepf.clij.macro.modules.Crop3D;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_crop2D")
public class Crop2D extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        return crop(getCLIJx(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asInteger(args[2]), asInteger(args[3]));
    }


    public static boolean crop2D(CLIJ clij, ClearCLBuffer input, ClearCLBuffer output, Integer startX, Integer startY, Integer width, Integer height) {
        net.haesleinhuepf.clij.macro.modules.Crop2D crop2D = new net.haesleinhuepf.clij.macro.modules.Crop2D();
        crop2D.setClij(clij);
        crop2D.setArgs(new Object[]{input, output, startX, startY, width, height});
        return crop2D.executeCL();
    }



    public static boolean crop2D(CLIJx clijx, ClearCLImageInterface src, ClearCLImageInterface dst, Integer startX, Integer startY) {
        return crop(clijx, src, dst, startX, startY);
    }

    public static boolean crop(CLIJx clijx, ClearCLImageInterface src, ClearCLImageInterface dst, Integer startX, Integer startY) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("start_x", startX);
        parameters.put("start_y", startY);
        clijx.execute(Crop2D.class, "crop_2d_x.cl", "crop_2d", dst.getDimensions(), dst.getDimensions(), parameters);
        return true;
    }


    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number startX, Number startY, Number width, Number height";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        int width = asInteger(args[4]);
        int height = asInteger(args[5]);

        return clij.createCLBuffer(new long[]{width, height}, input.getNativeType());
    }

    @Override
    public String getDescription() {
        return "Crops a given rectangle out of a given image.\n\n" +
                "Note: If the destination image pre-exists already, it will be overwritten and keep it's dimensions.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }

}
