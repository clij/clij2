package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.plugins.GenerateTouchMatrix;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

/**
 * Author: @haesleinhuepf
 *         January 2021
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_generateProximalNeighborsMatrix")
public class GenerateProximalNeighborsMatrix extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
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
        boolean result = generateProximalNeighborsMatrix(getCLIJ2(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asFloat(args[2]), asFloat(args[3]));
        return result;
    }

    public static boolean generateProximalNeighborsMatrix(CLIJ2 clij2, ClearCLBuffer distance_matrix, ClearCLBuffer touch_matrix_destination, Float min_distance, Float max_distance) {
        ClearCLBuffer above_min_distance = clij2.create(distance_matrix);
        clij2.greaterOrEqualConstant(distance_matrix, above_min_distance, min_distance);

        ClearCLBuffer below_max_distance = clij2.create(distance_matrix);
        clij2.smallerOrEqualConstant(distance_matrix, below_max_distance, max_distance);

        clij2.binaryAnd(above_min_distance, below_max_distance, touch_matrix_destination);
        above_min_distance.close();
        below_max_distance.close();

        clij2.setWhereXgreaterThanY(touch_matrix_destination, 0);
        clij2.setWhereXequalsY(touch_matrix_destination, 0);
        clij2.setColumn(touch_matrix_destination, 0, 0);

        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image distance_matrix, ByRef Image touch_matrix_destination, Number min_distance, Number max_distance";
    }

    @Override
    public Object[] getDefaultValues() {
        return new Object[]{null, null, 0, 10};
    }

    @Override
    public String getDescription() {
        return "Produces a touch-matrix where the neighbors within a given distance range are marked as touching neighbors.\n" +
                "\n" +
                "Takes a distance matrix (e.g. derived from a pointlist of centroids) and marks for every column the neighbors whose\n" +
                "distance lie within a given distance range (>= min and <= max). \n" +
                "The resulting matrix can be use as if it was a touch-matrix (a.k.a. adjacency graph matrix). \n" +
                "\n" +
                "Parameters\n" +
                "----------\n" +
                "distance_marix : Image\n" +
                "touch_matrix_destination : Image\n" +
                "min_distance : float, optional\n" +
                "    default : 0\n" +
                "max_distance : float, optional\n" +
                "    default: 10 \n" +
                "\n" +
                "Returns\n" +
                "-------\n" +
                "touch_matrix_destination";
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
