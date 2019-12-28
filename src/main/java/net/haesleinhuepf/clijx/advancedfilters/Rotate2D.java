package net.haesleinhuepf.clijx.advancedfilters;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij.utilities.AffineTransform;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import net.haesleinhuepf.clijx.utilities.CLIJUtilities;
import net.imglib2.realtransform.AffineTransform2D;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_rotate2D")
public class Rotate2D extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        float angle = (float)(-asFloat(args[2]) / 180.0f * Math.PI);
        boolean rotateAroundCenter = asBoolean(args[3]);

        AffineTransform2D at = new AffineTransform2D();
        Object[] args = openCLBufferArgs();

        if (rotateAroundCenter) {
            ClearCLBuffer input = (ClearCLBuffer) args[0];
            at.translate(-input.getWidth() / 2, -input.getHeight() / 2);
        }
        at.rotate(angle);
        if (rotateAroundCenter) {
            ClearCLBuffer input = (ClearCLBuffer) args[0];
            at.translate(input.getWidth() / 2, input.getHeight() / 2);
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

    public static boolean rotate2D(CLIJx clijx, ClearCLBuffer input, ClearCLBuffer output, Float angleX, Float angleY, Boolean rotateAroundCenter) {
        Rotate2D rotate2D = new Rotate2D();
        rotate2D.setClij(clijx.getClij());
        rotate2D.setArgs(new Object[]{input, output, angleX, angleY, rotateAroundCenter});
        return rotate2D.executeCL();
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number angle, Boolean rotateAroundCenter";
    }

    @Override
    public String getDescription() {
        return "Rotates an image in plane. All angles are entered in degrees. If the image is not rotated around \n" +
                "the center, it is rotated around the coordinate origin.\n\n" +
                "It is recommended to apply the rotation to an isotropic image.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }
}
