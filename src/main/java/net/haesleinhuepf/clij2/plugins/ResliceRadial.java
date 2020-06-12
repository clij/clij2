package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.CLIJUtilities;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
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
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_resliceRadial")
public class ResliceRadial extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized {
    @Override
    public String getCategories() {
        return "Transform";
    }

    @Override
    public boolean executeCL() {
        boolean result = getCLIJ2().resliceRadial((ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asFloat(args[3]), asFloat(args[4]), asFloat(args[5]), asFloat(args[6]), asFloat(args[7]), asFloat(args[8]));
        return result;
    }

    /*
     * Deprecated: use resliceRadial instead
     */
    @Deprecated
    public static boolean radialProjection(CLIJ2 clij2, ClearCLBuffer src, ClearCLBuffer dst, Float deltaAngle) {
        return resliceRadial(clij2, src, dst, deltaAngle, 0.0f, src.getWidth() / 2.0f, src.getHeight() / 2.0f,  1.0f, 1.0f);
    }

    public static boolean resliceRadial(CLIJ2 clij2, ClearCLBuffer src, ClearCLBuffer dst, Float deltaAngle) {
        return resliceRadial(clij2, src, dst, deltaAngle, 0.0f, src.getWidth() / 2.0f, src.getHeight() / 2.0f,  1.0f, 1.0f);
    }

    public static boolean resliceRadial(CLIJ2 clij2, ClearCLBuffer src, ClearCLBuffer dst, Float deltaAngle, Float centerX, Float centerY) {
        return resliceRadial(clij2, src, dst, deltaAngle, 0.0f, centerX, centerY, 1.0f, 1.0f);
    }

    public static boolean resliceRadial(CLIJ2 clij2, ClearCLBuffer src, ClearCLBuffer dst, Float deltaAngle, Float startAngleDegrees, Float centerX, Float centerY, Float scaleFactorX, Float scaleFactorY) {
        assertDifferent(src, dst);

        ClearCLImage image = clij2.create(src.getDimensions(), CLIJUtilities.nativeToChannelType(src.getNativeType()));
        clij2.copy(src, image);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", image);
        parameters.put("dst", dst);
        parameters.put("deltaAngle", deltaAngle);
        parameters.put("centerX", centerX);
        parameters.put("centerY", centerY);
        parameters.put("startAngleDegrees", startAngleDegrees);
        parameters.put("scaleX", scaleFactorX);
        parameters.put("scaleY", scaleFactorY);

        clij2.execute(ResliceRadial.class, "reslice_radial_interpolate_" + image.getDimension() + "d_x.cl", "reslice_radial_" + image.getDimension() + "d", dst.getDimensions(), dst.getDimensions(), parameters);

        clij2.release(image);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Image destination, Number numberOfAngles, Number angleStepSize, Number startAngleDegrees, Number centerX, Number centerY, Number scaleFactorX, Number scaleFactorY";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        int numberOfAngles = asInteger(args[2]);
        float angleStepSize = asFloat(args[3]);
        int effectiveNumberOfAngles = (int)((float)numberOfAngles / angleStepSize);
        int maximumRadius = (int)Math.sqrt(Math.pow(input.getWidth() / 2, 2) + Math.pow(input.getHeight() / 2, 2));
        return getCLIJ2().create(new long[]{maximumRadius, input.getDepth(), effectiveNumberOfAngles}, input.getNativeType());
    }

    @Override
    public String getDescription() {
        return "Computes a radial projection of an image stack. \n\nStarting point for the line is the given point in any \n" +
                "X/Y-plane of a given input image stack. Furthermore, radius of the resulting projection must be given " +
                "and scaling factors in X and Y in case pixels are not isotropic." +
                "This operation is similar to ImageJs 'Radial Reslice' method but offers less flexibility.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
