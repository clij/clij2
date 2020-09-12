package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

/**
 * Author: @haesleinhuepf
 *         October 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_generateBinaryOverlapMatrix")
public class GenerateBinaryOverlapMatrix extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized {

    @Override
    public boolean executeCL() {
        boolean result = getCLIJ2().generateBinaryOverlapMatrix((ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), (ClearCLBuffer)(args[2]));
        return result;
    }

    public static boolean generateBinaryOverlapMatrix(CLIJ2 clij2, ClearCLBuffer src_label_map1,  ClearCLBuffer src_label_map2, ClearCLBuffer dst_touch_matrix) {

        clij2.set(dst_touch_matrix, 0f);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src_label_map1", src_label_map1);
        parameters.put("src_label_map2", src_label_map2);
        parameters.put("dst_matrix", dst_touch_matrix);

        long[] globalSizes = src_label_map1.getDimensions();

        clij2.activateSizeIndependentKernelCompilation();
        clij2.execute(GenerateBinaryOverlapMatrix.class, "generate_binary_overlap_matrix_" + src_label_map1.getDimension() + "d_x.cl", "generate_binary_overlap_matrix_" + src_label_map1.getDimension() + "d", globalSizes, globalSizes, parameters);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image label_map1, Image label_map2, ByRef Image touch_matrix_destination";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        double maxValue1 = clij.op().maximumOfAllPixels((ClearCLBuffer) args[0]) + 1;
        double maxValue2 = clij.op().maximumOfAllPixels((ClearCLBuffer) args[1]) + 1;
        ClearCLBuffer output = clij.createCLBuffer(new long[]{(long)maxValue1, (long)maxValue2}, NativeTypeEnum.Float);
        return output;
    }

    @Override
    public String getDescription() {
        return "Takes two labelmaps with n and m labels and generates a (n+1)*(m+1) matrix where all pixels are set to 0 exept those where labels overlap between the label maps. \n\n" +
                "For example, if labels 3 in labelmap1 and 4 in labelmap2 are touching then the pixel (3,4) in the matrix will be set to 1.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }


    @Override
    public String getCategories() {
        return "Binary, Label, Measurement, Graph";
    }
}
