package net.haesleinhuepf.clijx.matrix;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

/**
 * Author: @haesleinhuepf
 *         October 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_countTouchingNeighbors")
public class CountTouchingNeighbors extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();
        boolean result = countTouchingNeighbors(getCLIJx(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]));
        releaseBuffers(args);
        return result;
    }

    public static boolean countTouchingNeighbors(CLIJx clijx, ClearCLBuffer src_touch_matrix, ClearCLBuffer dst_count_list) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src_touch_matrix", src_touch_matrix);
        parameters.put("dst_count_list", dst_count_list);

        long[] globalSizes = {src_touch_matrix.getWidth(), 1, 1};

        clijx.activateSizeIndependentKernelCompilation();
        clijx.execute(CountTouchingNeighbors.class, "count_touching_neighbors_x.cl", "count_touching_neighbors", globalSizes, globalSizes, parameters);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image touch_matrix, Image touching_neighbors_count_destination";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        ClearCLBuffer output = clij.createCLBuffer(new long[]{input.getWidth(), 1, 1}, NativeTypeEnum.Float);
        return output;
    }

    @Override
    public String getDescription() {
        return "Takes a touching-neighbors-matrix as input and delivers a vector with number of touching neighbors per label as a vector.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D -> 1D";
    }
}
