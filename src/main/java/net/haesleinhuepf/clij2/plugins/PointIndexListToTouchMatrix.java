package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.plugins.PointIndexListToMesh;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

import java.nio.ShortBuffer;
import java.util.HashMap;

/**
 * Author: @haesleinhuepf
 * January 2021
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_pointIndexListToTouchMatrix")
public class PointIndexListToTouchMatrix extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Indexlist";
    }

    @Override
    public String getOutputType() {
        return "Matrix";
    }


    @Override
    public String getParameterHelpText() {
        return "Image indexlist, ByRef Image matrix_destination";
    }

    @Override
    public boolean executeCL() {
        ClearCLBuffer indexList = (ClearCLBuffer) args[0];
        ClearCLBuffer mesh = (ClearCLBuffer) args[1];

        return pointIndexListToTouchMatrix(getCLIJ2(), indexList, mesh);
    }

    public static boolean pointIndexListToTouchMatrix(CLIJ2 clij2, ClearCLBuffer indexlist, ClearCLBuffer matrix_destination) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("src_indexlist", indexlist);
        parameters.put("dst_matrix", matrix_destination);

        long[] dimensions = {indexlist.getDimensions()[0], 1, 1};
        clij2.activateSizeIndependentKernelCompilation();
        clij2.execute(PointIndexListToTouchMatrix.class, "point_index_list_to_touch_matrix_x.cl", "point_index_list_to_touch_matrix", dimensions, dimensions, parameters);
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
        return "Takes a list of point indices to generate a touch matrix (a.k.a. adjacency graph matrix) out of it. \n\nThe list has" +
                "a dimensionality of m*n for the points 1... m (0 a.k.a. background is not in this list). In the n rows, there are\n" +
                "indices to points which should be connected.\n" +
                "\n" +
                "Parameters\n" +
                "----------\n" +
                "indexlist : Image\n" +
                "matrix_destination : Image";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public String getCategories() {
        return "Graph";
    }
}

