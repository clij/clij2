package net.haesleinhuepf.clijx.plugins;

import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

import java.nio.FloatBuffer;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_maximumOfAllPixels")
public class MaximumOfAllPixels extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        double maximumGreyValue = maximumOfAllPixels(getCLIJx(), (ClearCLBuffer)( args[0]));

        ResultsTable table = ResultsTable.getResultsTable();
        table.incrementCounter();
        table.addValue("Max", maximumGreyValue);
        table.show("Results");
        return true;
    }


    public static double maximumOfAllPixels(CLIJx clijx, ClearCLImageInterface clImage) {
        ClearCLImageInterface clReducedImage = clImage;
        ClearCLImageInterface clReducedImage2 = clImage;

        if (clImage.getDimension() == 3) {
            clReducedImage = clijx.create(new long[]{clImage.getWidth(), clImage.getHeight()}, clImage.getNativeType());

            clijx.maximumZProjection(clImage, clReducedImage);
            //HashMap<String, Object> parameters = new HashMap<>();
            //parameters.put("src", clImage);
            //parameters.put("dst", clReducedImage);
            //clijx.execute(MaximumOfAllPixels.class, "maximum_z_projection_x.cl", "maximum_z_projection", clReducedImage.getDimensions(), clReducedImage.getDimensions(), parameters);
        }
        if (clReducedImage.getDimension() == 2) {
            clReducedImage2 = clijx.create(new long[]{clReducedImage.getWidth(), 1}, clImage.getNativeType());
            clijx.maximumYProjection(clReducedImage, clReducedImage2);
            //HashMap<String, Object> parameters = new HashMap<>();
            //parameters.put("src", clImage);
            //parameters.put("dst", clReducedImage);
            //clijx.execute(MaximumOfAllPixels.class, "maximum_z_projection_x.cl", "maximum_z_projection", clReducedImage.getDimensions(), clReducedImage.getDimensions(), parameters);
        }

        ClearCLBuffer clReducedImage3 = clijx.create(new long[]{1, 1}, clijx.Float);
        clijx.maximumXProjection(clReducedImage2, clReducedImage3);

        float[] arr = new float[1];
        FloatBuffer buffer = FloatBuffer.wrap(arr);

        clReducedImage3.writeTo(buffer, true);

        clijx.release(clReducedImage3);

        double maximumGreyValue = arr[0];


/*
        RandomAccessibleInterval rai = clijx.convert(clReducedImage, RandomAccessibleInterval.class);
        Cursor cursor = Views.iterable(rai).cursor();
        float maximumGreyValue = -Float.MAX_VALUE;
        while (cursor.hasNext()) {
            float greyValue = ((RealType) cursor.next()).getRealFloat();
            if (maximumGreyValue < greyValue) {
                maximumGreyValue = greyValue;
            }
        }*/

        if (clImage != clReducedImage) {
            clijx.release(clReducedImage);
        }
        if (clImage != clReducedImage2) {
            clijx.release(clReducedImage2);
        }
        return maximumGreyValue;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source";
    }

    @Override
    public String getDescription() {
        return "Determines the maximum of all pixels in a given image. It will be stored in a new row of ImageJs\n" +
                "Results table in the column 'Max'.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }
}
