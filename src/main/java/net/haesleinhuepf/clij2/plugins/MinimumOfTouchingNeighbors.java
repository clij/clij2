package net.haesleinhuepf.clij2.plugins;


import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_minimumOfTouchingNeighbors")
public class MinimumOfTouchingNeighbors extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image values, Image touch_matrix, ByRef Image minimum_values_destination";
    }

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();
        boolean result = minimumOfTouchingNeighbors(getCLIJ2(), (ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]),  (ClearCLBuffer) (args[2]));
        releaseBuffers(args);
        return result;
    }

    public static boolean minimumOfTouchingNeighbors(CLIJ2 clij2, ClearCLBuffer src_values, ClearCLBuffer touch_matrix, ClearCLBuffer dst_values) {

        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("src_values", src_values);
        parameters.put("src_touch_matrix", touch_matrix);
        parameters.put("dst_values", dst_values);

        // it is possible to use measurent vectors, which have one element less because they don't
        // contain a measurement for the background
        if (touch_matrix.getWidth() == src_values.getWidth() + 1) {
            parameters.put("x_correction", -1);
            System.out.println("cooorrr -1");
        } else {
            parameters.put("x_correction", 0);
            System.out.println("cooorrr 0");
        }

        long[] globalSizes = new long[]{src_values.getWidth()};

        clij2.activateSizeIndependentKernelCompilation();
        clij2.execute(MinimumOfTouchingNeighbors.class, "minimum_of_touching_neighbors_x.cl", "minimum_value_of_touching_neighbors", globalSizes, globalSizes, parameters);

        return true;
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        return clij.create(new long[]{input.getWidth(), 1, 1}, NativeTypeEnum.Float);
    }

    @Override
    public String getDescription() {
        return "Takes a touch matrix and a vector of values to determine the minimum value among touching neighbors for every object.\n" +
                "TODO: This only works for values between 0 and 255 for now.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }


    public static void main(String[] args) {
        new ImageJ();

        ImagePlus imp = IJ.openImage("C:/structure/data/blobs.tif");
        imp.show();

        CLIJ2 clij2 = CLIJ2.getInstance();

        ClearCLBuffer input = clij2.push(imp);
        ClearCLBuffer temp1 = clij2.create(input);
        ClearCLBuffer temp2 = clij2.create(input);
        ClearCLBuffer temp3 = clij2.create(input);

        clij2.thresholdOtsu(input, temp1);
        clij2.voronoiOctagon(temp1, temp2);

        clij2.binaryNot(temp2, temp1);

        clij2.connectedComponentsLabelingBox(temp1, temp3);

        clij2.maximum2DBox(temp3, temp2, 2, 2);

        int number_of_objects = (int) clij2.maximumOfAllPixels(temp2);

        ClearCLBuffer touch_matrix = clij2.create(new long[]{number_of_objects + 1, number_of_objects + 1});
        clij2.generateTouchMatrix(temp2, touch_matrix);

        ResultsTable table = new ResultsTable();
        clij2.statisticsOfLabelledPixels(input, temp2, table);

        ClearCLBuffer column = clij2.create(table.size(), 1, 1);
        clij2.resultsTableColumnToImage(column, table, "MEAN_INTENSITY");


        clij2.replaceIntensities(temp2, column, temp3);
        clij2.show(temp3, "intensity");

        ClearCLBuffer column2 = clij2.create(column);
        clij2.minimumOfTouchingNeighbors(column, touch_matrix, column2);

        clij2.replaceIntensities(temp2, column2, temp3);
        clij2.show(temp3, "local min intensity");

        clij2.show(column, "col");
        clij2.show(column2, "col2");







    }
}
