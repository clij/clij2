package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 *         January 2021
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_generateNNearestNeighborsMatrix")
public class GenerateNNearestNeighborsMatrix extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Matrix";
    }

    @Override
    public String getOutputType() {
        return "Matrix";
    }


    @Override
    public boolean executeCL() {
        boolean result = generateNNearestNeighborsMatrix(getCLIJ2(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asInteger(args[2]));
        return result;
    }

    public static boolean generateNNearestNeighborsMatrix(CLIJ2 clij2, ClearCLBuffer distance_matrix, ClearCLBuffer touch_matrix_destination, Integer n) {
        if (n < 1) {
            System.out.println("Warning: generateNNearestNeighborsMatrix with n < 1 doesn't make much sense. N is set to 1.");
            n = 1;
        }
        ClearCLBuffer index_list = clij2.create(new long[]{distance_matrix.getWidth(), n});
        clij2.setRow(distance_matrix, 0, Float.MAX_VALUE);
        clij2.setColumn(distance_matrix, 0, Float.MAX_VALUE);
        clij2.setWhereXequalsY(distance_matrix, Float.MAX_VALUE);

        NClosestPoints.nClosestPoints(clij2, distance_matrix, index_list, true, true);

        clij2.set(touch_matrix_destination, 0);

        PointIndexListToTouchMatrix.pointIndexListToTouchMatrix(clij2, index_list, touch_matrix_destination);

        clij2.setColumn(touch_matrix_destination, 0, 0);

        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image distance_matrix, ByRef Image touch_matrix_destination, Number n";
    }

    @Override
    public Object[] getDefaultValues() {
        return new Object[]{null, null, 1};
    }

    @Override
    public String getDescription() {
        return "Produces a touch-matrix where the n nearest neighbors are marked as touching neighbors. \n" +
                "\n" +
                "Takes a distance matrix (e.g. derived from a pointlist of centroids) and marks for every column the n smallest\n" +
                "distances as neighbors. The resulting matrix can be use as if it was a touch-matrix (a.k.a. adjacency graph matrix). \n" +
                "\n" +
                "Inspired by a similar implementation in imglib2 [1]\n" +
                "\n" +
                "Note: The implementation is limited to square matrices.\n" +
                "\n" +
                "Parameters\n" +
                "----------\n" +
                "distance_marix : Image\n" +
                "touch_matrix_destination : Image\n" +
                "n : int\n" +
                "   number of neighbors\n" +
                "   \n" +
                "References\n" +
                "----------\n" +
                "[1] https://github.com/imglib/imglib2/blob/master/src/main/java/net/imglib2/interpolation/neighborsearch/InverseDistanceWeightingInterpolator.java\n" +
                "";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public String getCategories() {
        return "Measurement, Graph";
    }
}
