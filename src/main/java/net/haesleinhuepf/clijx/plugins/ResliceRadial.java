package net.haesleinhuepf.clijx.plugins;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.utilities.CLIJUtilities;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;

/**
 * ResliceRadial
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 *         December 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_resliceRadial")
public class ResliceRadial extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        boolean result = resliceRadial(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asFloat(args[3]), asFloat(args[4]), asFloat(args[5]), asFloat(args[6]), asFloat(args[7]), asFloat(args[8]));
        return result;
    }

    /*
     * Deprecated: use resliceRadial instead
     */
    @Deprecated
    public static boolean radialProjection(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Float deltaAngle) {
        return resliceRadial(clij, src, dst, deltaAngle, 0.0f, src.getWidth() / 2.0f, src.getHeight() / 2.0f,  1.0f, 1.0f);
    }

    public static boolean resliceRadial(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Float deltaAngle) {
        return resliceRadial(clij, src, dst, deltaAngle, 0.0f, src.getWidth() / 2.0f, src.getHeight() / 2.0f,  1.0f, 1.0f);
    }

    public static boolean resliceRadial(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Float deltaAngle, Float centerX, Float centerY) {
        return resliceRadial(clij, src, dst, deltaAngle, 0.0f, centerX, centerY, 1.0f, 1.0f);
    }

    public static boolean resliceRadial(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Float deltaAngle, Float startAngleDegrees, Float centerX, Float centerY, Float scaleFactorX, Float scaleFactorY) {
        assertDifferent(src, dst);

        ClearCLImage image = clij.create(src.getDimensions(), CLIJUtilities.nativeToChannelType(src.getNativeType()));
        clij.op().copy(src, image);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", image);
        parameters.put("dst", dst);
        parameters.put("deltaAngle", deltaAngle);
        parameters.put("centerX", centerX);
        parameters.put("centerY", centerY);
        parameters.put("startAngleDegrees", startAngleDegrees);
        parameters.put("scaleX", scaleFactorX);
        parameters.put("scaleY", scaleFactorY);

        boolean success = clij.execute(ResliceRadial.class, "resliceRadial_interpolate.cl", "radialProjection3d", parameters);

        image.close();
        return success;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number numberOfAngles, Number angleStepSize, Number startAngleDegrees, Number centerX, Number centerY, Number scaleFactorX, Number scaleFactorY";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        int numberOfAngles = asInteger(args[2]);
        float angleStepSize = asFloat(args[3]);
        int effectiveNumberOfAngles = (int)((float)numberOfAngles / angleStepSize);
        int maximumRadius = (int)Math.sqrt(Math.pow(input.getWidth() / 2, 2) + Math.pow(input.getHeight() / 2, 2));
        return clij.createCLBuffer(new long[]{maximumRadius, input.getDepth(), effectiveNumberOfAngles}, input.getNativeType());
    }

    @Override
    public String getDescription() {
        return "Computes a radial projection of an image stack. Starting point for the line is the given point in any \n" +
                "X/Y-plane of a given input image stack. Furthermore, radius of the resulting projection must be given " +
                "and scaling factors in X and Y in case pixels are not isotropic." +
                "This operation is similar to ImageJs 'Radial Reslice' method but offers less flexibility.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }

}
