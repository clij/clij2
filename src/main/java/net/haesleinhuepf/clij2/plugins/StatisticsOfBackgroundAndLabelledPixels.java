package net.haesleinhuepf.clij2.plugins;

import ij.ImagePlus;
import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

import java.util.ArrayList;

/**
 * Author: @haesleinhuepf
 *         April 2020
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_statisticsOfBackgroundAndLabelledPixels")
public class StatisticsOfBackgroundAndLabelledPixels extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {

        ClearCLBuffer inputImage = (ClearCLBuffer) args[0];
        ClearCLBuffer inputLabelMap = (ClearCLBuffer) args[1];

        ResultsTable resultsTable = ResultsTable.getResultsTable();


        statisticsOfBackgroundAndLabelledPixels(getCLIJ2(), inputImage, inputLabelMap, resultsTable);

        resultsTable.show("Results");
        return true;
    }

    public static ResultsTable statisticsOfBackgroundAndLabelledPixels(CLIJ2 clij2, ClearCLBuffer inputImage, ClearCLBuffer inputLabelMap, ResultsTable resultsTable) {
        int numberOfLabels = (int) clij2.maximumOfAllPixels(inputLabelMap);

        double[][] statistics = clij2.statisticsOfLabelledPixels(inputImage, inputLabelMap, 0, numberOfLabels);

        return StatisticsOfLabelledPixels.statisticsArrayToResultsTable(statistics, resultsTable);
    }


    @Override
    public String getParameterHelpText() {
        return "Image input, Image labelmap";
    }

    @Override
    public String getDescription() {
        return "Determines bounding box, area (in pixels/voxels), min, max and mean intensity \n" +
                " of background and labelled objects in a label map and corresponding pixels in the original image.\n\n" +
                "Instead of a label map, you can also use a binary image as a binary image is a label map with just one label." +
                "\n\nThis method is executed on the CPU and not on the GPU/OpenCL device.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
