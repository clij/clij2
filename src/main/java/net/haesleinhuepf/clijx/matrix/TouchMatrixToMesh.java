package net.haesleinhuepf.clijx.matrix;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

/**
 * Author: @haesleinhuepf
 *         November 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_touchMatrixToMesh")
public class TouchMatrixToMesh extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image pointlist, Image touch_matrix, Image mesh_destination";
    }

    @Override
    public boolean executeCL() {
        ClearCLBuffer pointlist = (ClearCLBuffer) args[0];
        ClearCLBuffer touch_matrix = (ClearCLBuffer) args[1];
        ClearCLBuffer mesh = (ClearCLBuffer) args[2];

        return touchMatrixToMesh(getCLIJx(), pointlist, touch_matrix, mesh);
    }

    public static boolean touchMatrixToMesh(CLIJx clijx, ClearCLBuffer pointlist, ClearCLBuffer touch_matrix, ClearCLBuffer mesh) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("src_pointlist", pointlist);
        parameters.put("src_touch_matrix", touch_matrix);
        parameters.put("dst_mesh", mesh);

        long[] dimensions = {touch_matrix.getDimensions()[0], 1, 1};
        clijx.execute(TouchMatrixToMesh.class, "touch_matrix_to_mesh_3d_x.cl", "touch_matrix_to_mesh_3d", dimensions, dimensions, parameters);
        return true;
    }

    @Override
    public String getDescription() {
        return "Takes a pointlist with dimensions n*d with n point coordinates in d dimensions and a touch matrix of size n*n. and draws lines from all points to points if the corresponding pixel in the touch matrix is 1.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
