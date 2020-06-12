package net.haesleinhuepf.clij2.plugins;

import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import net.imglib2.Cursor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

/**
 * Author: @haesleinhuepf
 *         September 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_varianceOfAllPixels")
public class VarianceOfAllPixels extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized {
    @Override
    public String getCategories() {
        return "Measurement";
    }

    @Override
    public boolean executeCL() {
        double variance = 0;

        ClearCLBuffer buffer1 = (ClearCLBuffer)( args[0]);

        variance = getCLIJ2().varianceOfAllPixels(buffer1);

        ResultsTable table = ResultsTable.getResultsTable();
        table.incrementCounter();
        table.addValue("Variance", variance);
        table.show("Results");
        return true;
    }

    public static double varianceOfAllPixels(CLIJ2 clij2, ClearCLBuffer buffer1) {
        double meanIntensity = clij2.sumPixels(buffer1) / (buffer1.getWidth() * buffer1.getHeight() * buffer1.getDepth());
        return varianceOfAllPixels(clij2, buffer1, new Float(meanIntensity));
    }

    public static double varianceOfAllPixels(CLIJ2 clij2, ClearCLImageInterface buffer1, Float meanIntensity) {
        ClearCLImageInterface clReducedImage = buffer1;
        float sum = 0;
        if (buffer1.getDimension() == 3) {
            clReducedImage = clij2.create(new long[]{buffer1.getWidth(), buffer1.getHeight()}, NativeTypeEnum.Float);

            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("src", buffer1);
            parameters.put("dst", clReducedImage);
            parameters.put("mean_intensity", meanIntensity);
            clij2.execute(VarianceOfMaskedPixels.class, "variance_projection_x.cl", "squared_sum_project", clReducedImage.getDimensions(), clReducedImage.getDimensions(), parameters);

            RandomAccessibleInterval rai = clij2.convert(clReducedImage, RandomAccessibleInterval.class);
            Cursor cursor = Views.iterable(rai).cursor();
            while (cursor.hasNext()) {
                sum += ((RealType) cursor.next()).getRealFloat();
            }

            clij2.release(clReducedImage);
        } else {
            RandomAccessibleInterval rai = clij2.convert(clReducedImage, RandomAccessibleInterval.class);
            Cursor cursor = Views.iterable(rai).cursor();
            while (cursor.hasNext()) {
                sum += Math.pow(((RealType) cursor.next()).getRealFloat() - meanIntensity, 2);
            }
        }
        return sum / (buffer1.getWidth() * buffer1.getHeight() * buffer1.getDepth());
    }

    @Override
    public String getParameterHelpText() {
        return "Image source";
    }

    @Override
    public String getDescription() {
        return "Determines the variance of all pixels in an image. \n\nThe value will be stored in a new row of ImageJs\n" +
                "Results table in the column 'Variance'.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
