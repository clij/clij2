package net.haesleinhuepf.clijx.plugins;

import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
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
public class VarianceOfMaskedPixels extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        double minVal = varianceOfMaskedPixels(getCLIJx(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]));

        ResultsTable table = ResultsTable.getResultsTable();
        table.incrementCounter();
        table.addValue("Masked_variance", minVal);
        table.show("Results");
        return true;
    }

    public static double varianceOfMaskedPixels(CLIJx clijx, ClearCLBuffer buffer1, ClearCLBuffer mask) {
        double meanIntensity = MeanOfMaskedPixels.meanOfMaskedPixels(clijx, buffer1, mask);
        return varianceOfMaskedPixels(clijx, buffer1, mask, new Float(meanIntensity));
    }

    public static double varianceOfMaskedPixels(CLIJx clijx, ClearCLBuffer buffer1, ClearCLBuffer mask, Float meanIntensity) {
        ClearCLBuffer clReducedImage = buffer1;
        float sum = 0;
        if (buffer1.getDimension() == 3) {
            clReducedImage = clijx.create(new long[]{buffer1.getWidth(), buffer1.getHeight()}, NativeTypeEnum.Float);

            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("src", buffer1);
            parameters.put("src_mask", mask);
            parameters.put("dst", clReducedImage);
            parameters.put("mean_intensity", meanIntensity);
            clijx.execute(VarianceOfMaskedPixels.class, "variance_masked_projection_x.cl", "masked_squared_sum_project", clReducedImage.getDimensions(), clReducedImage.getDimensions(), parameters);
        } else {
            clReducedImage = clijx.create(new long[]{buffer1.getWidth(), buffer1.getHeight()}, NativeTypeEnum.Float);

            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("src", buffer1);
            parameters.put("src_mask", mask);
            parameters.put("dst", clReducedImage);
            parameters.put("mean_intensity", meanIntensity);
            clijx.execute(VarianceOfMaskedPixels.class, "variance_2d_x.cl", "masked_squared_sum_2d_2d", clReducedImage.getDimensions(), clReducedImage.getDimensions(), parameters);
        }

        RandomAccessibleInterval rai = clijx.convert(clReducedImage, RandomAccessibleInterval.class);
        Cursor cursor = Views.iterable(rai).cursor();
        while (cursor.hasNext()) {
            sum += ((RealType) cursor.next()).getRealFloat();
        }

        clReducedImage.close();
        return sum / clijx.sumPixels(mask);
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image mask";
    }

    @Override
    public String getDescription() {
        return "Determines the variance in an image, but only in pixels which have non-zero values in another" +
                " binary mask image. The result is put in the results table as new column named 'Masked_variance'.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
