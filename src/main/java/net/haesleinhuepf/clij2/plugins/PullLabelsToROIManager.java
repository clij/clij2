package net.haesleinhuepf.clij2.plugins;


import ij.gui.Roi;
import ij.plugin.frame.RoiManager;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_pullLabelsToROIManager")
public class PullLabelsToROIManager extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image binary_input";
    }

    @Override
    public boolean executeCL() {
        CLIJ2 clij2 = getCLIJ2();
        return pullLabelsToROIManager(clij2, (ClearCLBuffer) args[0]);
    }




    public static boolean pullLabelsToROIManager(CLIJ2 clij2, ClearCLBuffer labelMap ) {
        RoiManager rm = RoiManager.getInstance();
        if (rm == null) {
            rm = new RoiManager();
        }

        return pullLabelsToROIManager(clij2, labelMap, rm);
    }

    public static boolean pullLabelsToROIManager(CLIJ2 clij2, ClearCLBuffer labelMap, RoiManager roiManager ) {

        ClearCLBuffer binary = clij2.create(labelMap);
        int numberOfLabels = (int) clij2.maximumOfAllPixels(labelMap);
        for (int i = 1; i < numberOfLabels; i++) {
            EqualConstant.equalConstant(clij2, labelMap, binary, new Float(i));
            Roi roi = PullAsROI.pullAsROI(clij2, binary);
            roiManager.addRoi(roi);
        }
        clij2.release(binary);
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
