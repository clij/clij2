package net.haesleinhuepf.clij.advancedfilters;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;

import org.scijava.plugin.Plugin;

import java.nio.FloatBuffer;
import java.util.HashMap;


/**
 * Author: @haesleinhuepf
 * June 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_excludeLabelsOnEdges")
public class ExcludeLabelsOnEdges extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        double sum = 0;

        Object[] args = openCLBufferArgs();

        ClearCLBuffer label_map_in = (ClearCLBuffer)( args[0]);
        ClearCLBuffer label_map_out = (ClearCLBuffer)( args[1]);

        excludeLabelsOnEdges(clij, label_map_in, label_map_out);

        releaseBuffers(args);

        return true;
    }

    public static boolean excludeLabelsOnEdges(CLIJ clij, ClearCLBuffer label_map_in, ClearCLBuffer label_map_out) {
        int max_label = (int) clij.op().maximumOfAllPixels(label_map_in);
        if (max_label == 0) {
            clij.op().set(label_map_out, 0f);
            return true;
        }
        float[] label_indices = new float[max_label];

        for (int i = 0; i < label_indices.length; i++) {
            label_indices[i] = i;
        }

        ClearCLBuffer label_index_map = clij.create(new long[]{label_indices.length, 1, 1}, label_map_out.getNativeType());
        label_index_map.readFrom(FloatBuffer.wrap(label_indices), true);

        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("src", label_map_in);
        parameters.put("label_index_dst", label_index_map);

        if (label_map_in.getDimension() > 2 && label_map_in.getDepth() > 1) {
            clij.execute(ExcludeLabelsOnEdges.class, "excludeLabelsOnEdges.cl", "exclude_on_edges_z_3d", new long[]{label_map_in.getWidth(), label_map_in.getHeight(), 1}, parameters);
        }
        clij.execute(ExcludeLabelsOnEdges.class, "excludeLabelsOnEdges.cl", "exclude_on_edges_y_3d", new long[]{label_map_in.getWidth(), 1, label_map_in.getDepth()}, parameters );
        clij.execute(ExcludeLabelsOnEdges.class, "excludeLabelsOnEdges.cl", "exclude_on_edges_x_3d", new long[]{1, label_map_in.getHeight(), label_map_in.getDepth()}, parameters );

        label_index_map.writeTo(FloatBuffer.wrap(label_indices), true);

        int count = 1;
        for (int i = 0; i < label_indices.length; i++) {
            if (label_indices[i] > 0) {
                label_indices[i] = count;
                count++;
            }
        }

        label_index_map.readFrom(FloatBuffer.wrap(label_indices), true);

        ConnectedComponentsLabeling.replace(clij, label_map_in, label_index_map, label_map_out);

        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image label_map_input, Image label_map_destination";
    }

    @Override
    public String getDescription() {
        return "Removes all labels from a label map which touch the edges. Remaining label elements are renumbered afterwards.";
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
