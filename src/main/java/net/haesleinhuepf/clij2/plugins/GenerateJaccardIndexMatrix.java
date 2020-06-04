package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

import java.nio.FloatBuffer;

/**
 * Author: @haesleinhuepf
 *         June 2020
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_generateJaccardIndexMatrix")
public class GenerateJaccardIndexMatrix extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        boolean result = generateJaccardIndexMatrix(getCLIJ2(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), (ClearCLBuffer)(args[2]));
        return result;
    }

    public static boolean generateJaccardIndexMatrix(CLIJ2 clij2, ClearCLBuffer src_label_map1,  ClearCLBuffer src_label_map2, ClearCLBuffer dst_jaccard_index_matrix) {
        int num_threads = (int) src_label_map1.getDepth();

        long[][][] counts = new long[num_threads][(int)dst_jaccard_index_matrix.getWidth()][(int)dst_jaccard_index_matrix.getHeight()];

        Thread[] threads = new Thread[num_threads];
        Statistician[] statisticians = new Statistician[num_threads];
        for (int i = 0; i < num_threads; i++) {
            statisticians[i] = new Statistician(counts[i], clij2, src_label_map1, src_label_map2, i);
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

        float[][] matrix = new float[(int)dst_jaccard_index_matrix.getWidth()][(int)dst_jaccard_index_matrix.getHeight()];

        for (int t = 0; t < num_threads; t++) {
            for (int j = 0; j < counts[0].length; j++) {
                for (int k = 0; k < counts[0][0].length; k++) {
                    matrix[j][k] += counts[t][j][k];
                }
            }
        }

        ClearCLBuffer true_positives = clij2.pushMat(matrix);
        ClearCLBuffer false_negatives = clij2.create(true_positives.getWidth(), 1);
        ClearCLBuffer false_positives = clij2.create(1, true_positives.getHeight());

        ClearCLBuffer temp1 = clij2.create(true_positives);
        ClearCLBuffer temp2 = clij2.create(true_positives);

        clij2.addImages(true_positives, false_negatives, temp1);
        clij2.addImages(temp1, false_positives, temp2);

        clij2.divideImages(true_positives, temp2, dst_jaccard_index_matrix);

        true_positives.close();
        false_negatives.close();
        false_positives.close();
        temp1.close();
        temp2.close();

        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image label_map1, Image label_map2, ByRef Image jaccard_index_matrix_destination";
    }


    private static class Statistician implements Runnable{


        private ClearCLBuffer input_label_map_1;
        private ClearCLBuffer input_label_map_2;
        private final int zPlane;
        private final CLIJ2 clij2;

        long[][] tps;

        private float[] labels_1;
        private float[] labels_2;

        Statistician(long[][] tps, CLIJ2 clij2, ClearCLBuffer input_label_map_1, ClearCLBuffer input_label_map_2, int zPlane) {
            this.tps = tps;
            this.input_label_map_1 = input_label_map_1;
            this.input_label_map_2 = input_label_map_2;

            this.zPlane = zPlane;
            this.clij2 = clij2;
        }

        @Override
        public void run() {
            synchronized (clij2) {
                ClearCLBuffer label_map_1_slice = clij2.create(input_label_map_1.getWidth(), input_label_map_1.getHeight());
                clij2.copySlice(input_label_map_1, label_map_1_slice, zPlane);
                labels_1 = new float[(int) (label_map_1_slice.getWidth() * label_map_1_slice.getHeight())];
                label_map_1_slice.writeTo(FloatBuffer.wrap(labels_1), true);
                label_map_1_slice.close();

                ClearCLBuffer label_map_2_slice = clij2.create(input_label_map_2.getWidth(), input_label_map_2.getHeight());
                clij2.copySlice(input_label_map_2, label_map_2_slice, zPlane);
                labels_2 = new float[(int) (label_map_2_slice.getWidth() * label_map_2_slice.getHeight())];
                label_map_2_slice.writeTo(FloatBuffer.wrap(labels_2), true);
                label_map_2_slice.close();
            }
            int width = (int) input_label_map_1.getWidth();


            int x = 0;
            int y = 0;
            for (int i = 0; i < labels_1.length; i++) {

                int label_1 = (int)labels_1[i];
                int label_2 = (int)labels_2[i];

                tps[label_1][label_2]++;

                x++;
                if (x >= width) {
                    x = 0;
                    y++;
                }
            }
        }
    }


    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        double maxValue1 = clij.op().maximumOfAllPixels((ClearCLBuffer) args[0]) + 1;
        double maxValue2 = clij.op().maximumOfAllPixels((ClearCLBuffer) args[1]) + 1;
        ClearCLBuffer output = clij.createCLBuffer(new long[]{(long)maxValue1, (long)maxValue2}, NativeTypeEnum.Float);
        return output;
    }

    @Override
    public String getDescription() {
        return "Takes two labelmaps with n and m labels_2 and generates a (n+1)*(m+1) matrix where all labels_1 are set to 0 exept those where labels_2 overlap between the label maps. \n\n" +
                "For the remaining labels_1, the value will be between 0 and 1 indicating the overlap as measured by the Jaccard Index.\n" +
                "Major parts of this operation run on the CPU.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    public static void main(String... args) {
        CLIJ2 clij2 = CLIJ2.getInstance();

        ClearCLBuffer image1 = clij2.pushString("" +
                "1 1 0 0\n" +
                "1 1 2 2\n\n" +
                "1 1 0 0\n" +
                "1 1 2 2");

        ClearCLBuffer image2 = clij2.pushString("" +
                "1 1 0 2\n" +
                "1 1 0 2\n\n" +
                "1 1 0 2\n" +
                "1 1 0 2");

        ClearCLBuffer ref = clij2.pushString("" +
                "0.5 0 0.5\n" +
                "0 1 0\n" +
                "0.5 0 0.5");


        ClearCLBuffer jaccard_matrix = clij2.create(3, 3);

        GenerateJaccardIndexMatrix.generateJaccardIndexMatrix(clij2, image1, image2, jaccard_matrix);

        clij2.print(jaccard_matrix);


        ClearCLBuffer jaccard_matrix_0 = clij2.create(3, 3);

        UndefinedToZero.undefinedToZero(clij2, jaccard_matrix, jaccard_matrix_0);

        clij2.print(jaccard_matrix_0);

    }
}
