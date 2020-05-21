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

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_rotate3D")
public class Rotate3D extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    protected Object[] getDefaultValues() {
        return new Object[]{null, null, 0, 0, 0, true};
    }

    @Override
    public boolean executeCL() {
        float angleX = (float)(-asFloat(args[2]) / 180.0f * Math.PI);
        float angleY = (float)(-asFloat(args[3]) / 180.0f * Math.PI);
        float angleZ = (float)(-asFloat(args[4]) / 180.0f * Math.PI);
        boolean rotateAroundCenter = asBoolean(args[5]);



        ClearCLBuffer input = ((ClearCLBuffer) args[0]);
        ClearCLBuffer output = ((ClearCLBuffer) args[1]);

        return getCLIJ2().rotate3D(input, output, angleX, angleY, angleZ, rotateAroundCenter);
    }


    public static boolean rotate3D(CLIJ2 clij2, ClearCLBuffer input, ClearCLBuffer output, Float angleX, Float angleY, Float angleZ, Boolean rotateAroundCenter) {
        AffineTransform3D at = new AffineTransform3D();

        if (rotateAroundCenter) {
            at.translate(-input.getWidth() / 2, -input.getHeight() / 2, -input.getDepth() / 2);
        }
        at.rotate(0, angleX);
        at.rotate(1, angleY);
        at.rotate(2, angleZ);
        if (rotateAroundCenter) {
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
        /*
        Rotate3D rotate3D = new Rotate3D();
        rotate3D.setClij(clij2.getClij());
        rotate3D.setArgs(new Object[]{input, output, angleX, angleY, angleZ, rotateAroundCenter});
        return rotate3D.executeCL();

         */
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Image destination, Number angleX, Number angleY, Number angleZ, Boolean rotateAroundCenter";
    }

    @Override
    public String getDescription() {
        return "Rotates an image stack in 3D. \n\nAll angles are entered in degrees. If the image is not rotated around \n" +
                "the center, it is rotated around the coordinate origin.\n\n" +
                "It is recommended to apply the rotation to an isotropic image stack.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }
}
