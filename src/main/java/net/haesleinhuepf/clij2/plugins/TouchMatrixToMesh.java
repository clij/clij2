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
 *         November 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_touchMatrixToMesh")
public class TouchMatrixToMesh extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Matrix";
    }

    @Override
    public String getOutputType() {
        return "Image";
    }


    @Override
    public String getCategories() {
        return "Visualisation, Graph";
    }

    @Override
    public String getParameterHelpText() {
        return "Image pointlist, Image touch_matrix, ByRef Image mesh_destination";
    }

    @Override
    public boolean executeCL() {
        ClearCLBuffer pointlist = (ClearCLBuffer) args[0];
        ClearCLBuffer touch_matrix = (ClearCLBuffer) args[1];
        ClearCLBuffer mesh = (ClearCLBuffer) args[2];

        return getCLIJ2().touchMatrixToMesh(pointlist, touch_matrix, mesh);
    }

    public static boolean touchMatrixToMesh(CLIJ2 clij2, ClearCLBuffer pointlist, ClearCLBuffer touch_matrix, ClearCLBuffer mesh) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("src_pointlist", pointlist);
        parameters.put("src_touch_matrix", touch_matrix);
        parameters.put("dst_mesh", mesh);

        long[] dimensions = {touch_matrix.getDimensions()[0], 1, 1};
        clij2.activateSizeIndependentKernelCompilation();
        clij2.execute(TouchMatrixToMesh.class, "touch_matrix_to_mesh_3d_x.cl", "touch_matrix_to_mesh_3d", dimensions, dimensions, parameters);
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
        return "Takes a pointlist with dimensions n*d with n point coordinates in d dimensions and a touch matrix of \n" +
                "size n*n to draw lines from all points to points if the corresponding pixel in the touch matrix is 1.\n\n" +
                "Parameters\n" +
                "----------\n" +
                "pointlist : Image\n" +
                "    n*d matrix representing n coordinates with d dimensions.\n" +
                "touch_matrix : Image\n" +
                "    A 2D binary matrix with 1 in pixels (i,j) where label i touches label j.\n" +
                "mehs_destination : Image\n" +
                "    The output image where results are written into.\n";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
