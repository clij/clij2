package net.haesleinhuepf.clij2.plugins;

import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

import java.nio.FloatBuffer;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_maximumOfAllPixels")
public class MaximumOfAllPixels extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        double maximumGreyValue = maximumOfAllPixels(getCLIJ2(), (ClearCLBuffer)( args[0]));

        ResultsTable table = ResultsTable.getResultsTable();
        table.incrementCounter();
        table.addValue("Max", maximumGreyValue);
        table.show("Results");
        return true;
    }


    public static double maximumOfAllPixels(CLIJ2 clij2, ClearCLImageInterface clImage) {
        ClearCLImageInterface clReducedImage = clImage;
        ClearCLImageInterface clReducedImage2 = clImage;

        if (clImage.getDimension() == 3) {
            clReducedImage = clij2.create(new long[]{clImage.getWidth(), clImage.getHeight()}, clImage.getNativeType());

            clij2.maximumZProjection(clImage, clReducedImage);
            //HashMap<String, Object> parameters = new HashMap<>();
            //parameters.put("src", clImage);
            //parameters.put("dst", clReducedImage);
            //clij2.execute(MaximumOfAllPixels.class, "maximum_z_projection_x.cl", "maximum_z_projection", clReducedImage.getDimensions(), clReducedImage.getDimensions(), parameters);
        }
        if (clReducedImage.getDimension() == 2) {
            clReducedImage2 = clij2.create(new long[]{clReducedImage.getWidth(), 1}, clImage.getNativeType());
            clij2.maximumYProjection(clReducedImage, clReducedImage2);
            //HashMap<String, Object> parameters = new HashMap<>();
            //parameters.put("src", clImage);
            //parameters.put("dst", clReducedImage);
            //clij2.execute(MaximumOfAllPixels.class, "maximum_z_projection_x.cl", "maximum_z_projection", clReducedImage.getDimensions(), clReducedImage.getDimensions(), parameters);
        }

        ClearCLBuffer clReducedImage3 = clij2.create(new long[]{1, 1}, clij2.Float);
        clij2.maximumXProjection(clReducedImage2, clReducedImage3);

        float[] arr = new float[1];
        FloatBuffer buffer = FloatBuffer.wrap(arr);

        clReducedImage3.writeTo(buffer, true);

        clij2.release(clReducedImage3);

        double maximumGreyValue = arr[0];


/*
        RandomAccessibleInterval rai = clij2.convert(clReducedImage, RandomAccessibleInterval.class);
        Cursor cursor = Views.iterable(rai).cursor();
        float maximumGreyValue = -Float.MAX_VALUE;
        while (cursor.hasNext()) {
            float greyValue = ((RealType) cursor.next()).getRealFloat();
            if (maximumGreyValue < greyValue) {
                maximumGreyValue = greyValue;
            }
        }*/

        if (clImage != clReducedImage) {
            clij2.release(clReducedImage);
        }
        if (clImage != clReducedImage2) {
            clij2.release(clReducedImage2);
        }
        return maximumGreyValue;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source";
    }

    @Override
    public String getDescription() {
        return "Determines the maximum of all pixels in a given image. \n\nIt will be stored in a new row of ImageJs\n" +
                "Results table in the column 'Max'.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
