package net.haesleinhuepf.clij2.plugins;

import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 *         April 2020
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_getMeanSquaredError")
public class GetMeanSquaredError extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized {
    @Override
    public String getCategories() {
        return "Measurements";
    }

    @Override
    public boolean executeCL() {
        double meanSquaredError = 0;

        ClearCLBuffer buffer1 = (ClearCLBuffer)( args[0]);
        ClearCLBuffer buffer2 = (ClearCLBuffer)( args[1]);
        meanSquaredError = getCLIJ2().meanSquaredError(buffer1, buffer2);

        ((Double[]) args[2])[0] = meanSquaredError;

        return true;
    }

    public static double getMeanSquaredError(CLIJ2 clij2, ClearCLBuffer buffer1, ClearCLBuffer buffer2) {
        return clij2.meanSquaredError(buffer1, buffer2);
    }

    @Override
    public String getParameterHelpText() {
        return "Image source1, Image source2, ByRef Number mean_squared_error";
    }

    @Override
    public String getDescription() {
        return "Determines the mean squared error (MSE) between two images. \n\nThe MSE will be stored in the variable mean_squared_error.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
