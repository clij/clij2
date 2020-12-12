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

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_paste2D")
public class Paste2D extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
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
        return new Object[]{null, null, 0, 0};
    }

    @Override
    public boolean executeCL() {
        boolean result = getCLIJ2().paste((ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asInteger(args[2]), asInteger(args[3]));
        return result;
    }

    public static boolean paste2D(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst, Integer destination_x, Integer destination_y) {
        return paste(clij2, src, dst, destination_x, destination_y);
    }

    public static boolean paste(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst, Integer destination_x, Integer destination_y) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("destination_x", destination_x);
        parameters.put("destination_y", destination_y);
        if (src.getDimension() == 3) {
            parameters.put("destination_z", 0);
        }
        parameters.put("dst", dst);
        clij2.execute(Paste2D.class, "paste_" + src.getDimension() + "d_x.cl", "paste_" + src.getDimension() + "d", src.getDimensions(), src.getDimensions(), parameters);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Image destination, Number destination_x, Number destination_y";
    }


    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        int x = asInteger(args[2]);
        int y = asInteger(args[3]);

        return clij.createCLBuffer(new long[]{input.getWidth() + x, input.getHeight() + y}, input.getNativeType());
    }

    @Override
    public String getDescription() {
        return "Pastes an image into another image at a given position.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }
}
