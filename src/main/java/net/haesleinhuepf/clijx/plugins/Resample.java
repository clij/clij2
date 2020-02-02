package net.haesleinhuepf.clijx.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import net.haesleinhuepf.clij2.utilities.CLIJUtilities;
import net.imglib2.realtransform.AffineTransform3D;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_resample")
public class Resample extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        float factorX = asFloat(args[2]);
        float factorY = asFloat(args[3]);
        float factorZ = asFloat(args[4]);
        boolean linearInterpolation = asBoolean(args[5]);


        boolean result = resample(getCLIJx(), (ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]), factorX, factorY, factorZ, linearInterpolation);
        return result;
    }

    public static boolean resample(CLIJx clijx, ClearCLImageInterface input, ClearCLImageInterface output, Float factorX, Float factorY, Float factorZ, boolean linearInterpolation) {
        AffineTransform3D at = new AffineTransform3D();
        at.set(factorX, 0, 0, 0,
                0, factorY, 0, 0,
                0, 0, factorZ, 0);

        if (linearInterpolation) {
            ClearCLImage inputImage = clijx.create(input.getDimensions(), CLIJUtilities.nativeToChannelType(input.getNativeType()));
            clijx.copy(input, inputImage);
            ClearCLImage outputImage = clijx.create(output.getDimensions(), CLIJUtilities.nativeToChannelType(output.getNativeType()));

            clijx.affineTransform3D(inputImage, outputImage, at);
            inputImage.close();

            clijx.copy(outputImage, output);
            outputImage.close();
        } else {
            if (input instanceof ClearCLBuffer && output instanceof ClearCLBuffer) {
                clijx.affineTransform3D((ClearCLBuffer) input, (ClearCLBuffer) output, at);
            } else if(input instanceof ClearCLImage && output instanceof ClearCLBuffer) {
                clijx.affineTransform3D((ClearCLImage) input, (ClearCLBuffer) output, at);
            } else if(input instanceof ClearCLImage && output instanceof ClearCLImage) {
                clijx.affineTransform3D((ClearCLImage) input, (ClearCLImage) output, at);
            } else if(input instanceof ClearCLBuffer && output instanceof ClearCLImage) {
                // TODO: fix that workaround for an inconvenient API of affineTransform3D
                ClearCLBuffer temp = clijx.create(output.getDimensions(), output.getNativeType());
                clijx.affineTransform3D((ClearCLBuffer) input, temp, at);
                clijx.copy(temp, output);
                clijx.release(temp);
            }
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
