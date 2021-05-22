package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.HasAuthor;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_labelProximalNeighborCountMap")
public class LabelProximalNeighborCountMap extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput, HasAuthor {
    @Override
    public String getInputType() {
        return "Label Image, Label Image";
    }

    @Override
    public String getOutputType() {
        return "Image";
    }


    @Override
    public String getParameterHelpText() {
        return "Image label_map1, Image label_map2, ByRef Image proximal_neighbor_count_map_destination, Number min_distance, Number max_distance";
    }

    @Override
    public boolean executeCL() {
        return labelProximalNeighborCountMap(getCLIJ2(), (ClearCLBuffer) args[0], (ClearCLBuffer) args[1], (ClearCLBuffer) args[2], asFloat(args[3]), asFloat(args[4]));
    }

    public static boolean labelProximalNeighborCountMap(CLIJ2 clij2, ClearCLBuffer label_map1, ClearCLBuffer label_map2, ClearCLBuffer overlap_count_map_destination, Float min_distance, Float max_distance) {
        int number_of_labels_1 = (int)clij2.maximumOfAllPixels(label_map1) + 1;
        int number_of_labels_2 = (int)clij2.maximumOfAllPixels(label_map2) + 1;

        int dimension = (int) label_map1.getDimension();

        ClearCLBuffer centroids_1 = clij2.create(number_of_labels_1, dimension);
        ClearCLBuffer centroids_2 = clij2.create(number_of_labels_2, dimension);
        clij2.centroidsOfLabels(label_map1, centroids_1);
        clij2.centroidsOfLabels(label_map2, centroids_2);

        ClearCLBuffer distance_matrix = clij2.create(new long[]{number_of_labels_1, number_of_labels_2}, NativeTypeEnum.Float);
        clij2.generateDistanceMatrix(centroids_1, centroids_2, distance_matrix);

        centroids_1.close();
        centroids_2.close();

        // ignore background
        clij2.setColumn(distance_matrix, 0, Float.MAX_VALUE);
        clij2.setRow(distance_matrix, 0, Float.MAX_VALUE);

        ClearCLBuffer below_matrix = clij2.create(distance_matrix);
        ClearCLBuffer above_matrix = clij2.create(distance_matrix);

        clij2.smallerOrEqualConstant(distance_matrix, below_matrix, max_distance);
        clij2.greaterOrEqualConstant(distance_matrix, above_matrix, min_distance);

        ClearCLBuffer proximal_matrix = clij2.create(distance_matrix);
        clij2.binaryAnd(below_matrix, above_matrix, proximal_matrix);
        below_matrix.close();
        above_matrix.close();
        distance_matrix.close();


        ClearCLBuffer overlap_count_array = clij2.create(number_of_labels_1,1, 1);

        clij2.sumYProjection(proximal_matrix, overlap_count_array);
        proximal_matrix.close();

        // ignore background measurement
        clij2.setColumn(overlap_count_array, 0, 0);

        clij2.replaceIntensities(label_map1, overlap_count_array, overlap_count_map_destination);
        overlap_count_array.close();

        return true;
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        return getCLIJ2().create(input.getDimensions(), NativeTypeEnum.Float);
    }

    @Override
    public String getDescription() {
        return "Takes two label maps, and counts for every label in label map 1 how many labels are in a given distance range to it in label map 2.\n\n" +
                "Distances are computed from the centroids of the labels. The resulting map is generated from the label map 1 by replacing the labels with the respective count.";
    }

    @Override
    public Object[] getDefaultValues() {
        return new Object[]{null, null, null, 0, Float.MAX_VALUE};
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public String getCategories() {
        return "Visualisation, Label, Measurements";
    }

    @Override
    public String getAuthorName() {
        return "Robert Haase, Kisha Sivanathan";
    }
}