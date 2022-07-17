package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.clearcl.enums.ImageChannelDataType;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij.utilities.AffineTransform;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.CLIJUtilities;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import net.imglib2.realtransform.AffineTransform3D;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_rotate3D")
public class Rotate3D extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
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
        return new Object[]{null, null, 0, 0, 0, true};
    }

    @Override
    public boolean executeCL() {
        float angleX = asFloat(args[2]);
        float angleY = asFloat(args[3]);
        float angleZ = asFloat(args[4]);
        boolean rotateAroundCenter = asBoolean(args[5]);



        ClearCLBuffer input = ((ClearCLBuffer) args[0]);
        ClearCLBuffer output = ((ClearCLBuffer) args[1]);

        return getCLIJ2().rotate3D(input, output, angleX, angleY, angleZ, rotateAroundCenter);
    }


    public static boolean rotate3D(CLIJ2 clij2, ClearCLBuffer input, ClearCLBuffer output, Float angle_x_in_degrees, Float angle_y_in_degrees, Float angle_z_in_degrees, Boolean rotateAroundCenter) {

        angle_x_in_degrees = (float)(-asFloat(angle_x_in_degrees) / 180.0f * Math.PI);
        angle_y_in_degrees = (float)(-asFloat(angle_y_in_degrees) / 180.0f * Math.PI);
        angle_z_in_degrees = (float)(-asFloat(angle_z_in_degrees) / 180.0f * Math.PI);

        AffineTransform3D at = new AffineTransform3D();

        if (rotateAroundCenter) {
            at.translate(-input.getWidth() / 2, -input.getHeight() / 2, -input.getDepth() / 2);
        }
        at.rotate(0, angle_x_in_degrees);
        at.rotate(1, angle_y_in_degrees);
        at.rotate(2, angle_z_in_degrees);
        if (rotateAroundCenter) {
            at.translate(input.getWidth() / 2, input.getHeight() / 2, input.getDepth() / 2);
        }

        if (!clij2.hasImageSupport()) {
            return clij2.affineTransform3D(input, output, AffineTransform.matrixToFloatArray(at));
        } else {
            ClearCLImage image = clij2.create(input.getDimensions(), ImageChannelDataType.Float);
            clij2.copy(input, image);
            clij2.affineTransform3D(image, output, AffineTransform.matrixToFloatArray(at));
            clij2.release(image);
            return true;
        }
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Image destination, Number angle_x_in_degrees, Number angle_y_in_degrees, Number angle_z_in_degrees, Boolean rotateAroundCenter";
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
