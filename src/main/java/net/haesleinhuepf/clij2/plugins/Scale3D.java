package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij.utilities.AffineTransform;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.CLIJUtilities;
import net.imglib2.realtransform.AffineTransform3D;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_scale3D")
public class Scale3D extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        float scaleFactorX = asFloat(args[2]);
        float scaleFactorY = asFloat(args[3]);
        float scaleFactorZ = asFloat(args[4]);
        boolean scaleAroundCenter = asBoolean(args[5]);

        ClearCLBuffer input = ((ClearCLBuffer) args[0]);
        ClearCLBuffer output = ((ClearCLBuffer) args[1]);
        CLIJ2 clij2 = getCLIJ2();

        return scale3D(clij2, input, output, scaleFactorX, scaleFactorY, scaleFactorZ, scaleAroundCenter);
    }

    public static boolean scale3D(CLIJ2 clij2, ClearCLBuffer input, ClearCLBuffer output, Float factorX, Float factorY, Float factorZ) {
        return scale3D(clij2, input, output, factorX, factorY, factorZ, false);
    }

    public static boolean scale3D(CLIJ2 clij2, ClearCLBuffer input, ClearCLBuffer output, Float factorX, Float factorY, Float factorZ, Boolean scaleAroundCenter) {
        AffineTransform3D at = new AffineTransform3D();


        if (scaleAroundCenter) {
            at.translate(-input.getWidth() / 2, -input.getHeight() / 2, -input.getDepth() / 2);
        }

        at.scale(1.0 / factorX, 1.0 / factorY, 1.0 / factorZ);

        if (scaleAroundCenter) {
            at.translate(input.getWidth() / 2, input.getHeight() / 2, input.getDepth() / 2);
        }

        if (!clij2.hasImageSupport()) {
            return clij2.affineTransform3D(input, output, AffineTransform.matrixToFloatArray(at));
        } else {
            ClearCLImage image = clij2.create(input.getDimensions(), CLIJUtilities.nativeToChannelType(input.getNativeType()));
            clij2.copy(input, image);
            clij2.affineTransform3D(image, output, AffineTransform.matrixToFloatArray(at));
            clij2.release(image);
            return true;
        }

        /*Scale3D scale3D = new Scale3D();
        scale3D.setClij(clij2.getClij());
        scale3D.setArgs(new Object[]{input, output, factorX, factorY, factorZ});
        return scale3D.executeCL();

         */
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number scaling_factor_x, Number scaling_factor_y, Number scaling_factor_z,  Boolean scale_to_center";
    }

    @Override
    public String getDescription() {
        return "Scales an image with a given factor.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }
}
