package net.haesleinhuepf.clij2.plugins;

import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.plugins.StatisticsOfLabelledPixels;
import net.haesleinhuepf.clij2.utilities.HasAuthor;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_labelOverlapCountMap")
public class LabelOverlapCountMap extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput, HasAuthor {
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
        return "Image label_map1, Image label_map2, ByRef Image overlap_count_map_destination";
    }

    @Override
    public boolean executeCL() {
        return labelOverlapCountMap(getCLIJ2(), (ClearCLBuffer) args[0], (ClearCLBuffer) args[1], (ClearCLBuffer) args[2]);
    }

    public static boolean labelOverlapCountMap(CLIJ2 clij2, ClearCLBuffer label_map1, ClearCLBuffer label_map2, ClearCLBuffer overlap_count_map_destination) {

        int number_of_labels_1 = (int)clij2.maximumOfAllPixels(label_map1) + 1;
        int number_of_labels_2 = (int)clij2.maximumOfAllPixels(label_map2) + 1;

        ClearCLBuffer binary_overlap_matrix = clij2.create(new long[]{number_of_labels_1, number_of_labels_2}, NativeTypeEnum.Float);

        clij2.generateBinaryOverlapMatrix(label_map1, label_map2, binary_overlap_matrix);

        ClearCLBuffer overlap_count_array = clij2.create(number_of_labels_1,1, 1);

        // ignore overlap with background
        clij2.setRow(binary_overlap_matrix, 0, 0);

        clij2.sumYProjection(binary_overlap_matrix, overlap_count_array);
        binary_overlap_matrix.close();

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
        return "Takes two label maps, and counts for every label in label map 1 how many labels overlap with it in label map 2.\n\n" +
                "The resulting map is generated from the label map 1 by replacing the labels with the respective count.";
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
