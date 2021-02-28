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
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_crop3D")
public class Crop3D extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Image";
    }

    @Override
    public String getOutputType() {
        return "Image";
    }

    @Override
    public String getCategories() {
        return "Transform";
    }

    @Override
    public Object[] getDefaultValues() {
        if (default_values != null) {
            return default_values;
        }
        return new Object[]{null, null, 0, 0, 0, 100, 100, 10};
    }

    @Override
    public boolean executeCL() {
        return getCLIJ2().crop((ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asInteger(args[2]), asInteger(args[3]), asInteger(args[4]));
    }

    public static boolean crop3D(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst, Integer startX, Integer startY, Integer startZ) {
        return crop(clij2, src, dst, startX, startY, startZ);
    }

    public static boolean crop(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst, Integer startX, Integer startY, Integer startZ) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("start_x", startX);
        parameters.put("start_y", startY);
        parameters.put("start_z", startZ);
        clij2.activateSizeIndependentKernelCompilation();
        clij2.execute(Crop3D.class, "crop_3d_x.cl", "crop_3d", dst.getDimensions(), dst.getDimensions(), parameters);
        return true;
    }



    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Image destination, Number start_x, Number start_y, Number start_z, Number width, Number height, Number depth";
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
        return "Crops a given sub-stack out of a given image stack. \n\n" +
                "Note: If the destination image pre-exists already, it will be overwritten and keep it's dimensions.\n\n"+
                "Parameters\n" +
                "----------\n" +
                "source : Image\n" +
                "    The image where a part will be cropped out.\n" +
                "destination : Image\n" +
                "    The cropped image will be stored in this variable.\n" +
                "start_x : Number\n" +
                "    The horizontal position of the region to crop in the source image.\n" +
                "start_y : Number\n" +
                "    The vertical position of the region to crop in the source image.\n" +
                "start_z : Number\n" +
                "    The slice position of the region to crop in the source image. Slices are counted 0-based; the first slice is z=0.\n" +
                "width : Number\n" +
                "    The width of the region to crop in the source image.\n" +
                "height : Number\n" +
                "    The height of the region to crop in the source image.\n" +
                "depth : Number\n" +
                "    The depth of the region to crop in the source image.\n";

    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }
}
