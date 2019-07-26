package net.haesleinhuepf.clij.registration;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.imglib2.realtransform.AffineTransform3D;
import org.scijava.plugin.Plugin;

/**
 *
 * Author: haesleinhuepf
 * June 2019
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_translationRegistration")
public class TranslationRegistration extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image input1, Image input2, Image destination";
    }

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();
        boolean result = correctTranslation(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), (ClearCLBuffer)(args[2]));
        releaseBuffers(args);
        return result;
    }

    public static boolean correctTranslation(CLIJ clij, ClearCLBuffer input1, ClearCLBuffer input2, ClearCLBuffer output) {

        double[] center =centerOfMass(clij, input1);
        return correctTranslation(clij, input2, output, center);
    }

    public static double[] centerOfMass(CLIJ clij, ClearCLBuffer input1) {
        ClearCLBuffer thresholded = clij.create(input1.getDimensions(), NativeTypeEnum.Byte);
        clij.op().automaticThreshold(input1, thresholded, "default");
        //clij.show(thresholded, "thresholded");

        double[] center = clij.op().centerOfMass(thresholded);

        thresholded.close();

        return center;
    }

    public static boolean correctTranslation(CLIJ clij, ClearCLBuffer input2, ClearCLBuffer output, double[] targetCenter) {

        ClearCLBuffer thresholded = clij.create(input2.getDimensions(), NativeTypeEnum.Byte);
        clij.op().automaticThreshold(input2, thresholded, "default");
        //clij.show(thresholded, "thresholded");

        double[] center = clij.op().centerOfMass(thresholded);

        thresholded.close();

        double[] delta = new double[3];
        for (int d = 0; d < delta.length && d < center.length; d++) {
            delta[d] = targetCenter[d] - center[d];
        }

        ClearCLBuffer bufferShiftCorrected = output;
        AffineTransform3D at = new AffineTransform3D();
        at.translate(delta);
        clij.op().affineTransform3D(input2, bufferShiftCorrected, at);

        return true;
    }

    @Override
    public String getDescription() {
        return "Measures center of mass of thresholded objects in the two input images and translates" +
                " the second image so that it better fits to the first image.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
