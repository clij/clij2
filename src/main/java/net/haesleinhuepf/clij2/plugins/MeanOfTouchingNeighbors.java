package net.haesleinhuepf.clij2.plugins;


import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_meanOfTouchingNeighbors")
public class MeanOfTouchingNeighbors extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Image, Matrix";
    }

    @Override
    public String getOutputType() {
        return "Image";
    }

    @Override
    public String getCategories() {
        return "Graph, Measurements";
    }

    @Override
    public String getParameterHelpText() {
        return "Image values, Image touch_matrix, ByRef Image mean_values_destination";
    }

    @Override
    public boolean executeCL() {
        boolean result = getCLIJ2().meanOfTouchingNeighbors((ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]),  (ClearCLBuffer) (args[2]));
        return result;
    }

    public static boolean meanOfTouchingNeighbors(CLIJ2 clij2, ClearCLBuffer src_values, ClearCLBuffer touch_matrix, ClearCLBuffer dst_values) {

        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("src_values", src_values);
        parameters.put("src_touch_matrix", touch_matrix);
        parameters.put("dst_values", dst_values);

        // it is possible to use measurent vectors, which have one element less because they don't
        // contain a measurement for the background
        if (touch_matrix.getWidth() == src_values.getWidth() + 1) {
            parameters.put("x_correction", -1);
        } else {
            parameters.put("x_correction", 0);
        }

        long[] globalSizes = new long[]{src_values.getWidth()};

        clij2.activateSizeIndependentKernelCompilation();
        clij2.execute(MeanOfTouchingNeighbors.class, "mean_of_touching_neighbors_x.cl", "mean_value_of_touching_neighbors", globalSizes, globalSizes, parameters);

        return true;
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        return clij.create(new long[]{input.getWidth(), 1, 1}, NativeTypeEnum.Float);
    }

    @Override
    public String getDescription() {
        return "Takes a touch matrix and a vector of values to determine the mean value among touching neighbors for every object. \n\n" +
                "Parameters\n" +
                "----------\n" +
                "values : Image\n" +
                "    A vector of values corresponding to the labels of which the mean average should be determined.\n" +
                "touch_matrix : Image\n" +
                "    A touch_matrix specifying which labels are taken into account for neighborhood relationships.\n" +
                "mean_values_destination : Image\n" +
                "    A the resulting vector of mean average values in the neighborhood.\n";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }
}
