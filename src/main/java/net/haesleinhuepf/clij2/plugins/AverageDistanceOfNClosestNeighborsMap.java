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

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_averageDistanceOfNClosestNeighborsMap")
public class AverageDistanceOfNClosestNeighborsMap extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
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
        return "Image input, ByRef Image destination, Number n";
    }

    @Override
    public boolean executeCL() {
        return averageDistanceOfNClosestNeighborsMap(getCLIJ2(), (ClearCLBuffer) args[0], (ClearCLBuffer) args[1], asInteger(args[2]));
    }

    public static boolean averageDistanceOfNClosestNeighborsMap(CLIJ2 clij2, ClearCLBuffer pushed, ClearCLBuffer result, Integer n) {
        int number_of_labels = (int)clij2.maximumOfAllPixels(pushed);

        ClearCLBuffer pointlist = clij2.create(number_of_labels, pushed.getDimension());
        clij2.centroidsOfLabels(pushed, pointlist);

        ClearCLBuffer distance_matrix = clij2.create(number_of_labels + 1, number_of_labels + 1);
        clij2.generateDistanceMatrix(pointlist, pointlist, distance_matrix);

        ClearCLBuffer distance_vector = clij2.create(number_of_labels + 1, 1, 1);
        clij2.averageDistanceOfNClosestPoints(distance_matrix, distance_vector, n);
        distance_matrix.close();
        pointlist.close();

        // ignore background measurement
        clij2.setColumn(distance_vector, 0, 0);

        clij2.replaceIntensities(pushed, distance_vector, result);
        distance_vector.close();

        return true;
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        return getCLIJ2().create(input.getDimensions(), NativeTypeEnum.Float);
    }

    @Override
    public String getDescription() {
        return "Takes a label map, determines distances between all centroids and replaces every label with the average distance to the n closest neighboring labels.";
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
