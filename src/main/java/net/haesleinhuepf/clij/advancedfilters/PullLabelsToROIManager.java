package net.haesleinhuepf.clij.advancedfilters;


import ij.gui.Roi;
import ij.plugin.frame.RoiManager;
import net.haesleinhuepf.clij.advancedmath.Equal;
import net.haesleinhuepf.clij.advancedmath.EqualConstant;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.exceptions.ClearCLUnknownArgumentNameException;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_pullLabelsToROIManager")
public class PullLabelsToROIManager extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image binary_input";
    }

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();

        RoiManager rm = RoiManager.getInstance();
        if (rm == null) {
            rm = new RoiManager();
        }
        ClearCLBuffer labelMap = (ClearCLBuffer) args[0];
        ClearCLBuffer binary = clij.create(labelMap);
        int numberOfLabels = (int) clij.op().maximumOfAllPixels(labelMap);
        for (int i = 1; i < numberOfLabels; i++) {
            EqualConstant.equalConstant(clij, labelMap, binary, new Float(i));
            Roi roi = PullAsROI.pullAsROI(clij, binary);
            rm.addRoi(roi);
        }
        binary.close();

        releaseBuffers(args);
        return true;
    }

    @Override
    public String getDescription() {
        return "Pulls all labels in a label map as ROIs to the ROI manager.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }
}
