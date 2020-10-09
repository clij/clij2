package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.clearcl.enums.ImageChannelDataType;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij.utilities.AffineTransform;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.CLIJUtilities;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import net.imglib2.realtransform.AffineTransform2D;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_scale2D")
public class Scale2D extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Image";
    }

    @Override
    public String getOutputType() {
        return "Image";
    }

    @Override
    public String getCategories() {
        return "Transform";
    }

    @Override
    public Object[] getDefaultValues() {
        return new Object[]{null, null, 1, 1, true};
    }

    @Override
    public boolean executeCL() {
        float scaleFactorX = asFloat(args[2]);
        float scaleFactorY = asFloat(args[3]);
        boolean scaleAroundCenter = asBoolean(args[4]);


        ClearCLBuffer input = ((ClearCLBuffer) args[0]);
        ClearCLBuffer output = ((ClearCLBuffer) args[1]);

        return getCLIJ2().scale2D(input, output, scaleFactorX, scaleFactorY, scaleAroundCenter);
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

        at.scale(1.0 / factorX, 1.0 / factorY);

        if (scaleAroundCenter) {
            at.translate(input.getWidth() / 2, input.getHeight() / 2, input.getDepth() / 2);
        }

        if (!clij2.hasImageSupport()) {
            return clij2.affineTransform2D(input, output, AffineTransform.matrixToFloatArray2D(at));
        } else {
            ClearCLImage image = clij2.create(input.getDimensions(), ImageChannelDataType.Float);
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
        return "Image source, ByRef Image destination, Number scaling_factor_x, Number scaling_factor_y, Boolean scale_to_center";
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
