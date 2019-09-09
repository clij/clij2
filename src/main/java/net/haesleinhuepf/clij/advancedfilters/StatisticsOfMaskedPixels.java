package net.haesleinhuepf.clij.advancedfilters;

import ij.IJ;
import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.imglib2.Cursor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;
import org.scijava.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Author: @haesleinhuepf
 *         September 2019 in Prague
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_statisticsOfMaskedPixels")
public class StatisticsOfMaskedPixels extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    public enum STATISTICS_ENTRY {

        BOUNDING_BOX_X(0),
        BOUNDING_BOX_Y(1),
        BOUNDING_BOX_Z(2),
        BOUNDING_BOX_WIDTH(3),
        BOUNDING_BOX_HEIGHT(4),
        BOUNDING_BOX_DEPTH(5),
        MINIMUM_INTENSITY(6),
        MAXIMUM_INTENSITY(7),
        MEAN_INTENSITY(8),
        PIXEL_COUNT(9);

        static final int NUMBER_OF_ENTRIES = 10;

        private int value;

        STATISTICS_ENTRY(int value) {
            this.value = value;
        }
    }


    @Override
    public boolean executeCL() {

        ClearCLBuffer inputImage = (ClearCLBuffer) args[0];
        ClearCLBuffer inputLabelMap = (ClearCLBuffer) args[1];

        double[][] statistics = statisticsOfMaskedPixels(clij, inputImage, inputLabelMap);

        ResultsTable resultsTable = ResultsTable.getResultsTable();


        ArrayList<STATISTICS_ENTRY> entries = new ArrayList<STATISTICS_ENTRY>();

        entries.add(STATISTICS_ENTRY.BOUNDING_BOX_X);
        entries.add(STATISTICS_ENTRY.BOUNDING_BOX_Y);
        entries.add(STATISTICS_ENTRY.BOUNDING_BOX_Z);
        entries.add(STATISTICS_ENTRY.BOUNDING_BOX_WIDTH);
        entries.add(STATISTICS_ENTRY.BOUNDING_BOX_HEIGHT);
        entries.add(STATISTICS_ENTRY.BOUNDING_BOX_DEPTH);
        entries.add(STATISTICS_ENTRY.MINIMUM_INTENSITY);
        entries.add(STATISTICS_ENTRY.MAXIMUM_INTENSITY);
        entries.add(STATISTICS_ENTRY.MEAN_INTENSITY);
        entries.add(STATISTICS_ENTRY.PIXEL_COUNT);

        for (int line = 0; line < statistics.length; line++) {
            resultsTable.incrementCounter();
            for (STATISTICS_ENTRY entry : entries) {
                resultsTable.addValue(entry.toString(), statistics[line][entry.value]);
            }
        }
        resultsTable.show("Results");
        return true;
    }

    public static double[][] statisticsOfMaskedPixels(CLIJ clij, ClearCLBuffer inputImage, ClearCLBuffer inputLabelMap) {
        int numberOfLabels = (int) clij.op().maximumOfAllPixels(inputLabelMap);
        return statisticsOfMaskedPixels(clij, inputImage, inputLabelMap, 1, numberOfLabels-1);
    }

    public static double[] statisticsOfMaskedPixels(CLIJ clij, ClearCLBuffer inputImage, ClearCLBuffer inputLabelMap, int labelIndex) {
        return statisticsOfMaskedPixels(clij, inputImage, inputLabelMap, labelIndex, labelIndex)[0];
    }

    public static double[][] statisticsOfMaskedPixels(CLIJ clij, ClearCLBuffer inputImage, ClearCLBuffer inputLabelMap, int startLabelIndex, int endLabelIndex) {
        double[][] statistics = new double[endLabelIndex - startLabelIndex + 1][STATISTICS_ENTRY.NUMBER_OF_ENTRIES];

        ClearCLBuffer tempMask = clij.create(inputImage);
        for (int i = startLabelIndex; i <= endLabelIndex; i++) {
            LabelToMask.labelToMask(clij, inputLabelMap, tempMask, new Float(i));
            double[] boundingBox = BoundingBox.boundingBox(clij, tempMask);

            int numberOfPixels = (int)clij.op().sumPixels(tempMask);
            double maximumIntensity = MaximumOfMaskedPixels.maximumOfMaskedPixels(clij, inputImage, tempMask);
            double minimumIntensity = MinimumOfMaskedPixels.minimumOfMaskedPixels(clij, inputImage, tempMask);
            double meanIntensity = MeanOfMaskedPixels.meanOfMaskedPixels(clij, inputImage, tempMask);

            statistics[i - startLabelIndex][STATISTICS_ENTRY.BOUNDING_BOX_X.value]      = boundingBox[STATISTICS_ENTRY.BOUNDING_BOX_X.value];
            statistics[i - startLabelIndex][STATISTICS_ENTRY.BOUNDING_BOX_Y.value]      = boundingBox[STATISTICS_ENTRY.BOUNDING_BOX_Y.value];
            statistics[i - startLabelIndex][STATISTICS_ENTRY.BOUNDING_BOX_Z.value]      = boundingBox[STATISTICS_ENTRY.BOUNDING_BOX_Z.value];
            statistics[i - startLabelIndex][STATISTICS_ENTRY.BOUNDING_BOX_WIDTH.value]  = boundingBox[STATISTICS_ENTRY.BOUNDING_BOX_WIDTH.value];
            statistics[i - startLabelIndex][STATISTICS_ENTRY.BOUNDING_BOX_HEIGHT.value] = boundingBox[STATISTICS_ENTRY.BOUNDING_BOX_HEIGHT.value];
            statistics[i - startLabelIndex][STATISTICS_ENTRY.BOUNDING_BOX_DEPTH.value]  = boundingBox[STATISTICS_ENTRY.BOUNDING_BOX_DEPTH.value];
            statistics[i - startLabelIndex][STATISTICS_ENTRY.MINIMUM_INTENSITY.value]   = minimumIntensity;
            statistics[i - startLabelIndex][STATISTICS_ENTRY.MAXIMUM_INTENSITY.value]   = maximumIntensity;
            statistics[i - startLabelIndex][STATISTICS_ENTRY.MEAN_INTENSITY.value]      = meanIntensity;
            statistics[i - startLabelIndex][STATISTICS_ENTRY.PIXEL_COUNT.value]         = numberOfPixels;
        }
        tempMask.close();

        return statistics;
    }


    // All the cropping doesn't work; we must recompile all the time; not good
    // We can revive that code to speed stats up when image-size-specific re-compilation is no longer necessary
    /*
    public static double[][] statisticsOfMaskedPixels(CLIJ clij, ClearCLBuffer inputImage, ClearCLBuffer inputLabelMap, int startLabelIndex, int endLabelIndex) {
        double[][] statistics = new double[endLabelIndex - startLabelIndex + 1][STATISTICS_ENTRY.NUMBER_OF_ENTRIES];

        ClearCLBuffer tempMask = clij.create(inputImage);
        for (int i = startLabelIndex; i <= endLabelIndex; i++) {
            LabelToMask.labelToMask(clij, inputLabelMap, tempMask, new Float(i));
            double[] boundingBox = BoundingBox.boundingBox(clij, tempMask);
            long[] size = new long[3];
            for (int d = 0; d < 3; d++) {
                size[d] = (long)Math.max(boundingBox[d + 3], 1);
            }
            ClearCLBuffer cropImage =  clij.create(size, inputImage.getNativeType());
            ClearCLBuffer cropMask =  clij.create(size, tempMask.getNativeType());

            clij.op().crop(inputImage, cropImage, (int)boundingBox[0], (int)boundingBox[1], (int)boundingBox[2]);
            clij.op().crop(tempMask, cropMask, (int)boundingBox[0], (int)boundingBox[1], (int)boundingBox[2]);

            int numberOfPixels = (int)clij.op().sumPixels(cropMask);
            double maximumIntensity = MaximumOfMaskedPixels.maximumOfMaskedPixels(clij, cropImage, cropMask);
            double minimumIntensity = MinimumOfMaskedPixels.minimumOfMaskedPixels(clij, cropImage, cropMask);
            double meanIntensity = MeanOfMaskedPixels.meanOfMaskedPixels(clij, cropImage, cropMask);

            statistics[i - startLabelIndex][STATISTICS_ENTRY.BOUNDING_BOX_X.value]      = boundingBox[STATISTICS_ENTRY.BOUNDING_BOX_X.value];
            statistics[i - startLabelIndex][STATISTICS_ENTRY.BOUNDING_BOX_Y.value]      = boundingBox[STATISTICS_ENTRY.BOUNDING_BOX_Y.value];
            statistics[i - startLabelIndex][STATISTICS_ENTRY.BOUNDING_BOX_Z.value]      = boundingBox[STATISTICS_ENTRY.BOUNDING_BOX_Z.value];
            statistics[i - startLabelIndex][STATISTICS_ENTRY.BOUNDING_BOX_WIDTH.value]  = boundingBox[STATISTICS_ENTRY.BOUNDING_BOX_WIDTH.value];
            statistics[i - startLabelIndex][STATISTICS_ENTRY.BOUNDING_BOX_HEIGHT.value] = boundingBox[STATISTICS_ENTRY.BOUNDING_BOX_HEIGHT.value];
            statistics[i - startLabelIndex][STATISTICS_ENTRY.BOUNDING_BOX_DEPTH.value]  = boundingBox[STATISTICS_ENTRY.BOUNDING_BOX_DEPTH.value];
            statistics[i - startLabelIndex][STATISTICS_ENTRY.MINIMUM_INTENSITY.value]   = minimumIntensity;
            statistics[i - startLabelIndex][STATISTICS_ENTRY.MAXIMUM_INTENSITY.value]   = maximumIntensity;
            statistics[i - startLabelIndex][STATISTICS_ENTRY.MEAN_INTENSITY.value]      = meanIntensity;
            statistics[i - startLabelIndex][STATISTICS_ENTRY.PIXEL_COUNT.value]         = numberOfPixels;

            cropImage.close();
            cropMask.close();

            IJ.log("idx: " + i);
        }
        tempMask.close();

        return statistics;
    }
    */


    @Override
    public String getParameterHelpText() {
        return "Image input, Image labelmap";
    }

    @Override
    public String getDescription() {
        return "Determines bounding box, area (in pixels/voxels), min, max and mean intensity " +
                " of a labelled object in a label map and corresponding pixels in the original image." +
                "Instead of a label map, you can also use a binary image as a binary image is a label map with just one label.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
