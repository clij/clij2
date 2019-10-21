package net.haesleinhuepf.clij.matrix;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

/**
 * Author: @haesleinhuepf
 *         October 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_generateTouchMatrix")
public class GenerateTouchMatrix extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();
        boolean result = generateTouchMatrix(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]));
        releaseBuffers(args);
        return result;
    }

    public static boolean generateTouchMatrix(CLIJ clij, ClearCLBuffer src_label_map, ClearCLBuffer dst_distance_matrix) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src_label_map", src_label_map);
        parameters.put("dst_matrix", dst_distance_matrix);

        long[] globalSizes = src_label_map.getDimensions();

        return clij.execute(GenerateTouchMatrix.class, "touch_matrix.cl", "generate_touch_matrix_" + src_label_map.getDimension() + "d", globalSizes, parameters);
    }

    @Override
    public String getParameterHelpText() {
        return "Image label_map, Image touch_matrix_destination";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        double maxValue = clij.op().maximumOfAllPixels(input) + 1;
        return clij.createCLBuffer(new long[]{(long)maxValue, (long)maxValue}, NativeTypeEnum.Float);
    }

    @Override
    public String getDescription() {
        return "Takes a labelmap with n labels and generates a (n+1)*(n+1) matrix where all pixels are set to 0 exept those where labels are touching." +
                "Only half of the matrix is filled (with x < y). For example, if labels 3 and 4 are touching then the pixel (3,4) in the matrix will be set to 1.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
