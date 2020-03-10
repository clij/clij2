package net.haesleinhuepf.clij2.plugins;

import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * July 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_sorensenDiceCoefficient")
public class SorensenDiceCoefficient extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {

        ClearCLBuffer buffer1 = (ClearCLBuffer)( args[0]);
        ClearCLBuffer buffer2 = (ClearCLBuffer)( args[1]);

        double jaccardIndex = JaccardIndex.jaccardIndex(getCLIJ2(), buffer1, buffer2);
        double sorensenDiceCoefficient = sorensenDiceCoefficient(jaccardIndex);

        ResultsTable table = ResultsTable.getResultsTable();
        table.incrementCounter();
        table.addValue("Sorensen_Dice_coefficient", sorensenDiceCoefficient);
        table.show("Results");
        return true;
    }

    public static double sorensenDiceCoefficient(CLIJ2 clij2, ClearCLBuffer input1, ClearCLBuffer input2) {
        double jaccardIndex = JaccardIndex.jaccardIndex(clij2, input1, input2);
        return sorensenDiceCoefficient(jaccardIndex);
    }

    public static double sorensenDiceCoefficient(double jaccardIndex) {
        return 2.0 * jaccardIndex / (jaccardIndex + 1);
    }

    @Override
    public String getParameterHelpText() {
        return "Image source1, Image source2";
    }

    @Override
    public String getDescription() {
        return "Determines the overlap of two binary images using the Sorensen-Dice coefficent.\n" +
                "A value of 0 suggests no overlap, 1 means perfect overlap.\n" +
                "The Sorensen-Dice coefficient is saved in the colum 'Sorensen_Dice_coefficient'.\n" +
                "Note that the Sorensen-Dice coefficient s can be calculated from the Jaccard index j using this formula:\n" +
                "<pre>s = f(j) = 2 j / (j + 1)</pre>";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
