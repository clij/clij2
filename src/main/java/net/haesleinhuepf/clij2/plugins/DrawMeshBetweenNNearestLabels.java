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

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_drawMeshBetweenNNearestLabels")
public class DrawMeshBetweenNNearestLabels extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
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
        return "Image input, ByRef Image destination, Number number_of_closest_labels";
    }

    @Override
    public boolean executeCL() {
        return drawMeshBetweenNNearestLabels(getCLIJ2(), (ClearCLBuffer) args[0], (ClearCLBuffer) args[1], asInteger(args[2]));
    }

    public static boolean drawMeshBetweenNNearestLabels(CLIJ2 clij2, ClearCLBuffer pushed, ClearCLBuffer result, Integer number_of_closest_labels) {
        int number_of_labels = (int)clij2.maximumOfAllPixels(pushed);
        //System.out.println("Labels count " + number_of_labels);

        ClearCLBuffer pointlist = clij2.create(number_of_labels, pushed.getDimension());
        clij2.centroidsOfLabels(pushed, pointlist);

        ClearCLBuffer distance_matrix = clij2.create(number_of_labels + 1, number_of_labels + 1);
        clij2.generateDistanceMatrix(pointlist, pointlist, distance_matrix);

        clij2.setWhereXequalsY(distance_matrix, Float.MAX_VALUE);
        clij2.setRow(distance_matrix, 0, Float.MAX_VALUE);
        clij2.setColumn(distance_matrix, 0, Float.MAX_VALUE);

        if (number_of_closest_labels < 1) {
            pointlist.close();
            distance_matrix.close();

            return false;
        }

        ClearCLBuffer indexlist = clij2.create(number_of_labels, number_of_closest_labels);

        clij2.nClosestPoints(distance_matrix, indexlist);

        clij2.set(result, 0);
        clij2.pointIndexListToMesh(pointlist, indexlist, result);

        pointlist.close();
        indexlist.close();
        distance_matrix.close();


        return true;
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        return getCLIJ2().create(input.getDimensions(), NativeTypeEnum.Float);
    }

    @Override
    public String getDescription() {
        return "Starting from a label map, draw lines between n closest labels for each label resulting in a mesh.\n\n" +
                "The end points of the lines correspond to the centroids of the labels. ";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public String getCategories() {
        return "Measurement, Graph, Label";
    }
}
