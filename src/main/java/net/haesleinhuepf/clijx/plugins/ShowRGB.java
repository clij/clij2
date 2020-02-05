package net.haesleinhuepf.clijx.plugins;

import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.WindowManager;
import ij.plugin.HyperStackConverter;
import ij.plugin.RGBStackConverter;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 *         November 2019
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_showRGB")
public class ShowRGB extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {

        return showRGB(clij, (ClearCLBuffer) args[0], (ClearCLBuffer)args[1], (ClearCLBuffer)args[2], (String)args[3]);
    }

    public static boolean showRGB(CLIJ clij, ClearCLBuffer inputR, ClearCLBuffer inputG, ClearCLBuffer inputB, String name) {
        ImagePlus impR = clij.pull(inputR);
        ImageStack stack = new ImageStack(impR.getWidth(), impR.getHeight());
        IJ.run(impR, "Enhance Contrast", "saturated=0.35");
        IJ.run(impR, "8-bit", "");
        stack.addSlice(impR.getProcessor());
        int channels = 1;

        ImagePlus impG = clij.pull(inputG);
        if (impG != null) {
            IJ.run(impG, "Enhance Contrast", "saturated=0.35");
            IJ.run(impG, "8-bit", "");
            stack.addSlice(impG.getProcessor());
            channels ++;
        }
        ImagePlus impB = clij.pull(inputB);
        if (impB != null) {
            IJ.run(impB, "Enhance Contrast", "saturated=0.35");
            IJ.run(impB, "8-bit", "");
            stack.addSlice(impB.getProcessor());
            channels ++;
        }


        ImagePlus tempImp = new ImagePlus("temp", stack);
        tempImp = HyperStackConverter.toHyperStack(tempImp, channels, 1, 1);
        /*if (tempImp.getNChannels() == 2) {
            tempImp.setC(1);
            IJ.run(tempImp, "Magenta", "");
            tempImp.setC(2);
            tempImp.setDisplayRange(0, 1);
        }*/
        RGBStackConverter.convertToRGB(tempImp);

        ImagePlus resultImp = WindowManager.getImage(name);
        if (resultImp == null) {
            resultImp = tempImp;
            resultImp.setTitle(name);
            resultImp.show();
        } else {
            resultImp.setProcessor(tempImp.getProcessor());
            resultImp.updateAndDraw();
        }
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image red, Image green, Image blue, String title";
    }

    @Override
    public String getDescription() {
        return "Visualises three 2D images as one RGB image";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }
}
