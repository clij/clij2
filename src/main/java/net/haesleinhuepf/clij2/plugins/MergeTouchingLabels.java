package net.haesleinhuepf.clij2.plugins;

import ij.IJ;
import ij.ImageJ;
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

import java.nio.FloatBuffer;
import java.util.HashMap;

/**
 * Author: @haesleinhuepf
 *         July 2020
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_mergeTouchingLabels")
public class MergeTouchingLabels extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Label Image";
    }

    @Override
    public String getOutputType() {
        return "Label Image";
    }


    @Override
    public boolean executeCL() {
        return mergeTouchingLabels(getCLIJ2(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]));
    }

    public static boolean mergeTouchingLabels(CLIJ2 clij2, ClearCLBuffer input, ClearCLBuffer output) {
        int number_of_objects = (int) clij2.maximumOfAllPixels(input);

        ClearCLBuffer touch_matrix = clij2.create(number_of_objects + 1, number_of_objects + 1);
        clij2.generateTouchMatrix(input, touch_matrix);

        ClearCLBuffer adjacency_matrix = clij2.create(number_of_objects + 1, number_of_objects + 1);
        clij2.touchMatrixToAdjacencyMatrix(touch_matrix, adjacency_matrix);
        touch_matrix.close();

        clij2.setWhereXequalsY(adjacency_matrix, 1);

        ClearCLBuffer adjacency_matrix_transposed = clij2.create(number_of_objects + 1, 1, number_of_objects + 1);
        clij2.transposeYZ(adjacency_matrix, adjacency_matrix_transposed);
        adjacency_matrix.close();

        ClearCLBuffer temp1 = clij2.create(adjacency_matrix_transposed);
        ClearCLBuffer temp2 = clij2.create(adjacency_matrix_transposed);
        clij2.setRampZ(temp1);
        clij2.multiplyImages(temp1, adjacency_matrix_transposed, temp2);
        temp1.close();
        adjacency_matrix_transposed.close();

        ClearCLBuffer max = clij2.create(number_of_objects + 1, 1);
        ClearCLBuffer arg_max = clij2.create(number_of_objects + 1, 1);

        clij2.argMaximumZProjection(temp2, max, arg_max);
        temp2.close();

        //clij2.print(arg_max);

        max.close();

        clij2.setColumn(arg_max, 0, 0); // background stays background

        ClearCLBuffer temp = clij2.create(output);

        clij2.replaceIntensities(input, arg_max, temp);
        arg_max.close();


        clij2.closeIndexGapsInLabelMap(temp, output);

        // check if none are touching
        int num_labels = (int) clij2.maximumOfAllPixels(output);
        ClearCLBuffer touch_matrix2 = clij2.create(new long[]{num_labels + 1, num_labels + 1}, clij2.UnsignedByte);
        clij2.generateTouchMatrix(output, touch_matrix2);
        clij2.setColumn(touch_matrix2, 0, 0);
        clij2.setRow(touch_matrix2, 0, 0);
        double num_touches = clij2.sumOfAllPixels(touch_matrix2);
        touch_matrix2.close();

        if (num_touches > 0) {
            clij2.copy(output, temp);
            mergeTouchingLabels(clij2, temp, output);
        }


        temp.close();

        return true;
    }


    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Image destination";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    public static void main(String ... args) {
        new ImageJ();


        CLIJ2 clij2 = CLIJ2.getInstance("HD");
        System.out.println(clij2.getGPUName());

        ClearCLBuffer input = clij2.pushString("" +
                "1 1 0 2 0 3 0 0 0 0\n" +
                "1 1 2 2 0 3 0 0 4 5\n" +
                "1 0 0 2 0 0 0 0 4 5\n" +
                "7 7 7 7 7 7 7 7 7 6"
        );

        ClearCLBuffer output = clij2.create(input);


        mergeTouchingLabels(clij2, input, output);

        clij2.print(output);

        input.close();
        output.close();

        System.out.println(clij2.reportMemory());
    }


    @Override
    public String getCategories() {
        return "Label, Filter";
    }
}
