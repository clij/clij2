package net.haesleinhuepf.clij.advancedfilters;


import ij.IJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_readImageFromDisc")
public class ReadImageFromDisc extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    private static String lastLoadedFilename = "";
    private static ImagePlus lastLoadedImage = null;

    @Override
    public String getParameterHelpText() {
        return "Image destination, String filename";
    }

    @Override
    public boolean executeCL() {
        ClearCLBuffer output = (ClearCLBuffer) (args[0]);
        String filename = args[1].toString();

        //readFromDisc(clij, output, filename);
        return true;
    }


    public static ClearCLBuffer readImageFromDisc(CLIJ clij, String filename) {
        if (filename.compareTo(lastLoadedFilename) == 0 && lastLoadedImage != null) {
        } else {
            lastLoadedFilename = filename;
            lastLoadedImage = IJ.openImage(filename);
        }
        return clij.push(lastLoadedImage);
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        String filename = args[1].toString();
        return readImageFromDisc(clij, filename);
    }

    @Override
    public String getDescription() {
        return "Read an image from disc.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }


}
