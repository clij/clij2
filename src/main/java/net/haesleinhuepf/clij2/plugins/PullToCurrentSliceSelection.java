package net.haesleinhuepf.clij2.plugins;


import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.Roi;
import ij.process.Blitter;
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
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_pullToCurrentSliceSelection")
public class PullToCurrentSliceSelection extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image input_slice, String target_image_stack";
    }

    @Override
    public boolean executeCL() {
        ClearCLBuffer slice = (ClearCLBuffer) (args[0]);
        ImagePlus imp = WindowManager.getImage((String)args[1]);
        pullToCurrentSliceSelection(getCLIJ2(), slice, imp);
        return true;
    }

    public static void pullToCurrentSliceSelection(CLIJ2 clij2, ClearCLBuffer input, ImagePlus target) {
        ClearCLBuffer currentSelection = clij2.pushCurrentSelection(target);
        clij2.paste(input, currentSelection, 0, 0);
        ImagePlus imp = clij2.pull(currentSelection);

        Roi roi = target.getRoi();
        if (roi == null) {
            target.getProcessor().copyBits(imp.getProcessor(), 0, 0, Blitter.COPY);
        } else {
            target.getProcessor().copyBits(imp.getProcessor(), roi.getBounds().x, roi.getBounds().y, Blitter.COPY);
        }

        currentSelection.close();
    }

    @Override
    public String getDescription() {
        return "Pulls a two dimensional image from the GPU memory and enters it in the current selection.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }
}
