package net.haesleinhuepf.clij2.plugins;


import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.plugins.ReadValuesFromMap;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * January 2021
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_modeOfNNearestNeighborsMap")
public class ModeOfNNearestNeighborsMap extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Image, Label Image";
    }

    @Override
    public String getOutputType() {
        return "Image";
    }

    @Override
    public String getCategories() {
        return "Graph, Measurements, Neighbor-Filter";
    }

    @Override
    public String getParameterHelpText() {
        return "Image parametric_map, Image label_map, ByRef Image parametric_map_destination, Number n";
    }

    @Override
    public Object[] getDefaultValues() {
        return new Object[]{null, null, null, 1};
    }

    @Override
    public boolean executeCL() {
        boolean result = modeOfNNearestNeighborsMap(getCLIJ2(), (ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]),  (ClearCLBuffer) (args[2]), asInteger(args[3]));
        return result;
    }

    public static boolean modeOfNNearestNeighborsMap(CLIJ2 clij2, ClearCLBuffer parametric_map, ClearCLBuffer label_map, ClearCLBuffer parametric_map_destination, Integer n) {

        int number_of_labels = (int) clij2.maximumOfAllPixels(label_map);
        ClearCLBuffer centroids = clij2.create(new long[]{number_of_labels, label_map.getDimension()});
        clij2.centroidsOfLabels(label_map, centroids);

        ClearCLBuffer distance_matrix = clij2.create(new long[]{number_of_labels + 1, number_of_labels + 1});
        clij2.generateDistanceMatrix(centroids, centroids, distance_matrix);
        centroids.close();

        ClearCLBuffer touch_matrix = clij2.create(new long[]{number_of_labels + 1, number_of_labels + 1});

        GenerateNNearestNeighborsMatrix.generateNNearestNeighborsMatrix(clij2, distance_matrix, touch_matrix, n);
        distance_matrix.close();

        ClearCLBuffer intensities = clij2.create(new long[]{number_of_labels + 1 , 1, 1});
        ReadValuesFromMap.readValuesFromMap(clij2, label_map, parametric_map, intensities);

        ClearCLBuffer new_intensities = clij2.create(new long[]{number_of_labels + 1 , 1, 1});
        ModeOfTouchingNeighbors.modeOfTouchingNeighbors(clij2, intensities, touch_matrix, new_intensities);
        intensities.close();
        touch_matrix.close();
        // keep background black
        clij2.setColumn(new_intensities, 0, 0);

        clij2.replaceIntensities(label_map, new_intensities, parametric_map_destination);
        new_intensities.close();

        return true;
    }

    @Override
    public String getDescription() {
        return "Takes a label image and a parametric intensity image and will replace each labels value in the parametric image\n" +
                "by the mode value of neighboring labels. The distance number of nearest neighbors can be configured.\n" +
                "Note: Values of all pixels in a label each must be identical.\n" +
                "\n" +
                "Parameters\n" +
                "----------\n" +
                "parametric_map : Image\n" +
                "label_map : Image\n" +
                "parametric_map_destination : Image\n" +
                "n : int\n" +
                "    number of nearest neighbors";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
