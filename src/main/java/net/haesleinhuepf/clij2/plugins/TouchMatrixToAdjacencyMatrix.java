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

import java.nio.ShortBuffer;
import java.util.HashMap;

/**
 * Author: @haesleinhuepf
 *         April 2020
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_touchMatrixToAdjacencyMatrix")
public class TouchMatrixToAdjacencyMatrix extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Matrix";
    }

    @Override
    public String getOutputType() {
        return "Matrix";
    }


    @Override
    public String getCategories() {
        return "Graph, Transform";
    }

    @Override
    public String getParameterHelpText() {
        return "Image touch_matrix, ByRef Image adjacency_matrix";
    }

    @Override
    public boolean executeCL() {
        ClearCLBuffer touch_matrix = (ClearCLBuffer) args[0];
        ClearCLBuffer adjacency_matrix = (ClearCLBuffer) args[1];

        return getCLIJ2().touchMatrixToAdjacencyMatrix(touch_matrix, adjacency_matrix);
    }

    public static boolean touchMatrixToAdjacencyMatrix(CLIJ2 clij2, ClearCLBuffer touch_matrix, ClearCLBuffer adjacency_matrix) {
        ClearCLBuffer temp = clij2.create(touch_matrix);
        clij2.transposeXY(touch_matrix, temp);
        clij2.addImages(temp, touch_matrix, adjacency_matrix);

        temp.close();

        return true;
    }

    @Override
    public String getDescription() {
        return "Converts a touch matrix in an adjacency matrix";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
