package net.haesleinhuepf.clijx.advancedfilters;

import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * July 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_jaccardIndex")
public class JaccardIndex extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {

        ClearCLBuffer buffer1 = (ClearCLBuffer)( args[0]);
        ClearCLBuffer buffer2 = (ClearCLBuffer)( args[1]);

        double jaccardIndex = jaccardIndex(clij, buffer1, buffer2);

        ResultsTable table = ResultsTable.getResultsTable();
        table.incrementCounter();
        table.addValue("Jaccard_Index", jaccardIndex);
        table.show("Results");
        return true;
    }

    public static double jaccardIndex(CLIJ clij, ClearCLBuffer input1, ClearCLBuffer input2) {
        ClearCLBuffer intersection = clij.create(input1.getDimensions(), NativeTypeEnum.Byte);
        ClearCLBuffer union = clij.create(input1.getDimensions(), NativeTypeEnum.Byte);

        BinaryIntersection.binaryIntersection(clij, input1, input2, intersection);
        BinaryUnion.binaryUnion(clij, input1, input2, union);

        double countIntersection = CountNonZeroPixels.countNonZeroPixels(clij, intersection);
        double countUnion = CountNonZeroPixels.countNonZeroPixels(clij, union);

        intersection.close();
        union.close();

        double jaccardIndex = countIntersection / countUnion;
        return jaccardIndex;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source1, Image source2";
    }

    @Override
    public String getDescription() {
        return "Determines the overlap of two binary images using the Jaccard index.\n" +
                "A value of 0 suggests no overlap, 1 means perfect overlap.\n" +
                "The resulting Jaccard index is saved to the results table in the 'Jaccard_Index' column.\n" +
                "Note that the Sorensen-Dice coefficient can be calculated from the Jaccard index j using this formula:\n" +
                "<pre>s = f(j) = 2 j / (j + 1)</pre>";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
