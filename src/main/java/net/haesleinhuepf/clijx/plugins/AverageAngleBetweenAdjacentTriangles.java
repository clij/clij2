package net.haesleinhuepf.clijx.plugins;


import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_averageAngleBetweenAdjacentTriangles")
public class AverageAngleBetweenAdjacentTriangles extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image pointlist, Image touch_matrix, Image average_distancelist_destination";
    }

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();
        boolean result = averageAngleBetweenAdjacentTriangles(getCLIJx(), (ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]),  (ClearCLBuffer) (args[2]));
        releaseBuffers(args);
        return result;
    }

    public static boolean averageAngleBetweenAdjacentTriangles(CLIJx clijx, ClearCLBuffer pointlist, ClearCLBuffer touch_matrix, ClearCLBuffer dst_average_angle_list) {

        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("src_pointlist", pointlist);
        parameters.put("src_touch_matrix", touch_matrix);
        parameters.put("dst_average_angle_list", dst_average_angle_list);

        long[] globalSizes = new long[]{touch_matrix.getWidth()};

        clijx.activateSizeIndependentKernelCompilation();
        clijx.execute(AverageAngleBetweenAdjacentTriangles.class, "average_angle_between_adjacent_triangles_x.cl", "average_angle_between_adjacent_triangles", globalSizes, globalSizes, parameters);

        return true;
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        return clij.create(new long[]{input.getWidth(), 1, 1}, NativeTypeEnum.Float);
    }

    @Override
    public String getDescription() {
        return "Takes a pointlist and a touch matrix to determine the average angle of adjacent triangles in a surface mesh. For every point, the average angle of adjacent triangles is saved.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }
}
