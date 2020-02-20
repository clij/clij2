package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;
import static net.haesleinhuepf.clij2.utilities.CLIJUtilities.checkDimensions;

/**
 * Author: @haesleinhuepf
 * February 2020
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_imageToStack")
public class ImageToStack extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        return imageToStack(getCLIJ2(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asInteger(args[2]));
    }

    public static boolean imageToStack(CLIJ2 clij2, ClearCLBuffer src, ClearCLBuffer dst, Integer numSlices) {
        assertDifferent(src, dst);

        ClearCLBuffer buffer = src;
        if (src.getDimension() == 3) {
            buffer = clij2.create(new long[]{src.getWidth(), src.getHeight()}, src.getNativeType());
            clij2.copySlice(src, buffer, 0);
        }

        for (int i = 0; i < numSlices; i++) {
            clij2.copySlice(buffer, dst, i);
        }

        if (buffer != src) {
            clij2.release(buffer);
        }

        return true;
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        int num_slices = asInteger(args[2]);
        return getCLIJ2().create(new long[]{input.getWidth(), input.getHeight(), num_slices}, input.getNativeType());
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number num_slices";
    }

    @Override
    public String getDescription() {
        return "Copies a single slice into a stack a given number of times.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D -> 3D";
    }
}
