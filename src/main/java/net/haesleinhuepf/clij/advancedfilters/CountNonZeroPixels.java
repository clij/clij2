package net.haesleinhuepf.clij.advancedfilters;

import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
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
 * June 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_countNonZeroPixels")
public class CountNonZeroPixels extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        double sum = 0;

        Object[] args = openCLBufferArgs();

        ClearCLBuffer buffer = (ClearCLBuffer)( args[0]);

        sum = countNonZeroPixels(clij, buffer);

        releaseBuffers(args);


        ResultsTable table = ResultsTable.getResultsTable();
        table.incrementCounter();
        table.addValue("CountNonZero", sum);
        table.show("Results");
        return true;
    }

    public static double countNonZeroPixels(CLIJ clij, ClearCLBuffer clImage) {
        ClearCLBuffer clReducedImage = clImage;
        if (clImage.getDimension() == 3) {
            clReducedImage = clij.createCLBuffer(new long[]{clImage.getWidth(), clImage.getHeight()}, clImage.getNativeType());

            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("src", clImage);
            parameters.put("dst", clReducedImage);
            parameters.put("tolerance", new Float(0.001));
            clij.execute(Kernels.class, "countnonzeropixels.cl", "count_non_zero_project_3d_2d", parameters);
        }

        RandomAccessibleInterval rai = clij.convert(clReducedImage, RandomAccessibleInterval.class);
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
        return "Determines the number of all pixels in a given image which are not equal to 0. It will be stored in a new row of ImageJs\n" +
                "Results table in the column 'Count_non_zero'.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
