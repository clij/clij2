package net.haesleinhuepf.clijx.plugins;

import ij.ImagePlus;
import ij.process.AutoThresholder;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import net.haesleinhuepf.clijx.utilities.HasAuthor;
import net.haesleinhuepf.clijx.utilities.HasLicense;
import org.scijava.plugin.Plugin;

import java.util.Arrays;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;

/**
 * AutomaticThreshold
 * <p>
 * Author: @haesleinhuepf
 * January 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_automaticThreshold")
public class AutomaticThreshold extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, HasAuthor, HasLicense {

    @Override
    public boolean executeCL() {
        ClearCLBuffer src = (ClearCLBuffer) (args[0]);
        ClearCLBuffer dst = (ClearCLBuffer) (args[1]);
        String userSelectedMethod = (String)args[2];

        return automaticThreshold(getCLIJx(), src, dst, userSelectedMethod);
    }

    public static boolean automaticThreshold(CLIJx clijx, ClearCLBuffer src, ClearCLBuffer dst, String userSelectedMethod) {
        assertDifferent(src, dst);

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

        return automaticThreshold(clijx, src, dst, userSelectedMethod, minimumGreyValue, maximumGreyValue, 256);
    }

    public static boolean automaticThreshold(CLIJx clijx, ClearCLBuffer src, ClearCLBuffer dst, String userSelectedMethod, Float minimumGreyValue, Float maximumGreyValue, Integer numberOfBins) {
        assertDifferent(src, dst);

        if (minimumGreyValue == null)
        {
            minimumGreyValue = new Double(clijx.minimumOfAllPixels(src)).floatValue();
        }

        if (maximumGreyValue == null)
        {
            maximumGreyValue = new Double(clijx.maximumOfAllPixels(src)).floatValue();
        }


        ClearCLBuffer histogram = clijx.create(new long[]{numberOfBins,1,1}, clijx.Float);
        Histogram.fillHistogram(clijx, src, histogram, minimumGreyValue, maximumGreyValue);
        //releaseBuffers(args);

        //System.out.println("CL sum " + clij.op().sumPixels(histogram));

        // the histogram is written in args[1] which is supposed to be a one-dimensional image
        ImagePlus histogramImp = clijx.convert(histogram, ImagePlus.class);
        clijx.release(histogram);

        // convert histogram
        float[] determinedHistogram = (float[])(histogramImp.getProcessor().getPixels());
        int[] convertedHistogram = new int[determinedHistogram.length];

        long sum = 0;
        for (int i = 0; i < determinedHistogram.length; i++) {
            convertedHistogram[i] = (int)determinedHistogram[i];
            sum += convertedHistogram[i];
        }
        //System.out.println("Sum: " + sum);


        String method = "Default";

        for (String choice : AutoThresholder.getMethods()) {
            if (choice.toLowerCase().compareTo(userSelectedMethod.toLowerCase()) == 0) {
                method = choice;
            }
        }
        //System.out.println("Method: " + method);

        float threshold = new AutoThresholder().getThreshold(method, convertedHistogram);

        // math source https://github.com/imagej/ImageJA/blob/master/src/main/java/ij/process/ImageProcessor.java#L692
        threshold = minimumGreyValue + ((threshold + 1.0f)/255.0f)*(maximumGreyValue-minimumGreyValue);

        //System.out.println("Threshold: " + threshold);

        Threshold.threshold(clijx, src, dst, threshold);

        return true;
    }

    @Override
    public String getDescription() {
        StringBuilder doc = new StringBuilder();
        doc.append("The automatic thresholder utilizes the threshold methods from ImageJ on a histogram determined on \n" +
                "the GPU to create binary images as similar as possible to ImageJ 'Apply Threshold' method. Enter one \n" +
                "of these methods in the method text field:\n" +
                Arrays.toString(AutoThresholderImageJ1.getMethods()) );
        return doc.toString();
    }


    @Override
    public String getParameterHelpText() {
        return "Image input, Image destination, String method";
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
}
