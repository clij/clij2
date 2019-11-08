package net.haesleinhuepf.clijx.advancedfilters;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.utilities.CLIJUtilities;
import net.imglib2.realtransform.AffineTransform3D;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_resample")
public class Resample extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        float factorX = asFloat(args[2]);
        float factorY = asFloat(args[3]);
        float factorZ = asFloat(args[4]);
        boolean linearInterpolation = asBoolean(args[5]);


        boolean result = resample(clij, (ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]), factorX, factorY, factorZ, linearInterpolation);
        return result;
    }

    public static boolean resample(CLIJ clij, ClearCLBuffer input, ClearCLBuffer output, float factorX, float factorY, float factorZ, boolean linearInterpolation) {
        AffineTransform3D at = new AffineTransform3D();
        at.set(factorX, 0, 0, 0,
                0, factorY, 0, 0,
                0, 0, factorZ, 0);

        if (linearInterpolation) {
            ClearCLImage inputImage = clij.create(input.getDimensions(), CLIJUtilities.nativeToChannelType(input.getNativeType()));
            clij.op().copy(input, inputImage);
            ClearCLImage outputImage = clij.create(output.getDimensions(), CLIJUtilities.nativeToChannelType(output.getNativeType()));


            clij.op().affineTransform3D(inputImage, outputImage, at);
            inputImage.close();

            clij.op().copy(outputImage, output);
            outputImage.close();
        } else {
            clij.op().affineTransform3D(input, output, at);
        }



        return true;
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        float factorX = asFloat(args[2]);
        float factorY = asFloat(args[3]);
        float factorZ = asFloat(args[4]);

        return clij.create(new long[]{
                (long) (input.getWidth() * factorX),
                (long) (input.getHeight() * factorY),
                (long) (input.getDepth() * factorZ)
        }, input.getNativeType());
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number factorX, Number factorY, Number factorZ, Boolean linearInterpolation";
    }


    @Override
    public String getDescription() {
        return "Resamples an image with given size factors using an affine transform.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
