package net.haesleinhuepf.clijx.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij.utilities.AffineTransform;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import net.haesleinhuepf.clij2.utilities.CLIJUtilities;
import net.imglib2.realtransform.AffineTransform2D;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_scale2D")
public class Scale2D extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        float scaleFactor = asFloat(args[2]);
        boolean rotateAroundCenter = asBoolean(args[3]);

        AffineTransform2D at = new AffineTransform2D();
        Object[] args = openCLBufferArgs();

        if (rotateAroundCenter) {
            ClearCLBuffer input = (ClearCLBuffer) args[0];
            at.translate(-input.getWidth() / 2, -input.getHeight() / 2, -input.getDepth() / 2);
        }

        at.scale(scaleFactor);

        if (rotateAroundCenter) {
            ClearCLBuffer input = (ClearCLBuffer) args[0];
            at.translate(input.getWidth() / 2, input.getHeight() / 2, input.getDepth() / 2);
        }


        ClearCLBuffer input = ((ClearCLBuffer) args[0]);
        ClearCLBuffer output = ((ClearCLBuffer) args[1]);
        CLIJx clijx = getCLIJx();

        if (!clij.hasImageSupport()) {
            return clijx.affineTransform2D(input, output, AffineTransform.matrixToFloatArray2D(at));
        } else {
            ClearCLImage image = clijx.create(input.getDimensions(), CLIJUtilities.nativeToChannelType(input.getNativeType()));
            clijx.copy(input, image);
            clijx.affineTransform2D(image, output, AffineTransform.matrixToFloatArray2D(at));
            clijx.release(image);
            return true;
        }
    }


    public static boolean scale(CLIJx clijx, ClearCLBuffer input, ClearCLBuffer output, Float factorX, Float factorY) {
        return scale2D(clijx, input, output, factorX, factorY);
    }

    public static boolean scale2D(CLIJx clijx, ClearCLBuffer input, ClearCLBuffer output, Float factorX, Float factorY) {
        Scale2D scale2D = new Scale2D();
        scale2D.setClij(clijx.getClij());
        scale2D.setArgs(new Object[]{input, output, factorX, factorY});
        return scale2D.executeCL();
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
