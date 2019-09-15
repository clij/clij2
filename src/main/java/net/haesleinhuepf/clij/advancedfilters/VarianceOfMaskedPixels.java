package net.haesleinhuepf.clij.advancedfilters;

import ij.gui.WaitForUserDialog;
import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.advancedmath.EqualConstant;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.imglib2.Cursor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_varianceOfMaskedPixels")
public class VarianceOfMaskedPixels extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        double minVal = varianceOfMaskedPixels(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]));

        ResultsTable table = ResultsTable.getResultsTable();
        table.incrementCounter();
        table.addValue("Masked_variance", minVal);
        table.show("Results");
        return true;
    }

    public static double varianceOfMaskedPixels(CLIJ clij, ClearCLBuffer buffer1, ClearCLBuffer mask) {
        double meanIntensity = MeanOfMaskedPixels.meanOfMaskedPixels(clij, buffer1, mask);
        return varianceOfMaskedPixels(clij, buffer1, mask, new Float(meanIntensity));
    }

    public static double varianceOfMaskedPixels(CLIJ clij, ClearCLBuffer buffer1, ClearCLBuffer mask, Float meanIntensity) {
        ClearCLBuffer clReducedImage = buffer1;
        float sum = 0;
        if (buffer1.getDimension() == 3) {
            clReducedImage = clij.createCLBuffer(new long[]{buffer1.getWidth(), buffer1.getHeight()}, NativeTypeEnum.Float);

            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("src", buffer1);
            parameters.put("src_mask", mask);
            parameters.put("dst", clReducedImage);
            parameters.put("mean_intensity", meanIntensity);
            clij.execute(VarianceOfMaskedPixels.class, "varianceProject.cl", "masked_squared_sum_project_3d_2d", parameters);
        } else {
            clReducedImage = clij.createCLBuffer(new long[]{buffer1.getWidth(), buffer1.getHeight()}, NativeTypeEnum.Float);

            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("src", buffer1);
            parameters.put("src_mask", mask);
            parameters.put("dst", clReducedImage);
            parameters.put("mean_intensity", meanIntensity);
            clij.execute(VarianceOfMaskedPixels.class, "variance2d.cl", "masked_squared_sum_2d_2d", parameters);
        }

        RandomAccessibleInterval rai = clij.convert(clReducedImage, RandomAccessibleInterval.class);
        Cursor cursor = Views.iterable(rai).cursor();
        while (cursor.hasNext()) {
            sum += ((RealType) cursor.next()).getRealFloat();
        }

        clReducedImage.close();
        return sum / clij.op().sumPixels(mask);
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image mask";
    }

    @Override
    public String getDescription() {
        return "Determines the variance in an image, but only in pixels which have non-zero values in another" +
                " binary mask image.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
