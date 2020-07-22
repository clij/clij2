package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_crop2D")
public class Crop2D extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized {
    @Override
    public String getCategories() {
        return "Transform";
    }

    @Override
    public Object[] getDefaultValues() {
        return new Object[]{null, null, 0, 0, 100, 100};
    }

    @Override
    public boolean executeCL() {
        return getCLIJ2().crop((ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asInteger(args[2]), asInteger(args[3]));
    }

    public static boolean crop2D(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst, Integer startX, Integer startY) {
        return crop(clij2, src, dst, startX, startY);
    }

    public static boolean crop(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst, Integer startX, Integer startY) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("start_x", startX);
        parameters.put("start_y", startY);
        clij2.activateSizeIndependentKernelCompilation();
        clij2.execute(Crop2D.class, "crop_2d_x.cl", "crop_2d", dst.getDimensions(), dst.getDimensions(), parameters);
        return true;
    }


    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Image destination, Number startX, Number startY, Number width, Number height";
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
        return "Crops a given rectangle out of a given image. \n\n" +
                "Note: If the destination image pre-exists already, it will be overwritten and keep it's dimensions.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }

}
