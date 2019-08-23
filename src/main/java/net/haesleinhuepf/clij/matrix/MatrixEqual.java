package net.haesleinhuepf.clij.matrix;


import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_matrixEqual")
public class MatrixEqual extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image input1, Image input2, Image destination";
    }

    @Override
    public boolean executeCL() {
        boolean result = matrixEqual(clij, (ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]), asFloat(args[2]));
        ResultsTable table = ResultsTable.getResultsTable();
        table.incrementCounter();
        table.addValue("MatrixEqual", result?1:0);
        return result;
    }

    public static boolean matrixEqual(CLIJ clij, ClearCLBuffer buffer1, ClearCLBuffer buffer2, Float tolerance) {
        if (buffer1.getWidth() != buffer2.getWidth() ||
            buffer1.getHeight() != buffer2.getHeight() ||
            buffer1.getDepth() != buffer2.getDepth()
        ) {
            System.out.println("Sizes different");
            return false;
        }

        ClearCLBuffer diffBuffer = clij.createCLBuffer(buffer1);
        clij.op().addImagesWeighted(buffer1, buffer2, diffBuffer, 1f, -1f);

        double maxDifference = clij.op().maximumOfAllPixels(diffBuffer);
        double minDifference = clij.op().minimumOfAllPixels(diffBuffer);
        diffBuffer.close();

        if (Math.abs(maxDifference) > tolerance || Math.abs(minDifference) > tolerance ) {
            System.out.println("Difference unequal to zero!");
            return false;
        }


        return true;
    }

    @Override
    public String getDescription() {
        return "Checks if all elements of a matrix are different by less than or equal to a given tolerance.\n" +
                "The result will be put in the results table as 1 if yes and 0 otherwise.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
