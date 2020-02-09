package net.haesleinhuepf.clij2.plugins;


import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_matrixEqual")
public class MatrixEqual extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image input1, Image input2, Number tolerance";
    }

    @Override
    public boolean executeCL() {
        boolean result = matrixEqual(getCLIJ2(), (ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]), asFloat(args[2]));
        ResultsTable table = ResultsTable.getResultsTable();
        table.incrementCounter();
        table.addValue("MatrixEqual", result?1:0);
        return result;
    }

    public static boolean matrixEqual(CLIJ2 clij2, ClearCLBuffer buffer1, ClearCLBuffer buffer2, Float tolerance) {
        if (buffer1.getWidth() != buffer2.getWidth() ||
            buffer1.getHeight() != buffer2.getHeight() ||
            buffer1.getDepth() != buffer2.getDepth()
        ) {
            System.out.println("Sizes different");
            return false;
        }

        ClearCLBuffer diffBuffer = clij2.create(buffer1.getDimensions(), NativeTypeEnum.Float);
        clij2.addImagesWeighted(buffer1, buffer2, diffBuffer, 1f, -1f);

        double maxDifference = clij2.maximumOfAllPixels(diffBuffer);
        double minDifference = clij2.minimumOfAllPixels(diffBuffer);
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
                "The result will be put in the results table in column \"MatrixEqual\" as 1 if yes and 0 otherwise.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
