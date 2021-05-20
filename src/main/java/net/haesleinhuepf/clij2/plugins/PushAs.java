package net.haesleinhuepf.clij2.plugins;

import ij.ImagePlus;
import ij.WindowManager;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJHandler;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import org.scijava.plugin.Plugin;

/**
 * Release
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_pushAs")
public class PushAs extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        String source_name = (String)args[0];
        String target_name = (String)args[1];

        if (WindowManager.getImage(source_name) == null) {
            //Macro.abort();
            throw new IllegalArgumentException("You tried to push the image '" + source_name + "' to the GPU.\n" +
                    "However, this image doesn't exist.");
        } else {
            ImagePlus imp = WindowManager.getImage(source_name);
            imp.changes = false;

            ClearCLBuffer temp = CLIJ.getInstance().push(imp);
            CLIJHandler.getInstance().pushInternal(temp, target_name);
            //.pushToGPU((String) args[0]);
        }
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "String image, String new_name";
    }

    @Override
    public String getDescription() {
        return "Copies an image specified to GPU memory and gives it a new name in order to process it there later.\n\n" +
                "This function is only available via ImageJ Macro.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
