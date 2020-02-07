package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
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
        Object[] args = openCLBufferArgs();
        boolean result = multiplyMatrix(getCLIJ2(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), (ClearCLBuffer)(args[2]));
        releaseBuffers(args);
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

    public static boolean multiplyMatrix_fast_x(CLIJ2 clij2, ClearCLBuffer input1, ClearCLBuffer input2, ClearCLBuffer output) {
        //int wg_size = (int) clij2.getCLIJ().getClearCLContext().getDevice().getMaxWorkGroupSize();
        //System.out.println("wg size " + wg_size);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src1", input1);
        parameters.put("src2", input2);
        parameters.put("dst_matrix", output);
        //parameters.put("work_group_size", wg_size);

        //long[] localSizes = new long[]{wg_size, 1, 1};

        long[] globalSizes = new long[]{1, output.getHeight()};

        clij2.activateSizeIndependentKernelCompilation();
        // System.out.println("hello world");
        clij2.execute(MultiplyMatrix.class, "multiply_matrix_x.cl", "multiply_matrix_fast_x", output.getDimensions(), globalSizes, /*localSizes,*/ parameters);
        return true;
    }

    public static boolean multiplyMatrix_fast_y(CLIJ2 clij2, ClearCLBuffer input1, ClearCLBuffer input2, ClearCLBuffer output) {
        //int wg_size = (int) clij2.getCLIJ().getClearCLContext().getDevice().getMaxWorkGroupSize();
        //System.out.println("wg size " + wg_size);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src1", input1);
        parameters.put("src2", input2);
        parameters.put("dst_matrix", output);
        //parameters.put("work_group_size", wg_size);

        //long[] localSizes = new long[]{wg_size, 1, 1};

        long[] globalSizes = new long[]{output.getWidth(), 1};

        clij2.activateSizeIndependentKernelCompilation();
        // System.out.println("hello world");
        clij2.execute(MultiplyMatrix.class, "multiply_matrix_x.cl", "multiply_matrix_fast_y", output.getDimensions(), globalSizes, /*localSizes,*/ parameters);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image matrix1, Image matrix2, Image matrix_destination";
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
