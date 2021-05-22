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
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_standardDeviationOfTouchingNeighborsMap")
public class StandardDeviationOfTouchingNeighborsMap extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
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
        return "Image parametric_map, Image label_map, ByRef Image parametric_map_destination, Number radius, Boolean ignore_touching_background";
    }

    @Override
    public Object[] getDefaultValues() {
        return new Object[]{null, null, null, 1};
    }

    @Override
    public boolean executeCL() {
        boolean result = standardDeviationOfTouchingNeighborsMap(getCLIJ2(), (ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]),  (ClearCLBuffer) (args[2]), asInteger(args[3]), asBoolean(args[4]));
        return result;
    }

    public static boolean standardDeviationOfTouchingNeighborsMap(CLIJ2 clij2, ClearCLBuffer parametric_map, ClearCLBuffer label_map, ClearCLBuffer parametric_map_destination, Integer radius, Boolean ignore_touching_background) {

        if (radius < 1) {
            clij2.copy(parametric_map, parametric_map_destination);
            return true;
        }

        int number_of_labels = (int) clij2.maximumOfAllPixels(label_map);
        ClearCLBuffer touch_matrix = clij2.create(new long[]{number_of_labels + 1, number_of_labels + 1});

        clij2.generateTouchMatrix(label_map, touch_matrix);

        if (ignore_touching_background) {
            clij2.setColumn(touch_matrix, 0, 0);
        }

        ClearCLBuffer other_matrix = clij2.create(touch_matrix);

        for (int i  = 1; i < radius; i++) {
            clij2.neighborsOfNeighbors(touch_matrix, other_matrix);
            clij2.copy(other_matrix, touch_matrix);
            if (ignore_touching_background) {
                clij2.setColumn(touch_matrix, 0, 0);
            }
        }
        other_matrix.close();

        ClearCLBuffer intensities = clij2.create(new long[]{number_of_labels + 1 , 1, 1});
        ReadValuesFromMap.readValuesFromMap(clij2, label_map, parametric_map, intensities);

        ClearCLBuffer new_intensities = clij2.create(new long[]{number_of_labels + 1 , 1, 1});
        clij2.standardDeviationOfTouchingNeighbors(intensities, touch_matrix, new_intensities);
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
                "by the standard deviation value of touching neighbor labels. The radius of the neighborhood can be configured:\n" +
                "* radius 0: Nothing is replaced\n" +
                "* radius 1: direct neighbors are taken into account\n" +
                "* radius 2: neighbors and neighbors or neighbors are taken into account\n" +
                "* radius n: ...\n" +
                "\n" +
                "Note: Values of all pixels in a label each must be identical.\n" +
                "\n" +
                "Parameters\n" +
                "----------\n" +
                "parametric_map : Image\n" +
                "label_map : Image\n" +
                "parametric_map_destination : Image\n" +
                "radius : int\n" +
                "ignore_touching_background : bool\n" +
                "\n" +
                "Returns\n" +
                "-------\n" +
                "parametric_map_destination";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
