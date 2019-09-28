package net.haesleinhuepf.clij.advancedfilters;


import ij.IJ;
import ij.ImagePlus;
import ij.gui.ImageRoi;
import ij.gui.Roi;
import ij.plugin.Selection;
import ij.plugin.Thresholder;
import ij.process.ImageProcessor;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.imagej.roi.MaskToRAConverter;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_pullAsROI")
public class PullAsROI extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image binary_input";
    }

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();
        Roi result = pullAsROI(clij, (ClearCLBuffer) (args[0]));
        ImagePlus imp = IJ.getImage();
        if (imp != null) {
            imp.setRoi(result);
        }
        releaseBuffers(args);
        return true;
    }

    public static Roi pullAsROI(CLIJ clij, ClearCLBuffer input) {
        ImagePlus imp = clij.pullBinary(input);
        ImageProcessor ip = imp.getProcessor();
        ip.setThreshold(127, 256, ImageProcessor.NO_LUT_UPDATE);
        IJ.runPlugIn(imp, "ij.plugin.filter.ThresholdToSelection", "");
        return imp.getRoi();
    }

    @Override
    public String getDescription() {
        return "Pulls a binary image from the GPU memory and puts it on the currently active ImageJ window.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }
}
