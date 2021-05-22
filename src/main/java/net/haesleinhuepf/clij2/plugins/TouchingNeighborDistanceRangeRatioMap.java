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

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_touchingNeighborDistanceRangeRatioMap")
public class TouchingNeighborDistanceRangeRatioMap extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Label Image";
    }

    @Override
    public String getOutputType() {
        return "Image";
    }

    @Override
    public String getParameterHelpText() {
        return "Image input, ByRef Image destination";
    }

    @Override
    public boolean executeCL() {
        return touchingNeighborDistanceRangeRatioMap(getCLIJ2(), (ClearCLBuffer) args[0], (ClearCLBuffer) args[1]);
    }

    @Deprecated
    public static boolean neighborDistanceRangeRatioMap(CLIJ2 clij2, ClearCLBuffer pushed, ClearCLBuffer result) {
        return touchingNeighborDistanceRangeRatioMap(clij2, pushed, result);
    }

    public static boolean touchingNeighborDistanceRangeRatioMap(CLIJ2 clij2, ClearCLBuffer pushed, ClearCLBuffer result) {
        int number_of_labels = (int)clij2.maximumOfAllPixels(pushed);
        ClearCLBuffer touch_matrix = clij2.create(number_of_labels + 1, number_of_labels + 1);
        clij2.generateTouchMatrix(pushed, touch_matrix);

        ClearCLBuffer pointlist = clij2.create(number_of_labels, pushed.getDimension());
        clij2.centroidsOfLabels(pushed, pointlist);

        ClearCLBuffer distance_matrix = clij2.create(number_of_labels + 1, number_of_labels + 1);
        clij2.generateDistanceMatrix(pointlist, pointlist, distance_matrix);

        ClearCLBuffer minimum_distance_vector = clij2.create(number_of_labels + 1, 1, 1);
        ClearCLBuffer maximum_distance_vector = clij2.create(number_of_labels + 1, 1, 1);
        ClearCLBuffer ratio_vector = clij2.create(number_of_labels + 1, 1, 1);


        clij2.minimumDistanceOfTouchingNeighbors(distance_matrix, touch_matrix, minimum_distance_vector);
        MaximumDistanceOfTouchingNeighbors.maximumDistanceOfTouchingNeighbors(clij2, distance_matrix, touch_matrix, maximum_distance_vector);

        clij2.divideImages(maximum_distance_vector, minimum_distance_vector, ratio_vector);

        touch_matrix.close();
        distance_matrix.close();
        pointlist.close();

        // ignore background measurement
        clij2.setColumn(ratio_vector, 0, 0);

        clij2.replaceIntensities(pushed, ratio_vector, result);
        ratio_vector.close();

        return true;
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        return getCLIJ2().create(input.getDimensions(), NativeTypeEnum.Float);
    }

    @Override
    public String getDescription() {
        return "Takes a label map, determines which labels touch and replaces every label with the minimum distance to their neighboring labels.\n\n" +
                "To determine the distances, the centroid of the labels is determined internally.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public String getCategories() {
        return "Measurements, Graph, Label";
    }
}
