package net.haesleinhuepf.clij.matrix;


import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij.test.TestUtilities;
import org.scijava.plugin.Plugin;

import java.util.HashMap;


@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_gaussJordan")
public class GaussJordan extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image A_matrix, Image B_result_vector, Image solution_destination";
    }

    @Override
    public boolean executeCL() {
        boolean result = gaussJordan(clij, (ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]),  (ClearCLBuffer) (args[2]));
        return result;
    }

    public static boolean gaussJordan(CLIJ clij, ClearCLBuffer matrix, ClearCLBuffer resultBuffer, ClearCLBuffer coefficients_destination) {


        HashMap<String, Object> parameters = new HashMap<>();
        int size = (int) matrix.getWidth();

        ClearCLBuffer temp = clij.create(matrix);
        clij.op().set(temp, 0f);

        for (int t = 0; t < (size-1); t++) {
            System.out.println("mat");
            TestUtilities.printBuffer(CLIJ.getInstance(), matrix);
            System.out.println("res");
            TestUtilities.printBuffer(CLIJ.getInstance(), resultBuffer);
            System.out.println("coe");
            TestUtilities.printBuffer(CLIJ.getInstance(), coefficients_destination);

            parameters.clear();
            parameters.put("dst_m_dev", temp);
            parameters.put("src_a_dev", matrix);
            parameters.put("size", new Integer(size));
            parameters.put("t", new Integer(t));

            clij.execute(MultiplyMatrix.class, "gaussJordan.cl", "Fan1", new long[]{size}, parameters);
            //System.out.println("tem");
            //TestUtilities.printBuffer(CLIJ.getInstance(), temp);

            parameters.clear();
            parameters.put("src_m_dev", temp);
            parameters.put("src_a_dev", matrix);
            parameters.put("dst_b_dev", resultBuffer);
            parameters.put("size", new Integer(size));
            parameters.put("t", new Integer(t));
            clij.execute(MultiplyMatrix.class, "gaussJordan.cl", "Fan2", new long[]{size, size}, parameters);


            //System.out.println("mat");
            //TestUtilities.printBuffer(CLIJ.getInstance(), matrix);
            //System.out.println("res");
            //TestUtilities.printBuffer(CLIJ.getInstance(), resultBuffer);
            //System.out.println("tem");
            //TestUtilities.printBuffer(CLIJ.getInstance(), temp);
        }

        parameters.clear();
        parameters.put("src_a_dev", matrix);
        parameters.put("src_b_dev", resultBuffer);
        parameters.put("dst_finalVec", coefficients_destination);
        parameters.put("size", new Integer(size));
        clij.execute(MultiplyMatrix.class, "gaussJordan.cl", "BackSub", new long[]{1,1,1}, parameters);
        return true;
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        return clij.create((ClearCLBuffer)args[1]);
    }

    @Override
    public String getDescription() {
        return "Gauss Jordan elimination algorithm for solving linear equation systems. " +
                "Ent the equation coefficients as an n*n sized image A and an n*1 sized image B:" +
                "\n" +
                "<pre>" +
                "a(1,1)*x + a(2,1)*y + a(3,1)+z = b(1)\n" +
                "a(2,1)*x + a(2,2)*y + a(3,2)+z = b(2)\n" +
                "a(3,1)*x + a(3,2)*y + a(3,3)+z = b(3)\n" +
                "</pre>\n" +
                "The results will then be given in an n*1 image with values [x, y, z].\n" +
                "\n" +
                "Adapted from: \n" +
                "https://github.com/qbunia/rodinia/blob/master/opencl/gaussian/gaussianElim_kernels.cl\n" +
                "L.G. Szafaryn, K. Skadron and J. Saucerman. \"Experiences Accelerating MATLAB Systems\n" +
                "//Biology Applications.\" in Workshop on Biomedicine in Computing (BiC) at the International\n" +
                "//Symposium on Computer Architecture (ISCA), June 2009.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }
}
