package net.haesleinhuepf.clijx.advancedfilters;

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

import javax.swing.*;

/**
 * Author: @haesleinhuepf
 *         November 2019
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_showGrey")
public class ShowGrey extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {

        return showGrey(clij, (ClearCLBuffer) args[0], (String)args[1]);
    }

    public static boolean showGrey(CLIJ clij, ClearCLBuffer input, String name) {
        final ImagePlus imp = clij.pull(input);
        SwingUtilities.invokeLater(() -> {
            IJ.run(imp, "Enhance Contrast", "saturated=0.35");

            ImagePlus resultImp = WindowManager.getImage(name);
            if (resultImp == null) {
                resultImp = imp;
                resultImp.setTitle(name);
                resultImp.show();
            } else {
                resultImp.setProcessor(imp.getProcessor());
                resultImp.updateAndDraw();
            }
            resultImp.show();
        });
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image input, String title";
    }

    @Override
    public String getDescription() {
        return "Visualises a single 2D image.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }
}
