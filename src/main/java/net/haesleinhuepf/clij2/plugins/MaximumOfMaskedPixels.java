package net.haesleinhuepf.clij2.plugins;

import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
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
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_maximumOfMaskedPixels")
public class MaximumOfMaskedPixels extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized {
    @Override
    public String getCategories() {
        return "Binary, Measurements";
    }

    @Override
    public boolean executeCL() {
        double maxVal = getCLIJ2().maximumOfMaskedPixels((ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]));


        ResultsTable table = ResultsTable.getResultsTable();
        table.incrementCounter();
        table.addValue("Masked_max", maxVal);
        table.show("Results");
        return true;
    }

    public static double maximumOfMaskedPixels(CLIJ2 clij2, ClearCLBuffer clImage, ClearCLBuffer mask) {
        ClearCLBuffer clReducedImage = clImage;
        ClearCLBuffer clReducedMask = mask;
        if (clImage.getDimension() == 3) {
            clReducedImage = clij2.create(new long[]{clImage.getWidth(), clImage.getHeight()}, clImage.getNativeType());
            clReducedMask = clij2.create(new long[]{clImage.getWidth(), clImage.getHeight()}, mask.getNativeType());

            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("src", clImage);
            parameters.put("mask", mask);
            parameters.put("dst_max", clReducedImage);
            parameters.put("dst_mask", clReducedMask);
            clij2.execute(MaximumOfMaskedPixels.class, "maximum_of_masked_pixels_3d_2d_x.cl", "maximum_of_masked_pixels_3d_2d", clReducedImage.getDimensions(), clReducedImage.getDimensions(), parameters);
        }

        RandomAccessibleInterval rai = clij2.convert(clReducedImage, RandomAccessibleInterval.class);
        Cursor cursor = Views.iterable(rai).cursor();
        RandomAccessibleInterval raiMask = clij2.convert(clReducedImage, RandomAccessibleInterval.class);
        Cursor maskCursor = Views.iterable(raiMask).cursor();
        float maximumGreyValue = -Float.MAX_VALUE;
        while (cursor.hasNext()) {
            float greyValue = ((RealType) cursor.next()).getRealFloat();
            float binaryValue = ((RealType) maskCursor.next()).getRealFloat();
            if (binaryValue != 0 && maximumGreyValue < greyValue) {
                maximumGreyValue = greyValue;
            }
        }

        if (clImage != clReducedImage) {
            clij2.release(clReducedImage);
            clij2.release(clReducedMask);
        }
        return maximumGreyValue;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image mask";
    }

    @Override
    public String getDescription() {
        return "Determines the maximum intensity in an image, but only in pixels which have non-zero values in another" +
                " mask image.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
