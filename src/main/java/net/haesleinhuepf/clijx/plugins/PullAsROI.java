package net.haesleinhuepf.clijx.plugins;


import ij.IJ;
import ij.ImagePlus;
import ij.gui.Roi;
import ij.process.ImageProcessor;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_pullAsROI")
public class PullAsROI extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image binary_input";
    }

    @Override
    public boolean executeCL() {
        Roi result = pullAsROI(getCLIJx(), (ClearCLBuffer) (args[0]));
        ImagePlus imp = IJ.getImage();
        if (imp != null) {
            imp.setRoi(result);
        }

        return true;
    }

    public static Roi pullAsROI(CLIJx clijx, ClearCLBuffer input) {
        ImagePlus imp = clijx.pullBinary(input);
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
