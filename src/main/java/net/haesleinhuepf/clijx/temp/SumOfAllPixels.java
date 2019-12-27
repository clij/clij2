package net.haesleinhuepf.clijx.temp;

import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.clearcl.enums.ImageChannelDataType;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
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
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_sumOfAllPixels")
public class SumOfAllPixels extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        double sum = sumPixels(getCLIJx(), (ClearCLBuffer)( args[0]));

        ResultsTable table = ResultsTable.getResultsTable();
        table.incrementCounter();
        table.addValue("Sum", sum);
        table.show("Results");
        return true;
    }

    public static double sumPixels(CLIJx clijx, ClearCLImageInterface clImage) {
        return sumOfAllPixels(clijx, clImage);
    }

    public static double sumOfAllPixels(CLIJx clijx, ClearCLImageInterface clImage) {
        ClearCLImageInterface clReducedImage = clImage;
        if (clImage.getDimension() == 3) {
            clReducedImage = clijx.create(new long[]{clImage.getWidth(), clImage.getHeight()}, ImageChannelDataType.Float);

            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("src", clImage);
            parameters.put("dst", clReducedImage);
            clijx.execute(SumOfAllPixels.class, "sumProject.cl", "sum_project_3d_2d", clReducedImage.getDimensions(), clReducedImage.getDimensions(), parameters);
        }

        RandomAccessibleInterval rai = clijx.convert(clReducedImage, RandomAccessibleInterval.class);
        Cursor cursor = Views.iterable(rai).cursor();
        float sum = 0;
        while (cursor.hasNext()) {
            sum += ((RealType) cursor.next()).getRealFloat();
        }

        if (clImage != clReducedImage) {
            clijx.release(clReducedImage);
        }
        return sum;
    }


    @Override
    public String getParameterHelpText() {
        return "Image source";
    }

    @Override
    public String getDescription() {
        return "Determines the sum of all pixels in a given image. It will be stored in a new row of ImageJs\n" +
                "Results table in the column 'Sum'.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
