package net.haesleinhuepf.clij2.plugins;


import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.Roi;
import ij.process.ImageProcessor;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_pullToCurrentZSlice")
public class PullToCurrentZSlice extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image input_slice, String target_image_stack";
    }

    @Override
    public boolean executeCL() {
        ClearCLBuffer slice = (ClearCLBuffer) (args[0]);
        ImagePlus imp = WindowManager.getImage((String)args[1]);
        pullToCurrentZSlice(getCLIJ2(), slice, imp);
        return true;
    }

    public static void pullToCurrentZSlice(CLIJ2 clij2, ClearCLBuffer input, ImagePlus target) {
        ImageProcessor ip = target.getProcessor();
        if (input.getWidth() == target.getWidth() && input.getHeight() == target.getHeight()) {
            if (input.getNativeType() == NativeTypeEnum.Float && ip.getBitDepth() == 32) {
                input.writeTo(FloatBuffer.wrap((float[]) ip.getPixels()), true);
                target.updateAndDraw();
                return;
            } else if (input.getNativeType() == NativeTypeEnum.UnsignedShort && ip.getBitDepth() == 16) {
                input.writeTo(ShortBuffer.wrap((short[]) ip.getPixels()), true);
                target.updateAndDraw();
                return;
            } else if (input.getNativeType() == NativeTypeEnum.UnsignedByte && ip.getBitDepth() == 8) {
                input.writeTo(ByteBuffer.wrap((byte[]) ip.getPixels()), true);
                target.updateAndDraw();
                return;
            }
        }

        ClearCLBuffer currentSlice = clij2.pushCurrentSlice(target);
        clij2.paste(input, currentSlice, 0, 0);
        ImagePlus imp = clij2.pull(currentSlice);
        target.setProcessor(imp.getProcessor());
        currentSlice.close();
    }

    @Override
    public String getDescription() {
        return "Pulls a two dimenstion image from the GPU memory and enters it in the current/given image stack at the selected slice.\n\n" +
                "If image type and/or dimensions don't fit a paste operation will be performed in the background, this may limit performance.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }
}
