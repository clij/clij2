package net.haesleinhuepf.clij2.plugins;

import ij.ImagePlus;
import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

import java.util.ArrayList;

/**
 * Author: @haesleinhuepf
 *         September 2019 in Prague
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_statisticsOfLabelledPixels")
public class StatisticsOfLabelledPixels extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    public enum STATISTICS_ENTRY {
        IDENTIFIER(0),
        BOUNDING_BOX_X(1),
        BOUNDING_BOX_Y(2),
        BOUNDING_BOX_Z(3),
        BOUNDING_BOX_END_X(4),
        BOUNDING_BOX_END_Y(5),
        BOUNDING_BOX_END_Z(6),
        BOUNDING_BOX_WIDTH(7),
        BOUNDING_BOX_HEIGHT(8),
        BOUNDING_BOX_DEPTH(9),
        MINIMUM_INTENSITY(10),
        MAXIMUM_INTENSITY(11),
        MEAN_INTENSITY(12),
        SUM_INTENSITY(13),
        STANDARD_DEVIATION_INTENSITY(14),
        PIXEL_COUNT(15);

        static final int NUMBER_OF_ENTRIES = 16;

        public final int value;

        STATISTICS_ENTRY(int value) {
            this.value = value;
        }
    }


    @Override
    public boolean executeCL() {

        ClearCLBuffer inputImage = (ClearCLBuffer) args[0];
        ClearCLBuffer inputLabelMap = (ClearCLBuffer) args[1];

        ResultsTable resultsTable = ResultsTable.getResultsTable();

        statisticsOfLabelledPixels(getCLIJ2(), inputImage, inputLabelMap, resultsTable);

        resultsTable.show("Results");
        return true;
    }

    public static ResultsTable statisticsOfLabelledPixels(CLIJ2 clij2, ClearCLBuffer inputImage, ClearCLBuffer inputLabelMap, ResultsTable resultsTable) {

        double[][] statistics = statisticsOfLabelledPixels(clij2, inputImage, inputLabelMap);

        ArrayList<STATISTICS_ENTRY> entries = new ArrayList<STATISTICS_ENTRY>();

        entries.add(STATISTICS_ENTRY.IDENTIFIER);
        entries.add(STATISTICS_ENTRY.BOUNDING_BOX_X);
        entries.add(STATISTICS_ENTRY.BOUNDING_BOX_Y);
        entries.add(STATISTICS_ENTRY.BOUNDING_BOX_Z);
        entries.add(STATISTICS_ENTRY.BOUNDING_BOX_END_X);
        entries.add(STATISTICS_ENTRY.BOUNDING_BOX_END_Y);
        entries.add(STATISTICS_ENTRY.BOUNDING_BOX_END_Z);
        entries.add(STATISTICS_ENTRY.BOUNDING_BOX_WIDTH);
        entries.add(STATISTICS_ENTRY.BOUNDING_BOX_HEIGHT);
        entries.add(STATISTICS_ENTRY.BOUNDING_BOX_DEPTH);
        entries.add(STATISTICS_ENTRY.MINIMUM_INTENSITY);
        entries.add(STATISTICS_ENTRY.MAXIMUM_INTENSITY);
        entries.add(STATISTICS_ENTRY.MEAN_INTENSITY);
        entries.add(STATISTICS_ENTRY.SUM_INTENSITY);
        entries.add(STATISTICS_ENTRY.STANDARD_DEVIATION_INTENSITY);
        entries.add(STATISTICS_ENTRY.PIXEL_COUNT);

        for (int line = 0; line < statistics.length; line++) {
            resultsTable.incrementCounter();
            for (STATISTICS_ENTRY entry : entries) {
                resultsTable.addValue(entry.toString(), statistics[line][entry.value]);
            }
        }

        return resultsTable;
    }

    public static double[][] statisticsOfLabelledPixels(CLIJ2 clij2, ClearCLBuffer inputImage, ClearCLBuffer inputLabelMap) {
        int numberOfLabels = (int) clij2.maximumOfAllPixels(inputLabelMap);
        return statisticsOfLabelledPixels(clij2, inputImage, inputLabelMap, 1, numberOfLabels);
    }

    public static double[] statisticsOfLabelledPixels(CLIJ2 clij2, ClearCLBuffer inputImage, ClearCLBuffer inputLabelMap, int labelIndex) {
        return statisticsOfLabelledPixels(clij2, inputImage, inputLabelMap, labelIndex, labelIndex)[0];
    }

    // as it's super slow on the GPU, let's do it on the CPU
    public static double[][] statisticsOfLabelledPixels(CLIJ2 clij2, ClearCLBuffer inputImage, ClearCLBuffer inputLabelMap, int startLabelIndex, int endLabelIndex) {
        double[][] statistics = new double[endLabelIndex - startLabelIndex + 1][STATISTICS_ENTRY.NUMBER_OF_ENTRIES];
        boolean[] initializedFlags = new boolean[statistics.length];

        ClearCLBuffer image = inputImage;
        ClearCLBuffer labelMap = inputLabelMap;

        if (inputImage.getNativeType() != NativeTypeEnum.Float) {
            image = clij2.create(inputImage.getDimensions(), NativeTypeEnum.Float);
            clij2.copy(inputImage, image);
        }

        if (inputLabelMap.getNativeType() != NativeTypeEnum.Float) {
            labelMap = clij2.create(inputLabelMap.getDimensions(), NativeTypeEnum.Float);
            clij2.copy(inputLabelMap, labelMap);
        }

        ImagePlus imp = clij2.pull(image);
        ImagePlus lab = clij2.pull(labelMap);
        int width = imp.getWidth();
        for (int z = 0; z < imp.getNSlices(); z++) {
            imp.setZ(z + 1);
            lab.setZ(z + 1);

            float[] pixels = (float[]) imp.getProcessor().getPixels();
            float[] labels = (float[]) lab.getProcessor().getPixels();

            int x = 0;
            int y = 0;
            for (int i = 0; i < pixels.length; i++) {
                int index = (int) labels[i];
                if (index > 0) {

                    int targetIndex = index - startLabelIndex;
                    double value = pixels[i];

                    boolean initialized = initializedFlags[targetIndex];

                    if (x < statistics[targetIndex][STATISTICS_ENTRY.BOUNDING_BOX_X.value] || !initialized) {
                        statistics[targetIndex][STATISTICS_ENTRY.BOUNDING_BOX_X.value] = x;
                    }
                    if (y < statistics[targetIndex][STATISTICS_ENTRY.BOUNDING_BOX_Y.value] || !initialized) {
                        statistics[targetIndex][STATISTICS_ENTRY.BOUNDING_BOX_Y.value] = y;
                    }
                    if (z < statistics[targetIndex][STATISTICS_ENTRY.BOUNDING_BOX_Z.value] || !initialized) {
                        statistics[targetIndex][STATISTICS_ENTRY.BOUNDING_BOX_Z.value] = z;
                    }
                    if (x > statistics[targetIndex][STATISTICS_ENTRY.BOUNDING_BOX_END_X.value] || !initialized) {
                        statistics[targetIndex][STATISTICS_ENTRY.BOUNDING_BOX_END_X.value] = x;
                    }
                    if (y > statistics[targetIndex][STATISTICS_ENTRY.BOUNDING_BOX_END_Y.value] || !initialized) {
                        statistics[targetIndex][STATISTICS_ENTRY.BOUNDING_BOX_END_Y.value] = y;
                    }
                    if (z > statistics[targetIndex][STATISTICS_ENTRY.BOUNDING_BOX_END_Z.value] || !initialized) {
                        statistics[targetIndex][STATISTICS_ENTRY.BOUNDING_BOX_END_Z.value] = z;
                    }

                    if (value > statistics[targetIndex][STATISTICS_ENTRY.MAXIMUM_INTENSITY.value] || !initialized) {
                        statistics[targetIndex][STATISTICS_ENTRY.MAXIMUM_INTENSITY.value] = value;
                    }
                    if (value < statistics[targetIndex][STATISTICS_ENTRY.MINIMUM_INTENSITY.value] || !initialized) {
                        statistics[targetIndex][STATISTICS_ENTRY.MINIMUM_INTENSITY.value] = value;
                    }
                    statistics[targetIndex][STATISTICS_ENTRY.SUM_INTENSITY.value] += value;
                    statistics[targetIndex][STATISTICS_ENTRY.PIXEL_COUNT.value] += 1;


                    initializedFlags[targetIndex] = true;
                }

                x++;
                if (x >= width) {
                    x = 0;
                    y++;
                }
            }
        }


        for (int j = 0; j < statistics.length; j++) {
            statistics[j][STATISTICS_ENTRY.MEAN_INTENSITY.value] =
                    statistics[j][STATISTICS_ENTRY.SUM_INTENSITY.value] /
                    statistics[j][STATISTICS_ENTRY.PIXEL_COUNT.value];

            statistics[j][STATISTICS_ENTRY.BOUNDING_BOX_WIDTH.value] =
                    statistics[j][STATISTICS_ENTRY.BOUNDING_BOX_END_X.value] -
                            statistics[j][STATISTICS_ENTRY.BOUNDING_BOX_X.value] + 1;
            statistics[j][STATISTICS_ENTRY.BOUNDING_BOX_HEIGHT.value] =
                    statistics[j][STATISTICS_ENTRY.BOUNDING_BOX_END_Y.value] -
                            statistics[j][STATISTICS_ENTRY.BOUNDING_BOX_Y.value] + 1;
            statistics[j][STATISTICS_ENTRY.BOUNDING_BOX_DEPTH.value] =
                    statistics[j][STATISTICS_ENTRY.BOUNDING_BOX_END_Z.value] -
                            statistics[j][STATISTICS_ENTRY.BOUNDING_BOX_Z.value] + 1;

            statistics[j][STATISTICS_ENTRY.IDENTIFIER.value] = j + startLabelIndex;
        }


        double[] squaredDifferencesFromMean = new double[statistics.length];
        for (int z = 0; z < imp.getNSlices(); z++) {
            imp.setZ(z + 1);
            lab.setZ(z + 1);

            float[] pixels = (float[]) imp.getProcessor().getPixels();
            float[] labels = (float[]) lab.getProcessor().getPixels();

            for (int i = 0; i < pixels.length; i ++) {
                int index = (int) labels[i];
                if (index > 0) {

                    int targetIndex = index - startLabelIndex;
                    double value = pixels[i];

                    squaredDifferencesFromMean[targetIndex] += Math.pow(value - statistics[targetIndex][STATISTICS_ENTRY.MEAN_INTENSITY.value], 2);
                }
            }
        }

        for (int j = 0; j < statistics.length; j++) {
            statistics[j][STATISTICS_ENTRY.STANDARD_DEVIATION_INTENSITY.value] =
                    Math.sqrt(squaredDifferencesFromMean[j] /
                            statistics[j][STATISTICS_ENTRY.PIXEL_COUNT.value]);
        }

        if (inputImage != image) {
            image.close();
        }
        if (inputLabelMap != labelMap) {
            labelMap.close();
        }

        return statistics;
    }
    /*
    public static double[][] statisticsOfLabelledPixels(CLIJ clij, ClearCLBuffer inputImage, ClearCLBuffer inputLabelMap, int startLabelIndex, int endLabelIndex) {
        if (endLabelIndex - startLabelIndex > 10) {
            System.out.println("processing many labels");
            return statisticsOfManyLabelledPixels(clij, inputImage, inputLabelMap, startLabelIndex, endLabelIndex);
        }

        double[][] statistics = new double[endLabelIndex - startLabelIndex + 1][STATISTICS_ENTRY.NUMBER_OF_ENTRIES];

        ClearCLBuffer tempMask = clij.create(inputImage);
        for (int i = startLabelIndex; i <= endLabelIndex; i++) {
            System.out.println("Deriving stats from " + i);
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
    }*/


    // All the cropping doesn't work; we must recompile all the time; not good
    // We can revive that code to speed stats up when image-size-specific re-compilation is no longer necessary
    /*
    private static double[][] statisticsOfManyLabelledPixels(CLIJ clij, ClearCLBuffer inputImage, ClearCLBuffer inputLabelMap, int startLabelIndex, int endLabelIndex) {
        double[][] statistics = new double[endLabelIndex - startLabelIndex + 1][STATISTICS_ENTRY.NUMBER_OF_ENTRIES];

        ClearCLBuffer tempMask = clij.create(inputImage);
        for (int i = startLabelIndex; i <= endLabelIndex; i++) {
            System.out.println("Deriving stats from " + i);
            LabelToMask.labelToMask(clij, inputLabelMap, tempMask, new Float(i));
            double[] boundingBox = BoundingBox.boundingBox(clij, tempMask);
            long[] size = new long[3];
            for (int d = 0; d < 3; d++) {
                size[d] = (long)Math.max(((long)(boundingBox[d + 3] / 10)) * 10, 1);
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
