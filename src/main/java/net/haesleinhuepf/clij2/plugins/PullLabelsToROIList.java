package net.haesleinhuepf.clij2.plugins;


import ij.IJ;
import ij.gui.Roi;
import ij.plugin.frame.RoiManager;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_pullLabelsToROIList")
public class PullLabelsToROIList extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized {

    @Override
    public String getParameterHelpText() {
        return "Image labelmap_input";
    }

    @Override
    public boolean executeCL() {
        CLIJ2 clij2 = getCLIJ2();
        ArrayList<Roi> list = getCLIJ2().pullLabelsToROIList((ClearCLBuffer) args[0]);
        for (Roi roi : list) {
            IJ.log("" + roi);
        }
        return true;
    }




    public static ArrayList<Roi> pullLabelsToROIList(CLIJ2 clij2, ClearCLBuffer labelMap ) {
        ArrayList<Roi> list = new ArrayList<>();
        pullLabelsToROIList(clij2, labelMap, list);
        return list;
    }

    public static boolean pullLabelsToROIList(CLIJ2 clij2, ClearCLBuffer labelMap, List<Roi> roiList ) {

        ClearCLBuffer binary = clij2.create(labelMap);
        int numberOfLabels = (int) clij2.maximumOfAllPixels(labelMap);
        for (int i = 1; i <= numberOfLabels; i++) {
            EqualConstant.equalConstant(clij2, labelMap, binary, new Float(i));
            Roi roi = PullAsROI.pullAsROI(clij2, binary);
            if (roi == null) {
                System.out.println("Warning: Empty ROI (label = " + i + ") detected (pullLabelsToROIManager).");
                continue;
            }
            if (roi == null) {
                roi = new Roi(0,0,0,0);
            }
            roi.setName("Label_" + i);
            roiList.add(roi);
        }
        clij2.release(binary);
        return true;
    }

    @Override
    public String getDescription() {
        return "Pulls all labels in a label map as ROIs to a list. \n\nFrom ImageJ macro this list is written to the log \n" +
                "window. From ImageJ macro conside using pullLabelsToROIManager.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }

    @Override
    public String getCategories() {
        return "Label";
    }
}
