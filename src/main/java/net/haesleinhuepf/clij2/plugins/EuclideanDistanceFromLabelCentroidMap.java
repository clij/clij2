package net.haesleinhuepf.clij2.plugins;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.clearcl.ClearCL;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.plugins.OnlyzeroOverwriteMaximumBox;
import net.haesleinhuepf.clij2.plugins.StatisticsOfLabelledPixels;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_euclideanDistanceFromLabelCentroidMap")
public class EuclideanDistanceFromLabelCentroidMap extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
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
        return "Image labelmap_input, ByRef Image destination";
    }

    @Override
    public boolean executeCL() {
        return euclideanDistanceFromLabelCentroidMap(getCLIJ2(), (ClearCLBuffer) args[0], (ClearCLBuffer) args[1]);
    }

    public static boolean euclideanDistanceFromLabelCentroidMap(CLIJ2 clij2, ClearCLBuffer pushed, ClearCLBuffer result) {
        int number_of_labels = (int)clij2.maximumOfAllPixels(pushed);
        ClearCLBuffer pointlist = clij2.create(number_of_labels + 1,pushed.getDimension(), 1);

        clij2.centroidsOfBackgroundAndLabels(pushed, pointlist);

        HashMap<String, Object> parameters = new HashMap();
        parameters.put("src", pushed);
        parameters.put("pointlist", pointlist);
        parameters.put("dst", result);
        clij2.execute(EuclideanDistanceFromLabelCentroidMap.class, "euclidean_distance_from_label_centroid_map_x.cl", "euclidean_distance_from_label_centroid_map", result.getDimensions(), result.getDimensions(), parameters);

        pointlist.close();

        return true;
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        return getCLIJ2().create(input.getDimensions(), NativeTypeEnum.Float);
    }

    @Override
    public String getDescription() {
        return "Takes a label map, determines the centroids of all labels and writes the distance of all labelled pixels to their centroid in the result image.\nBackground pixels stay zero.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public String getCategories() {
        return "Label, Measurements";
    }

}
