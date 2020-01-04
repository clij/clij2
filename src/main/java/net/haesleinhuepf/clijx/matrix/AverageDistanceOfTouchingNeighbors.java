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

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_averageDistanceOfTouchingNeighbors")
public class AverageDistanceOfTouchingNeighbors extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image distance_matrix, Image touch_matrix, Image average_distancelist_destination";
    }

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();
        boolean result = averageDistanceOfTouchingNeighbors(getCLIJx(), (ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]),  (ClearCLBuffer) (args[2]));
        releaseBuffers(args);
        return result;
    }

    public static boolean averageDistanceOfTouchingNeighbors(CLIJx clijx, ClearCLBuffer distance_matrix, ClearCLBuffer touch_matrix, ClearCLBuffer average_distancelist_destination) {

        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("src_distance_matrix", distance_matrix);
        parameters.put("src_touch_matrix", touch_matrix);
        parameters.put("dst_average_distance_list", average_distancelist_destination);

        long[] globalSizes = new long[]{distance_matrix.getWidth()};

        clijx.activateSizeIndependentKernelCompilation();
        clijx.execute(AverageDistanceOfTouchingNeighbors.class, "average_distance_of_touching_neighbors_x.cl", "average_distance_of_touching_neighbors", globalSizes, globalSizes, parameters);

        return true;
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        return clij.create(new long[]{input.getWidth(), 1, 1}, NativeTypeEnum.Float);
    }

    @Override
    public String getDescription() {
        return "Takes a touch matrix and a distance matrix to determine the average distance of touching neighbors for every object.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }
}
