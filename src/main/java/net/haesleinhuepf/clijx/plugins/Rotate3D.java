package net.haesleinhuepf.clijx.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij.utilities.AffineTransform;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import net.haesleinhuepf.clijx.utilities.CLIJUtilities;
import net.imglib2.realtransform.AffineTransform3D;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_rotate3D")
public class Rotate3D extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        float angleX = (float)(-asFloat(args[2]) / 180.0f * Math.PI);
        float angleY = (float)(-asFloat(args[3]) / 180.0f * Math.PI);
        float angleZ = (float)(-asFloat(args[4]) / 180.0f * Math.PI);
        boolean rotateAroundCenter = asBoolean(args[5]);

        AffineTransform3D at = new AffineTransform3D();
        Object[] args = openCLBufferArgs();

        if (rotateAroundCenter) {
            ClearCLBuffer input = (ClearCLBuffer) args[0];
            at.translate(-input.getWidth() / 2, -input.getHeight() / 2, -input.getDepth() / 2);
        }
        at.rotate(0, angleX);
        at.rotate(1, angleY);
        at.rotate(2, angleZ);
        if (rotateAroundCenter) {
            ClearCLBuffer input = (ClearCLBuffer) args[0];
            at.translate(input.getWidth() / 2, input.getHeight() / 2, input.getDepth() / 2);
        }

        ClearCLBuffer input = ((ClearCLBuffer) args[0]);
        ClearCLBuffer output = ((ClearCLBuffer) args[1]);
        CLIJx clijx = getCLIJx();

        if (!clij.hasImageSupport()) {
            return clijx.affineTransform3D(input, output, AffineTransform.matrixToFloatArray(at));
        } else {
            ClearCLImage image = clijx.create(input.getDimensions(), CLIJUtilities.nativeToChannelType(input.getNativeType()));
            clijx.copy(input, image);
            clijx.affineTransform3D(image, output, AffineTransform.matrixToFloatArray(at));
            clijx.release(image);
            return true;
        }

    }


    public static boolean rotate3D(CLIJx clijx, ClearCLBuffer input, ClearCLBuffer output, Float angleX, Float angleY, Float angleZ, Boolean rotateAroundCenter) {
        Rotate3D rotate3D = new Rotate3D();
        rotate3D.setClij(clijx.getClij());
        rotate3D.setArgs(new Object[]{input, output, angleX, angleY, angleZ, rotateAroundCenter});
        return rotate3D.executeCL();
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number angleX, Number angleY, Number angleZ, Boolean rotateAroundCenter";
    }

    @Override
    public String getDescription() {
        return "Rotates an image stack in 3D. All angles are entered in degrees. If the image is not rotated around \n" +
                "the center, it is rotated around the coordinate origin.\n\n" +
                "It is recommended to apply the rotation to an isotropic image stack.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }
}
