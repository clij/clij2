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
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_excludeLabelsOutsideSizeRange")
public class ExcludeLabelsOutsideSizeRange extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Label Image";
    }

    @Override
    public String getOutputType() {
        return "Label Image";
    }

    @Override
    public Object[] getDefaultValues() {
        return new Object[]{null, null, 100, 1000};
    }

    @Override
    public String getParameterHelpText() {
        return "Image input, ByRef Image destination, Number minimum_size, Number maximum_size";
    }

    @Override
    public boolean executeCL() {
        return excludeLabelsOutsideSizeRange(getCLIJ2(), (ClearCLBuffer) args[0], (ClearCLBuffer) args[1], asFloat(args[2]), asFloat(args[3]));
    }

    public static boolean excludeLabelsOutsideSizeRange(CLIJ2 clij2, ClearCLBuffer pushed, ClearCLBuffer result, Float minimum_size, Float maximum_size) {
        int number_of_labels = (int)clij2.maximumOfAllPixels(pushed);
        ClearCLBuffer size_array = clij2.create(number_of_labels + 1,1, 1);

        ResultsTable table = new ResultsTable();
        clij2.statisticsOfBackgroundAndLabelledPixels(pushed, pushed, table);

        clij2.pushResultsTableColumn(size_array, table, StatisticsOfLabelledPixels.STATISTICS_ENTRY.PIXEL_COUNT.toString());

        ClearCLBuffer below_min = clij2.create(new long[]{number_of_labels + 1,1, 1}, NativeTypeEnum.UnsignedByte);
        clij2.smallerConstant(size_array, below_min, minimum_size);

        ClearCLBuffer above_max = clij2.create(new long[]{number_of_labels + 1,1, 1}, NativeTypeEnum.UnsignedByte);
        clij2.greaterConstant(size_array, above_max, maximum_size);

        clij2.binaryOr(above_max, below_min, size_array);
        clij2.excludeLabels(size_array, pushed, result);

        above_max.close();
        below_min.close();
        size_array.close();

        return true;
    }

    @Override
    public String getDescription() {
        return "Removes labels from a label map which are not within a certain size range.\n\n" +
                "Size of the labels is given as the number of pixel or voxels per label.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public String getCategories() {
        return "Label, Filter";
    }
}
