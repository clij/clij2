package net.haesleinhuepf.clijx.base;

import fiji.util.gui.GenericDialogPlus;
import ij.IJ;
import ij.Macro;
import ij.WindowManager;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJHandler;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

/**
 * Release
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_pushCurrentZStack")
public class PushCurrentZStack extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        if (WindowManager.getImage((String)args[0]) == null) {
            GenericDialogPlus gd = new GenericDialogPlus("CLIJ_pushCurrentZStack() Error");
            gd.addMessage("You tried to push the image '" + args[0] + "' to the GPU.\n" +
                    "However, this image doesn't exist. Please choose another one.");

            gd.addImageChoice("Image", IJ.getImage().getTitle());
            gd.showDialog();

            if (gd.wasCanceled()) {
                Macro.abort();
            } else {
                CLIJHandler.getInstance().pushCurrentZStackToGPU(gd.getNextImage().getTitle());
            }
        } else {
            CLIJHandler.getInstance().pushCurrentZStackToGPU((String) args[0]);
        }
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "String image";
    }

    @Override
    public String getDescription() {
        return "Copies an image specified by its name to GPU memory in order to process it there later.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
