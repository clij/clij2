package net.haesleinhuepf.clij.advancedfilters;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_paste2D")
public class Paste2D extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();
        boolean result = paste(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asInteger(args[2]), asInteger(args[3]));
        releaseBuffers(args);
        return result;
    }

    public static boolean paste(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer destination_x, Integer destination_y) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("destination_x", destination_x);
        parameters.put("destination_y", destination_y);
        parameters.put("dst", dst);
        return clij.execute(Paste2D.class, "paste.cl", "paste_2d", src.getDimensions(),parameters);
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number destinationX, Number destinationY";
    }


    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        int x = asInteger(args[2]);
        int y = asInteger(args[3]);
        int z = asInteger(args[4]);

        return clij.createCLBuffer(new long[]{input.getWidth() + x, input.getHeight() + y, input.getDepth() + z}, input.getNativeType());
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
