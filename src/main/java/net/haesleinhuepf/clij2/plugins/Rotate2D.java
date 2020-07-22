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
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import net.imglib2.realtransform.AffineTransform2D;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_rotate2D")
public class Rotate2D extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized {
    @Override
    public String getCategories() {
        return "Transform";
    }

    @Override
    public Object[] getDefaultValues() {
        return new Object[]{null, null, 0, true};
    }

    @Override
    public boolean executeCL() {
        float angle = (float)(-asFloat(args[2]) / 180.0f * Math.PI);
        boolean rotateAroundCenter = asBoolean(args[3]);


        ClearCLBuffer input = ((ClearCLBuffer) args[0]);
        ClearCLBuffer output = ((ClearCLBuffer) args[1]);

        return getCLIJ2().rotate2D(input, output, angle, rotateAroundCenter);
    }

    public static boolean rotate2D(CLIJ2 clij2, ClearCLBuffer input, ClearCLBuffer output, Float angle, Boolean rotateAroundCenter) {


        AffineTransform2D at = new AffineTransform2D();

        if (rotateAroundCenter) {
            at.translate(-input.getWidth() / 2, -input.getHeight() / 2);
        }
        at.rotate(angle);
        if (rotateAroundCenter) {
            at.translate(input.getWidth() / 2, input.getHeight() / 2);
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
        Rotate2D rotate2D = new Rotate2D();
        rotate2D.setClij(clij2.getClij());
        rotate2D.setArgs(new Object[]{input, output, angle, rotateAroundCenter});
        return rotate2D.executeCL();

         */
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Image destination, Number angle, Boolean rotateAroundCenter";
    }

    @Override
    public String getDescription() {
        return "Rotates an image in plane. \n\nAll angles are entered in degrees. If the image is not rotated around \n" +
                "the center, it is rotated around the coordinate origin.\n\n" +
                "It is recommended to apply the rotation to an isotropic image.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }
}
