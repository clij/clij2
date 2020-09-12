package net.haesleinhuepf.clij2.plugins;

import ij.ImagePlus;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.HasAuthor;
import net.haesleinhuepf.clij2.utilities.HasLicense;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

import java.util.Arrays;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;

/**
 * AutomaticThreshold
 * <p>
 * Author: @haesleinhuepf
 * January 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_getAutomaticThreshold")
public class GetAutomaticThreshold extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, HasAuthor, HasLicense, IsCategorized {

    @Override
    public boolean executeCL() {
        ClearCLBuffer src = (ClearCLBuffer) (args[0]);
        String userSelectedMethod = (String)args[1];

        double threshold = getCLIJ2().getAutomaticThreshold(src, userSelectedMethod);
        ((Double[])args[2])[0] = threshold;
        return true;
    }

    public static double getAutomaticThreshold(CLIJ2 clij2, ClearCLBuffer src, String userSelectedMethod) {

        Float minimumGreyValue = 0f;
        Float maximumGreyValue = 0f;
        Integer numberOfBins = 256;

        if (src.getNativeType() == NativeTypeEnum.UnsignedByte) {
            minimumGreyValue = 0f;
            maximumGreyValue = 255f;
        } else {
            minimumGreyValue = null;
            maximumGreyValue = null;
        }

        return getAutomaticThreshold(clij2, src, userSelectedMethod, minimumGreyValue, maximumGreyValue, 256);
    }

    public static double getAutomaticThreshold(CLIJ2 clij2, ClearCLBuffer src, String userSelectedMethod, Float minimumGreyValue, Float maximumGreyValue, Integer numberOfBins) {

        if (minimumGreyValue == null) {
            minimumGreyValue = new Double(clij2.minimumOfAllPixels(src)).floatValue();
        }

        if (maximumGreyValue == null) {
            maximumGreyValue = new Double(clij2.maximumOfAllPixels(src)).floatValue();
        }


        ClearCLBuffer histogram = clij2.create(new long[]{numberOfBins, 1, 1}, clij2.Float);
        Histogram.fillHistogram(clij2, src, histogram, minimumGreyValue, maximumGreyValue);
        //releaseBuffers(args);

        //System.out.println("CL sum " + clij.op().sumPixels(histogram));

        // the histogram is written in args[1] which is supposed to be a one-dimensional image
        ImagePlus histogramImp = clij2.convert(histogram, ImagePlus.class);
        clij2.release(histogram);

        // convert histogram
        float[] determinedHistogram = (float[]) (histogramImp.getProcessor().getPixels());
        int[] convertedHistogram = new int[determinedHistogram.length];

        long sum = 0;
        for (int i = 0; i < determinedHistogram.length; i++) {
            convertedHistogram[i] = (int) determinedHistogram[i];
            sum += convertedHistogram[i];
        }
        //System.out.println("Sum: " + sum);


        String method = "Default";

        for (String choice : AutoThresholderImageJ1.getMethods()) {
            if (choice.toLowerCase().compareTo(userSelectedMethod.toLowerCase()) == 0) {
                method = choice;
            }
        }
        //System.out.println("Method: " + method);

        float threshold = new AutoThresholderImageJ1().getThreshold(method, convertedHistogram);

        // math source https://github.com/imagej/ImageJA/blob/master/src/main/java/ij/process/ImageProcessor.java#L692
        threshold = minimumGreyValue + ((threshold + 1.0f) / 255.0f) * (maximumGreyValue - minimumGreyValue);

        //System.out.println("Threshold: " + threshold)

        return threshold;
    }

    @Override
    public String getDescription() {
        StringBuilder doc = new StringBuilder();
        doc.append("The automatic thresholder utilizes the threshold methods from ImageJ on a histogram determined on \n" +
                "the GPU to determine a threshold value as similar as possible to ImageJ 'Apply Threshold' method. \n\nEnter one \n" +
                "of these methods in the method text field:\n" +
                Arrays.toString(AutoThresholderImageJ1.getMethods()) );
        return doc.toString();
    }

    @Override
    public String getParameterHelpText() {
        return "Image input, String method, ByRef Number threshold_value";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public String getAuthorName() {
        return "Robert Haase based on work by G. Landini and W. Rasband";
    }

    @Override
    public String getLicense() {
        return "The code for the automatic thresholding methods originates from " +
                "https://github.com/imagej/imagej1/blob/master/ij/process/AutoThresholder.java" +
                "\n\n" +
                "Detailed documentation on the implemented methods can be found online: " +
                "https://imagej.net/Auto_Threshold";
    }

    @Override
    public String getCategories() {
        return "Binary, Measurements";
    }
}
