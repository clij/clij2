package net.haesleinhuepf.clij2.plugins;


import ij.IJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

import java.io.File;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_saveAsTIF")
public class SaveAsTIF extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image input, String filename";
    }

    @Override
    public boolean executeCL() {
        saveAsTIF(getCLIJ2(), (ClearCLBuffer) (args[0]), args[1].toString());
        return true;
    }

    public static boolean saveAsTIF(CLIJ2 clij2, ClearCLBuffer input, String filename) {
        ImagePlus imp = clij2.pull(input);
        new File(filename).getParentFile().mkdirs();
        IJ.saveAsTiff(imp, filename);
        return new File(filename).exists();
    }

    @Override
    public String getDescription() {
        return "Pulls an image from the GPU memory and saves it as TIF to disc.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
