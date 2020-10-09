package net.haesleinhuepf.clij2.plugins;

import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import net.imglib2.Cursor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

/**
 * Author: @haesleinhuepf
 * June 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_countNonZeroPixels")
public class CountNonZeroPixels extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Image";
    }

    @Override
    public String getOutputType() {
        return "Image";
    }

    @Override
    public String getCategories() {
        return "Measurements, Filter";
    }

    @Override
    public boolean executeCL() {
        double sum = 0;

        ClearCLBuffer buffer = (ClearCLBuffer)( args[0]);
        sum = getCLIJ2().countNonZeroPixels(buffer);

        ResultsTable table = ResultsTable.getResultsTable();
        table.incrementCounter();
        table.addValue("CountNonZero", sum);
        table.show("Results");
        return true;
    }

    public static double countNonZeroPixels(CLIJ2 clij2, ClearCLBuffer clImage) {
        ClearCLBuffer clReducedImage = clImage;
        if (clImage.getDimension() == 3) {
            clReducedImage = clij2.create(new long[]{clImage.getWidth(), clImage.getHeight()}, NativeTypeEnum.Float);

            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("src", clImage);
            parameters.put("dst", clReducedImage);
            parameters.put("tolerance", new Float(0.0));
            clij2.execute(CountNonZeroPixels.class, "count_non_zero_projection_3d_2d_x.cl", "count_non_zero_projection_3d_2d", clReducedImage.getDimensions(), clReducedImage.getDimensions(), parameters);
        } else {
            clReducedImage = clij2.create(new long[]{clImage.getWidth(), clImage.getHeight()}, NativeTypeEnum.Float);
            clij2.greaterConstant(clImage, clReducedImage, 0);
        }

        RandomAccessibleInterval rai = clij2.convert(clReducedImage, RandomAccessibleInterval.class);
        Cursor cursor = Views.iterable(rai).cursor();
        float sum = 0;
        while (cursor.hasNext()) {
            sum += ((RealType) cursor.next()).getRealFloat();
        }

        if (clImage != clReducedImage) {
            clReducedImage.close();
        }
        return sum;
    }


    @Override
    public String getParameterHelpText() {
        return "Image source";
    }

    @Override
    public String getDescription() {
        return "Determines the number of all pixels in a given image which are not equal to 0. \n\nIt will be stored in a new row of ImageJs\n" +
                "Results table in the column 'CountNonZero'.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
