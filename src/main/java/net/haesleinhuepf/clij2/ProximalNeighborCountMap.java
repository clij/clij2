package net.haesleinhuepf.clij2;

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

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_proximalNeighborCountMap")
public class ProximalNeighborCountMap extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
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
        return "Image input, ByRef Image destination, Number min_distance, Number max_distance";
    }

    @Override
    public boolean executeCL() {
        return proximalNeighborCountMap(getCLIJ2(), (ClearCLBuffer) args[0], (ClearCLBuffer) args[1], asFloat(args[2]), asFloat(args[3]));
    }

    @Override
    public Object[] getDefaultValues() {
        return new Object[]{null, null, 0, Float.MAX_VALUE};
    }

    public static boolean proximalNeighborCountMap(CLIJ2 clij2, ClearCLBuffer pushed, ClearCLBuffer result, Float min_distance, Float max_distance) {
        int number_of_labels = (int)clij2.maximumOfAllPixels(pushed);
        ClearCLBuffer pointlist = clij2.create(number_of_labels, pushed.getDimension());
        clij2.centroidsOfLabels(pushed, pointlist);

        ClearCLBuffer distance_matrix = clij2.create(number_of_labels + 1, number_of_labels + 1);
        clij2.generateDistanceMatrix(pointlist,pointlist,distance_matrix);
        pointlist.close();

        ClearCLBuffer touch_matrix = clij2.create(number_of_labels + 1, number_of_labels + 1);
        GenerateProximalNeighborsMatrix.generateProximalNeighborsMatrix(clij2, distance_matrix, touch_matrix, min_distance, max_distance);
        distance_matrix.close();

        ClearCLBuffer touch_count_vector = clij2.create(number_of_labels + 1, 1, 1);
        clij2.countTouchingNeighbors(touch_matrix, touch_count_vector);
        touch_matrix.close();

        // ignore measurement for background
        clij2.setColumn(touch_count_vector, 0, 0);

        clij2.replaceIntensities(pushed, touch_count_vector, result);
        touch_count_vector.close();

        return true;
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        return getCLIJ2().create(input.getDimensions(), NativeTypeEnum.Float);
    }

    @Override
    public String getDescription() {
        return "Takes a label map, determines which labels are within a given distance range and replaces every label with the number of neighboring labels.\n\n";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }


    @Override
    public String getCategories() {
        return "Measurements, Graph, Label, Visualisation";
    }
}
