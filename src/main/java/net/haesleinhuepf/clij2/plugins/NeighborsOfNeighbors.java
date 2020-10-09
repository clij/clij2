package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 *         May 2020
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_neighborsOfNeighbors")
public class NeighborsOfNeighbors extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Matrix";
    }

    @Override
    public String getOutputType() {
        return "Matrix";
    }


    @Override
    public String getParameterHelpText() {
        return "Image touch_matrix, ByRef Image neighbor_matrix_destination";
    }

    @Override
    public boolean executeCL() {
        ClearCLBuffer touch_matrix = (ClearCLBuffer) args[0];
        ClearCLBuffer neighbor_matrix = (ClearCLBuffer) args[1];

        return neighborsOfNeighbors(getCLIJ2(), touch_matrix, neighbor_matrix);
    }

    public static boolean neighborsOfNeighbors(CLIJ2 clij2, ClearCLBuffer touch_matrix, ClearCLBuffer neighbor_matrix) {
        ClearCLBuffer temp = clij2.create(touch_matrix);
        clij2.multiplyMatrix(touch_matrix, touch_matrix, temp);
        clij2.greaterConstant(temp, neighbor_matrix, 0);
        temp.close();
        return true;
    }

    @Override
    public String getDescription() {
        return "Determines neighbors of neigbors from touch matrix and saves the result as a new touch matrix.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public String getCategories() {
        return "Graph, Filter";
    }
}
