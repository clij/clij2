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

import java.nio.FloatBuffer;
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
        PIXEL_COUNT(15),
        SUM_INTENSITY_TIMES_X(16),
        SUM_INTENSITY_TIMES_Y(17),
        SUM_INTENSITY_TIMES_Z(18),
        MASS_CENTER_X(19),
        MASS_CENTER_Y(20),
        MASS_CENTER_Z(21),
        SUM_X(22),
        SUM_Y(23),
        SUM_Z(24),
        CENTROID_X(25),
        CENTROID_Y(26),
        CENTROID_Z(27),
        SUM_DISTANCE_TO_MASS_CENTER(28),
        MEAN_DISTANCE_TO_MASS_CENTER(29),
        MAX_DISTANCE_TO_MASS_CENTER(30),
        MAX_MEAN_DISTANCE_TO_MASS_CENTER_RATIO(31),
        SUM_DISTANCE_TO_CENTROID(32),
        MEAN_DISTANCE_TO_CENTROID(33),
        MAX_DISTANCE_TO_CENTROID(34),
        MAX_MEAN_DISTANCE_TO_CENTROID_RATIO(35);
        static final int NUMBER_OF_ENTRIES = 36;

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

        getCLIJ2().statisticsOfLabelledPixels(inputImage, inputLabelMap, resultsTable);

        resultsTable.show("Results");
        return true;
    }

    public static ResultsTable statisticsOfLabelledPixels(CLIJ2 clij2, ClearCLBuffer inputImage, ClearCLBuffer inputLabelMap, ResultsTable resultsTable) {

        double[][] statistics = statisticsOfLabelledPixels(clij2, inputImage, inputLabelMap);

        return statisticsArrayToResultsTable(statistics, resultsTable);
    }


    static ResultsTable statisticsArrayToResultsTable(double[][] statistics, ResultsTable resultsTable) {
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
        entries.add(STATISTICS_ENTRY.SUM_INTENSITY_TIMES_X);
        entries.add(STATISTICS_ENTRY.SUM_INTENSITY_TIMES_Y);
        entries.add(STATISTICS_ENTRY.SUM_INTENSITY_TIMES_Z);
        entries.add(STATISTICS_ENTRY.MASS_CENTER_X);
        entries.add(STATISTICS_ENTRY.MASS_CENTER_Y);
        entries.add(STATISTICS_ENTRY.MASS_CENTER_Z);
        entries.add(STATISTICS_ENTRY.SUM_X);
        entries.add(STATISTICS_ENTRY.SUM_Y);
        entries.add(STATISTICS_ENTRY.SUM_Z);
        entries.add(STATISTICS_ENTRY.CENTROID_X);
        entries.add(STATISTICS_ENTRY.CENTROID_Y);
        entries.add(STATISTICS_ENTRY.CENTROID_Z);
        entries.add(STATISTICS_ENTRY.SUM_DISTANCE_TO_MASS_CENTER);
        entries.add(STATISTICS_ENTRY.MEAN_DISTANCE_TO_MASS_CENTER);
        entries.add(STATISTICS_ENTRY.MAX_DISTANCE_TO_MASS_CENTER);
        entries.add(STATISTICS_ENTRY.MAX_MEAN_DISTANCE_TO_MASS_CENTER_RATIO);
        entries.add(STATISTICS_ENTRY.SUM_DISTANCE_TO_CENTROID);
        entries.add(STATISTICS_ENTRY.MEAN_DISTANCE_TO_CENTROID);
        entries.add(STATISTICS_ENTRY.MAX_DISTANCE_TO_CENTROID);
        entries.add(STATISTICS_ENTRY.MAX_MEAN_DISTANCE_TO_CENTROID_RATIO);


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

    private static class Statistician implements Runnable{


        private double[][] statistics;
        private final ClearCLBuffer inputImage;
        private final ClearCLBuffer inputLabelMap;
        private final int startLabelIndex;
        private final int endLabelIndex;
        private final int zPlane;
        private final CLIJ2 clij2;

        float[] pixels;
        float[] labels;

        Statistician(double[][] statistics, CLIJ2 clij2, ClearCLBuffer inputImage, ClearCLBuffer inputLabelMap, int startLabelIndex, int endLabelIndex, int zPlane) {

            this.statistics = statistics;
            this.inputImage = inputImage;
            this.inputLabelMap = inputLabelMap;
            this.startLabelIndex = startLabelIndex;
            this.endLabelIndex = endLabelIndex;
            this.zPlane = zPlane;
            this.clij2 = clij2;
        }

        @Override
        public void run() {
            boolean[] initializedFlags = new boolean[statistics.length];

            //ImagePlus imp = null;
            //ImagePlus lab = null;

            synchronized (clij2) {
                ClearCLBuffer image = clij2.create(inputImage.getWidth(), inputImage.getHeight());
                clij2.copySlice(inputImage, image, zPlane);
                //inputImage;
                ClearCLBuffer labelMap = null;
                //inputLabelMap;

                //if (inputImage.getNativeType() != NativeTypeEnum.Float) {
                //    image = clij2.create(inputImage.getDimensions(), NativeTypeEnum.Float);
                //    clij2.copy(inputImage, image);
                //}

                if (inputLabelMap != null) {
                    labelMap = clij2.create(inputLabelMap.getWidth(), inputLabelMap.getHeight());
                    clij2.copySlice(inputLabelMap, labelMap, zPlane);
                    //if (inputLabelMap.getNativeType() != NativeTypeEnum.Float) {
                    //    labelMap = clij2.create(inputLabelMap.getDimensions(), NativeTypeEnum.Float);
                    //    clij2.copy(inputLabelMap, labelMap);
                    //}
                }

                pixels = new float[(int) (image.getWidth() * image.getHeight())];
                image.writeTo(FloatBuffer.wrap(pixels), true);
                image.close();

                if (labelMap != null) {
                    labels = new float[(int) (labelMap.getWidth() * labelMap.getHeight())];
                    labelMap.writeTo(FloatBuffer.wrap(labels), true);
  //              };
                //imp = clij2.pull(image);
                //lab = inputLabelMap != null ? clij2.pull(labelMap) : null;

//                if (labelMap != null) {
                    labelMap.close();
                }
            }
            int width = (int) inputImage.getWidth();
            //for (int z = 0; z < imp.getNSlices(); z++) {
            //    imp.setZ(z + 1);
            //    if (lab != null) {
            //        lab.setZ(z + 1);
            //    }

                //pixels = (float[]) imp.getProcessor().getPixels();
                //labels = lab!=null?(float[]) lab.getProcessor().getPixels():null;

                int x = 0;
                int y = 0;
                for (int i = 0; i < pixels.length; i++) {

                    int index = labels!=null?(int) labels[i]:0;
                    if (index >= startLabelIndex) {

                        int targetIndex = index - startLabelIndex;
                        double value = pixels[i];

                        boolean initialized = initializedFlags[targetIndex];

                        if (x < statistics[targetIndex][STATISTICS_ENTRY.BOUNDING_BOX_X.value] || !initialized) {
                            statistics[targetIndex][STATISTICS_ENTRY.BOUNDING_BOX_X.value] = x;
                        }
                        if (y < statistics[targetIndex][STATISTICS_ENTRY.BOUNDING_BOX_Y.value] || !initialized) {
                            statistics[targetIndex][STATISTICS_ENTRY.BOUNDING_BOX_Y.value] = y;
                        }
                        if (zPlane < statistics[targetIndex][STATISTICS_ENTRY.BOUNDING_BOX_Z.value] || !initialized) {
                            statistics[targetIndex][STATISTICS_ENTRY.BOUNDING_BOX_Z.value] = zPlane;
                        }
                        if (x > statistics[targetIndex][STATISTICS_ENTRY.BOUNDING_BOX_END_X.value] || !initialized) {
                            statistics[targetIndex][STATISTICS_ENTRY.BOUNDING_BOX_END_X.value] = x;
                        }
                        if (y > statistics[targetIndex][STATISTICS_ENTRY.BOUNDING_BOX_END_Y.value] || !initialized) {
                            statistics[targetIndex][STATISTICS_ENTRY.BOUNDING_BOX_END_Y.value] = y;
                        }
                        if (zPlane > statistics[targetIndex][STATISTICS_ENTRY.BOUNDING_BOX_END_Z.value] || !initialized) {
                            statistics[targetIndex][STATISTICS_ENTRY.BOUNDING_BOX_END_Z.value] = zPlane;
                        }

                        if (value > statistics[targetIndex][STATISTICS_ENTRY.MAXIMUM_INTENSITY.value] || !initialized) {
                            statistics[targetIndex][STATISTICS_ENTRY.MAXIMUM_INTENSITY.value] = value;
                        }
                        if (value < statistics[targetIndex][STATISTICS_ENTRY.MINIMUM_INTENSITY.value] || !initialized) {
                            statistics[targetIndex][STATISTICS_ENTRY.MINIMUM_INTENSITY.value] = value;
                        }
                        statistics[targetIndex][STATISTICS_ENTRY.SUM_INTENSITY.value] += value;

                        statistics[targetIndex][STATISTICS_ENTRY.SUM_INTENSITY_TIMES_X.value] += value * x;
                        statistics[targetIndex][STATISTICS_ENTRY.SUM_INTENSITY_TIMES_Y.value] += value * y;
                        statistics[targetIndex][STATISTICS_ENTRY.SUM_INTENSITY_TIMES_Z.value] += value * zPlane;

                        statistics[targetIndex][STATISTICS_ENTRY.SUM_X.value] += x;
                        statistics[targetIndex][STATISTICS_ENTRY.SUM_Y.value] += y;
                        statistics[targetIndex][STATISTICS_ENTRY.SUM_Z.value] += zPlane;

                        statistics[targetIndex][STATISTICS_ENTRY.PIXEL_COUNT.value] += 1;

                        initializedFlags[targetIndex] = true;
                    }

                    x++;
                    if (x >= width) {
                        x = 0;
                        y++;
                    }
                }
            //}
        }
    }


    private static class SecondOrderStatistician implements Runnable {
        private final float[] pixels;
        private final float[] labels;
        private final int startLabelIndex;
        private final double[][] statistics;
        private final double[] squaredDifferencesFromMean;
        private final int width;
        private final int z;

        SecondOrderStatistician(float[] pixels, float[] labels, int startLabelIndex, int width, int z, double[][] statistics, double[] squaredDifferencesFromMean) {
            this.pixels = pixels;
            this.labels = labels;
            this.startLabelIndex = startLabelIndex;
            this.statistics = statistics;
            this.squaredDifferencesFromMean = squaredDifferencesFromMean;
            this.width = width;
            this.z = z;
        }

        @Override
        public void run() {
            //for (int z = 0; z < imp.getNSlices(); z++) {
              //  imp.setZ(z + 1);
                //if(lab != null) {
                  //  lab.setZ(z + 1);
                //}

                //float[] pixels = (float[]) imp.getProcessor().getPixels();
                //float[] labels = lab!=null?(float[]) lab.getProcessor().getPixels():null;

                int x = 0;
                int y = 0;
                for (int i = 0; i < pixels.length; i ++) {

                    int index = labels!=null?(int) labels[i]:0;
                    if (index >= startLabelIndex) {

                        int targetIndex = index - startLabelIndex;
                        double value = pixels[i];

                        squaredDifferencesFromMean[targetIndex] += Math.pow(value - statistics[targetIndex][STATISTICS_ENTRY.MEAN_INTENSITY.value], 2);

                        double mass_center_x = statistics[targetIndex][STATISTICS_ENTRY.MASS_CENTER_X.value];
                        double mass_center_y = statistics[targetIndex][STATISTICS_ENTRY.MASS_CENTER_Y.value];
                        double mass_center_z = statistics[targetIndex][STATISTICS_ENTRY.MASS_CENTER_Z.value];

                        double distance = Math.sqrt(
                                Math.pow(mass_center_x - x, 2) +
                                Math.pow(mass_center_y - y, 2) +
                                Math.pow(mass_center_z - z, 2)
                        );

                        if (statistics[targetIndex][STATISTICS_ENTRY.MAX_DISTANCE_TO_MASS_CENTER.value] < distance) {
                            statistics[targetIndex][STATISTICS_ENTRY.MAX_DISTANCE_TO_MASS_CENTER.value] = distance;
                        }
                        statistics[targetIndex][STATISTICS_ENTRY.SUM_DISTANCE_TO_MASS_CENTER.value] += distance;

                        double center_x = statistics[targetIndex][STATISTICS_ENTRY.CENTROID_X.value];
                        double center_y = statistics[targetIndex][STATISTICS_ENTRY.CENTROID_Y.value];
                        double center_z = statistics[targetIndex][STATISTICS_ENTRY.CENTROID_Z.value];

                        distance = Math.sqrt(
                                Math.pow(center_x - x, 2) +
                                Math.pow(center_y - y, 2) +
                                Math.pow(center_z - z, 2)
                        );

                        if (statistics[targetIndex][STATISTICS_ENTRY.MAX_DISTANCE_TO_CENTROID.value] < distance) {
                            statistics[targetIndex][STATISTICS_ENTRY.MAX_DISTANCE_TO_CENTROID.value] = distance;
                        }
                        statistics[targetIndex][STATISTICS_ENTRY.SUM_DISTANCE_TO_CENTROID.value] += distance;

                    }

                    x++;
                    if (x >= width) {
                        x = 0;
                        y++;
                    }

                }
           // }

        }
    }

    // as it's super slow on the GPU, let's do it on the CPU
    public static double[][] statisticsOfLabelledPixels(CLIJ2 clij2, ClearCLBuffer inputImage, ClearCLBuffer inputLabelMap, int startLabelIndex, int endLabelIndex) {
        int num_threads = (int) inputImage.getDepth();

        double[][][] statistics = new double[num_threads + 1][endLabelIndex - startLabelIndex + 1][STATISTICS_ENTRY.NUMBER_OF_ENTRIES];

        Thread[] threads = new Thread[num_threads];
        Statistician[] statisticians = new Statistician[num_threads];
        for (int i = 0; i < num_threads; i++) {
            statisticians[i] = new Statistician(statistics[i + 1], clij2, inputImage, inputLabelMap, startLabelIndex, endLabelIndex, i);
            threads[i] = new Thread(statisticians[i]);
            threads[i].start();
        }
        for (int i = 0; i < num_threads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        STATISTICS_ENTRY[] indices_to_min_collect = {
                STATISTICS_ENTRY.BOUNDING_BOX_X,
                STATISTICS_ENTRY.BOUNDING_BOX_Y,
                STATISTICS_ENTRY.BOUNDING_BOX_Z,
                STATISTICS_ENTRY.MINIMUM_INTENSITY
        };
        STATISTICS_ENTRY[] indices_to_max_collect = {
                STATISTICS_ENTRY.BOUNDING_BOX_END_X,
                STATISTICS_ENTRY.BOUNDING_BOX_END_Y,
                STATISTICS_ENTRY.BOUNDING_BOX_END_Z,
                STATISTICS_ENTRY.MAXIMUM_INTENSITY
        };
        STATISTICS_ENTRY[] indices_to_sum_collect = {
                STATISTICS_ENTRY.SUM_INTENSITY,
                STATISTICS_ENTRY.PIXEL_COUNT,
                STATISTICS_ENTRY.SUM_INTENSITY_TIMES_X,
                STATISTICS_ENTRY.SUM_INTENSITY_TIMES_Y,
                STATISTICS_ENTRY.SUM_INTENSITY_TIMES_Z,
                STATISTICS_ENTRY.SUM_X,
                STATISTICS_ENTRY.SUM_Y,
                STATISTICS_ENTRY.SUM_Z
        };

        for (int t = 0; t < num_threads; t++) {
            for (int j = 0; j < statistics[0].length; j++) {
                for (STATISTICS_ENTRY entry : indices_to_max_collect) {
                    if (t == 0) {
                        statistics[0][j][entry.value] = statistics[t + 1][j][entry.value];
                    } else {
                        statistics[0][j][entry.value] = Math.max(statistics[0][j][entry.value], statistics[t + 1][j][entry.value]);
                    }
                }
                for (STATISTICS_ENTRY entry : indices_to_min_collect) {
                    if (t == 0) {
                        statistics[0][j][entry.value] = statistics[t + 1][j][entry.value];
                    } else {
                        statistics[0][j][entry.value] = Math.min(statistics[0][j][entry.value], statistics[t + 1][j][entry.value]);
                    }
                }
                for (STATISTICS_ENTRY entry : indices_to_sum_collect) {
                    statistics[0][j][entry.value] += statistics[t + 1][j][entry.value];
                }
            }
        }

        for (int j = 0; j < statistics[0].length; j++) {
            statistics[0][j][STATISTICS_ENTRY.MEAN_INTENSITY.value] =
                    statistics[0][j][STATISTICS_ENTRY.SUM_INTENSITY.value] /
                    statistics[0][j][STATISTICS_ENTRY.PIXEL_COUNT.value];

            statistics[0][j][STATISTICS_ENTRY.BOUNDING_BOX_WIDTH.value] =
                    statistics[0][j][STATISTICS_ENTRY.BOUNDING_BOX_END_X.value] -
                    statistics[0][j][STATISTICS_ENTRY.BOUNDING_BOX_X.value] + 1;
            statistics[0][j][STATISTICS_ENTRY.BOUNDING_BOX_HEIGHT.value] =
                    statistics[0][j][STATISTICS_ENTRY.BOUNDING_BOX_END_Y.value] -
                    statistics[0][j][STATISTICS_ENTRY.BOUNDING_BOX_Y.value] + 1;
            statistics[0][j][STATISTICS_ENTRY.BOUNDING_BOX_DEPTH.value] =
                    statistics[0][j][STATISTICS_ENTRY.BOUNDING_BOX_END_Z.value] -
                    statistics[0][j][STATISTICS_ENTRY.BOUNDING_BOX_Z.value] + 1;

            statistics[0][j][STATISTICS_ENTRY.IDENTIFIER.value] = j + startLabelIndex;

            statistics[0][j][STATISTICS_ENTRY.MASS_CENTER_X.value] =
                    statistics[0][j][STATISTICS_ENTRY.SUM_INTENSITY_TIMES_X.value] /
                    statistics[0][j][STATISTICS_ENTRY.SUM_INTENSITY.value];
            statistics[0][j][STATISTICS_ENTRY.MASS_CENTER_Y.value] =
                    statistics[0][j][STATISTICS_ENTRY.SUM_INTENSITY_TIMES_Y.value] /
                    statistics[0][j][STATISTICS_ENTRY.SUM_INTENSITY.value];
            statistics[0][j][STATISTICS_ENTRY.MASS_CENTER_Z.value] =
                    statistics[0][j][STATISTICS_ENTRY.SUM_INTENSITY_TIMES_Z.value] /
                    statistics[0][j][STATISTICS_ENTRY.SUM_INTENSITY.value];

            statistics[0][j][STATISTICS_ENTRY.CENTROID_X.value] =
                    statistics[0][j][STATISTICS_ENTRY.SUM_X.value] /
                    statistics[0][j][STATISTICS_ENTRY.PIXEL_COUNT.value];
            statistics[0][j][STATISTICS_ENTRY.CENTROID_Y.value] =
                    statistics[0][j][STATISTICS_ENTRY.SUM_Y.value] /
                    statistics[0][j][STATISTICS_ENTRY.PIXEL_COUNT.value];
            statistics[0][j][STATISTICS_ENTRY.CENTROID_Z.value] =
                    statistics[0][j][STATISTICS_ENTRY.SUM_Z.value] /
                    statistics[0][j][STATISTICS_ENTRY.PIXEL_COUNT.value];
        }

        for (int t = 0; t < num_threads; t++) {
            for (int j = 0; j < statistics[0].length; j++) {
                statistics[t + 1][j][STATISTICS_ENTRY.MEAN_INTENSITY.value] = statistics[0][j][STATISTICS_ENTRY.MEAN_INTENSITY.value];

                statistics[t + 1][j][STATISTICS_ENTRY.MASS_CENTER_X.value] = statistics[0][j][STATISTICS_ENTRY.MASS_CENTER_X.value];
                statistics[t + 1][j][STATISTICS_ENTRY.MASS_CENTER_Y.value] = statistics[0][j][STATISTICS_ENTRY.MASS_CENTER_Y.value];
                statistics[t + 1][j][STATISTICS_ENTRY.MASS_CENTER_Z.value] = statistics[0][j][STATISTICS_ENTRY.MASS_CENTER_Z.value];
                statistics[t + 1][j][STATISTICS_ENTRY.CENTROID_X.value] = statistics[0][j][STATISTICS_ENTRY.CENTROID_X.value];
                statistics[t + 1][j][STATISTICS_ENTRY.CENTROID_Y.value] = statistics[0][j][STATISTICS_ENTRY.CENTROID_Y.value];
                statistics[t + 1][j][STATISTICS_ENTRY.CENTROID_Z.value] = statistics[0][j][STATISTICS_ENTRY.CENTROID_Z.value];


                statistics[t + 1][j][STATISTICS_ENTRY.MAX_DISTANCE_TO_MASS_CENTER.value] = 0;
                statistics[t + 1][j][STATISTICS_ENTRY.MAX_DISTANCE_TO_CENTROID.value] = 0;
                if (t == 0) {
                    statistics[0][j][STATISTICS_ENTRY.MAX_DISTANCE_TO_MASS_CENTER.value] = 0;
                    statistics[0][j][STATISTICS_ENTRY.MAX_DISTANCE_TO_CENTROID.value] = 0;
                }
            }
        }

        double[][] squaredDifferencesFromMean = new double[num_threads + 1][statistics[0].length];

        Thread[] followUpThreads = new Thread[num_threads];
        for (int t = 0; t < num_threads; t++) {
            followUpThreads[t] = new Thread(new SecondOrderStatistician(
                    statisticians[t].pixels,
                    statisticians[t].labels,
                    startLabelIndex,
                    (int) inputImage.getWidth(),
                    t, // t corresponds to z-position
                    statistics[t + 1],
                    squaredDifferencesFromMean[t + 1]
            ));
            followUpThreads[t].start();
        }
        for (int t = 0; t < num_threads; t++) {
            try {
                followUpThreads[t].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        if (inputImage != image) {
//            image.close();
//        }
//        if (inputLabelMap != labelMap && labelMap != null) {
//            labelMap.close();
//        }



        for (int t = 0; t < num_threads; t++) {
            for (int j = 0; j < statistics[0].length; j++) {
                squaredDifferencesFromMean[0][j] += squaredDifferencesFromMean[t+1][j];
                statistics[0][j][STATISTICS_ENTRY.MAX_DISTANCE_TO_MASS_CENTER.value] = Math.max(statistics[0][j][STATISTICS_ENTRY.MAX_DISTANCE_TO_MASS_CENTER.value], statistics[t + 1][j][STATISTICS_ENTRY.MAX_DISTANCE_TO_MASS_CENTER.value]);
                statistics[0][j][STATISTICS_ENTRY.MAX_DISTANCE_TO_CENTROID.value] = Math.max(statistics[0][j][STATISTICS_ENTRY.MAX_DISTANCE_TO_CENTROID.value], statistics[t + 1][j][STATISTICS_ENTRY.MAX_DISTANCE_TO_CENTROID.value]);
                statistics[0][j][STATISTICS_ENTRY.SUM_DISTANCE_TO_MASS_CENTER.value] = statistics[0][j][STATISTICS_ENTRY.SUM_DISTANCE_TO_MASS_CENTER.value] + statistics[t + 1][j][STATISTICS_ENTRY.SUM_DISTANCE_TO_MASS_CENTER.value];
                statistics[0][j][STATISTICS_ENTRY.SUM_DISTANCE_TO_CENTROID.value] = statistics[0][j][STATISTICS_ENTRY.SUM_DISTANCE_TO_CENTROID.value] + statistics[t + 1][j][STATISTICS_ENTRY.SUM_DISTANCE_TO_CENTROID.value];
            }
        }

        for (int j = 0; j < statistics[0].length; j++) {
            statistics[0][j][STATISTICS_ENTRY.STANDARD_DEVIATION_INTENSITY.value] =
                    Math.sqrt(squaredDifferencesFromMean[0][j] /
                            statistics[0][j][STATISTICS_ENTRY.PIXEL_COUNT.value]);

            statistics[0][j][STATISTICS_ENTRY.MEAN_DISTANCE_TO_MASS_CENTER.value] = statistics[0][j][STATISTICS_ENTRY.SUM_DISTANCE_TO_MASS_CENTER.value] / statistics[0][j][STATISTICS_ENTRY.PIXEL_COUNT.value];
            statistics[0][j][STATISTICS_ENTRY.MEAN_DISTANCE_TO_CENTROID.value] = statistics[0][j][STATISTICS_ENTRY.SUM_DISTANCE_TO_CENTROID.value] / statistics[0][j][STATISTICS_ENTRY.PIXEL_COUNT.value];

            statistics[0][j][STATISTICS_ENTRY.MAX_MEAN_DISTANCE_TO_MASS_CENTER_RATIO.value] = statistics[0][j][STATISTICS_ENTRY.MAX_DISTANCE_TO_MASS_CENTER.value] / statistics[0][j][STATISTICS_ENTRY.MEAN_DISTANCE_TO_MASS_CENTER.value];
            statistics[0][j][STATISTICS_ENTRY.MAX_MEAN_DISTANCE_TO_CENTROID_RATIO.value] = statistics[0][j][STATISTICS_ENTRY.MAX_DISTANCE_TO_CENTROID.value] / statistics[0][j][STATISTICS_ENTRY.MEAN_DISTANCE_TO_CENTROID.value];
        }

        return statistics[0];
    }

    public static double[][] statisticsOfLabelledPixels_single_threaded(CLIJ2 clij2, ClearCLBuffer inputImage, ClearCLBuffer inputLabelMap, int startLabelIndex, int endLabelIndex) {
        double[][] statistics = new double[endLabelIndex - startLabelIndex + 1][STATISTICS_ENTRY.NUMBER_OF_ENTRIES];
        boolean[] initializedFlags = new boolean[statistics.length];

        ClearCLBuffer image = inputImage;
        ClearCLBuffer labelMap = inputLabelMap;

        if (inputImage.getNativeType() != NativeTypeEnum.Float) {
            image = clij2.create(inputImage.getDimensions(), NativeTypeEnum.Float);
            clij2.copy(inputImage, image);
        }

        if (inputLabelMap != null) {
            if (inputLabelMap.getNativeType() != NativeTypeEnum.Float) {
                labelMap = clij2.create(inputLabelMap.getDimensions(), NativeTypeEnum.Float);
                clij2.copy(inputLabelMap, labelMap);
            }
        }

        ImagePlus imp = clij2.pull(image);
        ImagePlus lab = inputLabelMap!=null?clij2.pull(labelMap):null;
        int width = imp.getWidth();
        for (int z = 0; z < imp.getNSlices(); z++) {
            imp.setZ(z + 1);
            if (lab != null) {
                lab.setZ(z + 1);
            }

            float[] pixels = (float[]) imp.getProcessor().getPixels();
            float[] labels = lab!=null?(float[]) lab.getProcessor().getPixels():null;

            int x = 0;
            int y = 0;
            for (int i = 0; i < pixels.length; i++) {

                int index = labels!=null?(int) labels[i]:0;
                if (index >= startLabelIndex) {

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

                    statistics[targetIndex][STATISTICS_ENTRY.SUM_INTENSITY_TIMES_X.value] += value * x;
                    statistics[targetIndex][STATISTICS_ENTRY.SUM_INTENSITY_TIMES_Y.value] += value * y;
                    statistics[targetIndex][STATISTICS_ENTRY.SUM_INTENSITY_TIMES_Z.value] += value * z;

                    statistics[targetIndex][STATISTICS_ENTRY.SUM_X.value] += x;
                    statistics[targetIndex][STATISTICS_ENTRY.SUM_Y.value] += y;
                    statistics[targetIndex][STATISTICS_ENTRY.SUM_Z.value] += z;

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

            statistics[j][STATISTICS_ENTRY.MASS_CENTER_X.value] =
                    statistics[j][STATISTICS_ENTRY.SUM_INTENSITY_TIMES_X.value] /
                            statistics[j][STATISTICS_ENTRY.SUM_INTENSITY.value];
            statistics[j][STATISTICS_ENTRY.MASS_CENTER_Y.value] =
                    statistics[j][STATISTICS_ENTRY.SUM_INTENSITY_TIMES_Y.value] /
                            statistics[j][STATISTICS_ENTRY.SUM_INTENSITY.value];
            statistics[j][STATISTICS_ENTRY.MASS_CENTER_Z.value] =
                    statistics[j][STATISTICS_ENTRY.SUM_INTENSITY_TIMES_Z.value] /
                            statistics[j][STATISTICS_ENTRY.SUM_INTENSITY.value];

            statistics[j][STATISTICS_ENTRY.CENTROID_X.value] =
                    statistics[j][STATISTICS_ENTRY.SUM_X.value] /
                            statistics[j][STATISTICS_ENTRY.PIXEL_COUNT.value];
            statistics[j][STATISTICS_ENTRY.CENTROID_Y.value] =
                    statistics[j][STATISTICS_ENTRY.SUM_Y.value] /
                            statistics[j][STATISTICS_ENTRY.PIXEL_COUNT.value];
            statistics[j][STATISTICS_ENTRY.CENTROID_Z.value] =
                    statistics[j][STATISTICS_ENTRY.SUM_Z.value] /
                            statistics[j][STATISTICS_ENTRY.PIXEL_COUNT.value];


        }


        double[] squaredDifferencesFromMean = new double[statistics.length];
        for (int z = 0; z < imp.getNSlices(); z++) {
            imp.setZ(z + 1);
            if(lab != null) {
                lab.setZ(z + 1);
            }

            float[] pixels = (float[]) imp.getProcessor().getPixels();
            float[] labels = lab!=null?(float[]) lab.getProcessor().getPixels():null;

            for (int i = 0; i < pixels.length; i ++) {

                int index = labels!=null?(int) labels[i]:0;
                if (index >= startLabelIndex) {

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
        if (inputLabelMap != labelMap && labelMap != null) {
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
        return "Determines bounding box, area (in pixels/voxels), min, max and mean intensity \n" +
                " of labelled objects in a label map and corresponding pixels in the original image. \n\n" +
                "Instead of a label map, you can also use a binary image as a binary image is a label map with just one label." +
                "\n\nThis method is executed on the CPU and not on the GPU/OpenCL device.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }




}
