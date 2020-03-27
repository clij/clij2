package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

import java.nio.FloatBuffer;
import java.util.HashMap;


/**
 * Author: @haesleinhuepf
 * June 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_excludeLabelsOnEdges")
public class ExcludeLabelsOnEdges extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        ClearCLBuffer label_map_in = (ClearCLBuffer)( args[0]);
        ClearCLBuffer label_map_out = (ClearCLBuffer)( args[1]);

        return excludeLabelsOnEdges(getCLIJ2(), label_map_in, label_map_out);
    }

    public static boolean excludeLabelsOnEdges(CLIJ2 clij2, ClearCLBuffer label_map_in, ClearCLBuffer label_map_out) {
        int max_label = (int) clij2.maximumOfAllPixels(label_map_in);
        if (max_label == 0) {
            clij2.set(label_map_out, 0f);
            return true;
        }
        float[] label_indices = new float[max_label];

        for (int i = 0; i < label_indices.length; i++) {
            label_indices[i] = i;
        }

        ClearCLBuffer label_index_map = clij2.create(new long[]{label_indices.length, 1, 1}, clij2.Float);
        label_index_map.readFrom(FloatBuffer.wrap(label_indices), true);

        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("src", label_map_in);
        parameters.put("label_index_dst", label_index_map);

        long[] dims;
        if (label_map_in.getDimension() > 2 && label_map_in.getDepth() > 1) {
            dims =  new long[]{label_map_in.getWidth(), label_map_in.getHeight(), 1};
            clij2.execute(ExcludeLabelsOnEdges.class, "excludeLabelsOnEdges_3d_x.cl", "exclude_on_edges_z_3d", dims, dims, parameters);
        }
        dims = new long[]{label_map_in.getWidth(), 1, label_map_in.getDepth()};
        clij2.execute(ExcludeLabelsOnEdges.class, "excludeLabelsOnEdges_3d_x.cl", "exclude_on_edges_y_3d", dims, dims, parameters );
        dims = new long[]{1, label_map_in.getHeight(), label_map_in.getDepth()};
        clij2.execute(ExcludeLabelsOnEdges.class, "excludeLabelsOnEdges_3d_x.cl", "exclude_on_edges_x_3d", dims, dims, parameters );

        label_index_map.writeTo(FloatBuffer.wrap(label_indices), true);

        int count = 1;
        for (int i = 0; i < label_indices.length; i++) {
            if (label_indices[i] > 0) {
                label_indices[i] = count;
                count++;
            }
        }

        label_index_map.readFrom(FloatBuffer.wrap(label_indices), true);

        clij2.replaceIntensities(label_map_in, label_index_map, label_map_out);

        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image label_map_input, ByRef Image label_map_destination";
    }

    @Override
    public String getDescription() {
        return "Removes all labels from a label map which touch the edges of the image (in X, Y and Z if the image is 3D). " +
                "Remaining label elements are renumbered afterwards.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        return clij.create(input.getDimensions(), NativeTypeEnum.Float);
    }
}
