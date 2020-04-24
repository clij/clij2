package net.haesleinhuepf.clij2.plugins;

import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 *         April 2020
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_statisticsOfImage")
public class StatisticsOfImage extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {

        ClearCLBuffer inputImage = (ClearCLBuffer) args[0];

        ResultsTable resultsTable = ResultsTable.getResultsTable();


        getCLIJ2().statisticsOfImage(inputImage, resultsTable);

        resultsTable.show("Results");
        return true;
    }

    public static ResultsTable statisticsOfImage(CLIJ2 clij2, ClearCLBuffer inputImage, ResultsTable resultsTable) {
        int numberOfLabels = 0;

        double[][] statistics = clij2.statisticsOfLabelledPixels(inputImage, null, 0, numberOfLabels);

        return StatisticsOfLabelledPixels.statisticsArrayToResultsTable(statistics, resultsTable);
    }


    @Override
    public String getParameterHelpText() {
        return "Image input";
    }

    @Override
    public String getDescription() {
        return "Determines image size (bounding box), area (in pixels/voxels), min, max and mean intensity \n" +
                " of all pixels in the original image." +
                "\n\nThis method is executed on the CPU and not on the GPU/OpenCL device.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
