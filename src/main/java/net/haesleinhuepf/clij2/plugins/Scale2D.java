package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij.utilities.AffineTransform;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.utilities.CLIJUtilities;
import net.imglib2.realtransform.AffineTransform2D;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_scale2D")
public class Scale2D extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        float scaleFactor = asFloat(args[2]);
        boolean scaleAroundCenter = asBoolean(args[3]);



        ClearCLBuffer input = ((ClearCLBuffer) args[0]);
        ClearCLBuffer output = ((ClearCLBuffer) args[1]);
        CLIJ2 clij2 = getCLIJ2();

        return scale2D(clij2, input, output, scaleFactor, scaleFactor, scaleAroundCenter);
    }

    public static boolean scale(CLIJ2 clij2, ClearCLBuffer input, ClearCLBuffer output, Float factor) {
        return scale2D(clij2, input, output, factor, factor, false);
    }

    public static boolean scale(CLIJ2 clij2, ClearCLBuffer input, ClearCLBuffer output, Float factorX, Float factorY) {
        return scale2D(clij2, input, output, factorX, factorY);
    }

    public static boolean scale2D(CLIJ2 clij2, ClearCLBuffer input, ClearCLBuffer output, Float factorX, Float factorY) {
        return scale2D(clij2, input, output, factorX, factorY, false);
    }



    public static boolean scale2D(CLIJ2 clij2, ClearCLBuffer input, ClearCLBuffer output, Float factorX, Float factorY, boolean scaleAroundCenter) {

        AffineTransform2D at = new AffineTransform2D();

        if (scaleAroundCenter) {

            at.translate(-input.getWidth() / 2, -input.getHeight() / 2, -input.getDepth() / 2);
        }

        at.scale(factorX, factorY);

        if (scaleAroundCenter) {
            at.translate(input.getWidth() / 2, input.getHeight() / 2, input.getDepth() / 2);
        }

        if (!clij2.hasImageSupport()) {
            return clij2.affineTransform2D(input, output, AffineTransform.matrixToFloatArray2D(at));
        } else {
            ClearCLImage image = clij2.create(input.getDimensions(), CLIJUtilities.nativeToChannelType(input.getNativeType()));
            clij2.copy(input, image);
            clij2.affineTransform2D(image, output, AffineTransform.matrixToFloatArray2D(at));
            clij2.release(image);
            return true;
        }
        /*
        Scale2D scale2D = new Scale2D();
        scale2D.setClij(clij2.getClij());
        scale2D.setArgs(new Object[]{input, output, factorX, factorY});
        return scale2D.executeCL();
        */
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number scaling_factor, Boolean scale_to_center";
    }

    @Override
    public String getDescription() {
        return "Scales an image with a given factor.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }
}
