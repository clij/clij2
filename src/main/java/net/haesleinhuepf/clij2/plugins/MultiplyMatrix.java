package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

/**
 * Author: @haesleinhuepf
 *         August 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_multiplyMatrix")
public class MultiplyMatrix extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        boolean result = getCLIJ2().multiplyMatrix((ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), (ClearCLBuffer)(args[2]));
        return result;
    }

    public static boolean multiplyMatrix(CLIJ2 clij2, ClearCLBuffer input1, ClearCLBuffer input2, ClearCLBuffer output) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src1", input1);
        parameters.put("src2", input2);
        parameters.put("dst_matrix", output);

        clij2.activateSizeIndependentKernelCompilation();
        clij2.execute(MultiplyMatrix.class, "multiply_matrix_x.cl", "multiply_matrix", output.getDimensions(), output.getDimensions(), parameters);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image matrix1, Image matrix2, ByRef Image matrix_destination";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        ClearCLBuffer input1 = (ClearCLBuffer) args[0];
        ClearCLBuffer input2 = (ClearCLBuffer) args[1];
        return clij.createCLBuffer(new long[]{input2.getWidth(), input1.getHeight()}, NativeTypeEnum.Float);
    }

    @Override
    public String getDescription() {
        return "Multiplies two matrices with each other.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }
}
