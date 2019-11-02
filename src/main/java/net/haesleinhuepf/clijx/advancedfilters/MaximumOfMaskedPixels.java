package net.haesleinhuepf.clijx.advancedfilters;

import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
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

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_maximumOfMaskedPixels")
public class MaximumOfMaskedPixels extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        double maxVal = maximumOfMaskedPixels(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]));


        ResultsTable table = ResultsTable.getResultsTable();
        table.incrementCounter();
        table.addValue("Masked_max", maxVal);
        table.show("Results");
        return true;
    }

    public static double maximumOfMaskedPixels(CLIJ clij, ClearCLBuffer clImage, ClearCLBuffer mask) {
        ClearCLBuffer clReducedImage = clImage;
        ClearCLBuffer clReducedMask = mask;
        if (clImage.getDimension() == 3) {
            clReducedImage = clij.createCLBuffer(new long[]{clImage.getWidth(), clImage.getHeight()}, clImage.getNativeType());
            clReducedMask = clij.createCLBuffer(new long[]{clImage.getWidth(), clImage.getHeight()}, mask.getNativeType());

            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("src", clImage);
            parameters.put("mask", mask);
            parameters.put("dst_max", clReducedImage);
            parameters.put("dst_mask", clReducedMask);
            clij.execute(MaximumOfMaskedPixels.class, "masked_projections.cl", "max_project_3d_2d", parameters);
        }

        RandomAccessibleInterval rai = clij.convert(clReducedImage, RandomAccessibleInterval.class);
        Cursor cursor = Views.iterable(rai).cursor();
        RandomAccessibleInterval raiMask = clij.convert(clReducedImage, RandomAccessibleInterval.class);
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
            clReducedImage.close();
            clReducedMask.close();
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
