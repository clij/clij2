package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.CLIJUtilities;
import net.imglib2.realtransform.AffineTransform2D;
import net.imglib2.realtransform.AffineTransform3D;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_resample")
public class Resample extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        float factorX = asFloat(args[2]);
        float factorY = asFloat(args[3]);
        float factorZ = asFloat(args[4]);
        boolean linearInterpolation = asBoolean(args[5]);


        boolean result = resample(getCLIJ2(), (ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]), factorX, factorY, factorZ, linearInterpolation);
        return result;
    }

    @Deprecated
    public static boolean resample(CLIJ2 clij2, ClearCLImageInterface input, ClearCLImageInterface output, Float factorX, Float factorY, Float factorZ, Boolean linearInterpolation) {
        if (input.getDimension() == 3) {
            return resample3D(clij2, input, output, factorX, factorY, factorZ, linearInterpolation);
        } else {
            return resample2D(clij2, input, output, factorX, factorY, linearInterpolation);
        }
    }

    public static boolean resample3D(CLIJ2 clij2, ClearCLImageInterface input, ClearCLImageInterface output, Float factorX, Float factorY, Float factorZ, Boolean linearInterpolation) {
        AffineTransform3D at = new AffineTransform3D();
        at.set(factorX, 0, 0, 0,
                0, factorY, 0, 0,
                0, 0, factorZ, 0);

        if (linearInterpolation) {
            ClearCLImage inputImage = clij2.create(input.getDimensions(), CLIJUtilities.nativeToChannelType(input.getNativeType()));
            clij2.copy(input, inputImage);
            ClearCLImage outputImage = clij2.create(output.getDimensions(), CLIJUtilities.nativeToChannelType(output.getNativeType()));

            clij2.affineTransform3D(inputImage, outputImage, at);
            inputImage.close();

            clij2.copy(outputImage, output);
            outputImage.close();
        } else {
            if (input instanceof ClearCLBuffer && output instanceof ClearCLBuffer) {
                clij2.affineTransform3D((ClearCLBuffer) input, (ClearCLBuffer) output, at);
            } else if(input instanceof ClearCLImage && output instanceof ClearCLBuffer) {
                clij2.affineTransform3D((ClearCLImage) input, (ClearCLBuffer) output, at);
            } else if(input instanceof ClearCLImage && output instanceof ClearCLImage) {
                clij2.affineTransform3D((ClearCLImage) input, (ClearCLImage) output, at);
            } else if(input instanceof ClearCLBuffer && output instanceof ClearCLImage) {
                // TODO: fix that workaround for an inconvenient API of affineTransform3D
                ClearCLBuffer temp = clij2.create(output.getDimensions(), output.getNativeType());
                clij2.affineTransform3D((ClearCLBuffer) input, temp, at);
                clij2.copy(temp, output);
                clij2.release(temp);
            }
        }
        return true;
    }

    public static boolean resample2D(CLIJ2 clij2, ClearCLImageInterface input, ClearCLImageInterface output, Float factorX, Float factorY, Boolean linearInterpolation) {
        AffineTransform2D at = new AffineTransform2D();
        at.set(factorX, 0, 0,
                0, factorY, 0);

        if (linearInterpolation) {
            ClearCLImage inputImage = clij2.create(input.getDimensions(), CLIJUtilities.nativeToChannelType(input.getNativeType()));
            clij2.copy(input, inputImage);
            ClearCLImage outputImage = clij2.create(output.getDimensions(), CLIJUtilities.nativeToChannelType(output.getNativeType()));

            clij2.affineTransform2D(inputImage, outputImage, at);
            inputImage.close();

            clij2.copy(outputImage, output);
            outputImage.close();
        } else {
            if (input instanceof ClearCLBuffer && output instanceof ClearCLBuffer) {
                clij2.affineTransform2D((ClearCLBuffer) input, (ClearCLBuffer) output, at);
            } else if(input instanceof ClearCLImage && output instanceof ClearCLBuffer) {
                clij2.affineTransform2D((ClearCLImage) input, (ClearCLBuffer) output, at);
            } else if(input instanceof ClearCLImage && output instanceof ClearCLImage) {
                clij2.affineTransform2D((ClearCLImage) input, (ClearCLImage) output, at);
            } else if(input instanceof ClearCLBuffer && output instanceof ClearCLImage) {
                // TODO: fix that workaround for an inconvenient API of affineTransform3D
                ClearCLBuffer temp = clij2.create(output.getDimensions(), output.getNativeType());
                clij2.affineTransform2D((ClearCLBuffer) input, temp, at);
                clij2.copy(temp, output);
                clij2.release(temp);
            }
        }
        return true;
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        float factorX = asFloat(args[2]);
        float factorY = asFloat(args[3]);
        float factorZ = asFloat(args[4]);

        if (input.getDimension() == 2 ) {
            return clij.create(new long[]{
                    (long) (input.getWidth() * factorX),
                    (long) (input.getHeight() * factorY)
            }, input.getNativeType());
        } else {
            return clij.create(new long[]{
                    (long) (input.getWidth() * factorX),
                    (long) (input.getHeight() * factorY),
                    (long) (input.getDepth() * factorZ)
            }, input.getNativeType());
        }
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
