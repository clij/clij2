package net.haesleinhuepf.clijx.matrix;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

/**
 * PointIndexListToMesh
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 12 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_pointIndexListToMesh")
public class PointIndexListToMesh extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image pointlist, Image indexList, Image Mesh";
    }

    @Override
    public boolean executeCL() {
        ClearCLBuffer pointlist = (ClearCLBuffer) args[0];
        ClearCLBuffer indexList = (ClearCLBuffer) args[1];
        ClearCLBuffer mesh = (ClearCLBuffer) args[2];

        return pointIndexListToMesh(getCLIJx(), pointlist, indexList, mesh);
    }

    public static boolean pointIndexListToMesh(CLIJx clijx, ClearCLBuffer pointlist, ClearCLBuffer indexlist, ClearCLBuffer mesh) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("src_pointlist", pointlist);
        parameters.put("src_indexlist", indexlist);
        parameters.put("dst_mesh", mesh);

        long[] dimensions = {indexlist.getDimensions()[0], 1, 1};
        clijx.activateSizeIndependentKernelCompilation();
        clijx.execute(PointIndexListToMesh.class, "pointindexlist_to_mesh_3d_x.cl", "pointindexlist_to_mesh_3d", dimensions, dimensions, parameters);
        return true;
    }

    @Override
    public String getDescription() {
        return "Meshes all points in a given point list which are indiced in a corresponding index list. TODO: Explain better";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}

