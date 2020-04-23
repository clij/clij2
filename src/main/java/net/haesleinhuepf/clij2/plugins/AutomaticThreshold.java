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
import org.scijava.plugin.Plugin;

import java.util.Arrays;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;

/**
 * AutomaticThreshold
 * <p>
 * Author: @haesleinhuepf
 * January 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_automaticThreshold")
public class AutomaticThreshold extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, HasAuthor, HasLicense {

    @Override
    public boolean executeCL() {
        ClearCLBuffer src = (ClearCLBuffer) (args[0]);
        ClearCLBuffer dst = (ClearCLBuffer) (args[1]);
        String userSelectedMethod = (String)args[2];

        return getCLIJ2().automaticThreshold(src, dst, userSelectedMethod);
    }

    public static boolean automaticThreshold(CLIJ2 clij2, ClearCLBuffer src, ClearCLBuffer dst, String userSelectedMethod) {
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

        return automaticThreshold(clij2, src, dst, userSelectedMethod, minimumGreyValue, maximumGreyValue, 256);
    }


    public static boolean automaticThreshold(CLIJ2 clij2, ClearCLBuffer src, ClearCLBuffer dst, String userSelectedMethod, Float minimumGreyValue, Float maximumGreyValue, Integer numberOfBins) {
        double threshold = GetAutomaticThreshold.getAutomaticThreshold(clij2, src, userSelectedMethod, minimumGreyValue, maximumGreyValue, numberOfBins);
        clij2.threshold(src, dst, threshold);
        return true;
    }

    @Override
    public String getDescription() {
        StringBuilder doc = new StringBuilder();
        doc.append("The automatic thresholder utilizes the threshold methods from ImageJ on a histogram determined on \n" +
                "the GPU to create binary images as similar as possible to ImageJ 'Apply Threshold' method.\n\n Enter one \n" +
                "of these methods in the method text field:\n" +
                Arrays.toString(AutoThresholderImageJ1.getMethods()) );
        return doc.toString();
    }

    @Override
    public String getParameterHelpText() {
        return "Image input, ByRef Image destination, String method";
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
