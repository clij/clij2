package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

import java.nio.ShortBuffer;
import java.util.HashMap;

/**
 * PointIndexListToMesh
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 12 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_pointIndexListToMesh")
public class PointIndexListToMesh extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image pointlist, Image indexList, ByRef Image mesh_destination";
    }

    @Override
    public boolean executeCL() {
        ClearCLBuffer pointlist = (ClearCLBuffer) args[0];
        ClearCLBuffer indexList = (ClearCLBuffer) args[1];
        ClearCLBuffer mesh = (ClearCLBuffer) args[2];

        return pointIndexListToMesh(getCLIJ2(), pointlist, indexList, mesh);
    }

    public static boolean pointIndexListToMesh(CLIJ2 clij2, ClearCLBuffer pointlist, ClearCLBuffer indexlist, ClearCLBuffer mesh) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("src_pointlist", pointlist);
        parameters.put("src_indexlist", indexlist);
        parameters.put("dst_mesh", mesh);

        long[] dimensions = {indexlist.getDimensions()[0], 1, 1};
        clij2.activateSizeIndependentKernelCompilation();
        clij2.execute(PointIndexListToMesh.class, "pointindexlist_to_mesh_3d_x.cl", "pointindexlist_to_mesh_3d", dimensions, dimensions, parameters);
        return true;
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        ClearCLBuffer pointlist = (ClearCLBuffer) args[0];

        CLIJ2 clij2 = getCLIJ2();
        ClearCLBuffer temp = clij2.create(new long[]{1, pointlist.getHeight()}, NativeTypeEnum.UnsignedShort);

        clij2.maximumXProjection(input, temp);

        short[] array = new short[(int) temp.getHeight()];
        ShortBuffer buffer = ShortBuffer.wrap(array);

        temp.writeTo(buffer, true);
        clij2.release(temp);

        long[] dimensions = new long[array.length];
        for (int d = 0; d < array.length; d++) {
            dimensions[d] = array[d];
        }

        return clij2.create(dimensions, input.getNativeType());
    }

    @Override
    public String getDescription() {
        return "Meshes all points in a given point list which are indiced in a corresponding index list.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}

