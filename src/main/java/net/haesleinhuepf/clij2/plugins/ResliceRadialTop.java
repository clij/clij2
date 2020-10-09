package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.CLIJUtilities;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
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
 *         May2020
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_resliceRadialTop")
public class ResliceRadialTop extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
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
    public boolean executeCL() {
        ClearCLBuffer input = (ClearCLBuffer)( args[0]);
        ClearCLBuffer result = (ClearCLBuffer)(args[1]);
        float angleStepSize = asFloat(args[3]);
        float startAngleDegrees = asFloat(args[4]);
        float centerX = asFloat(args[5]);
        float centerY = asFloat(args[6]);
        float scaleFactorX = asFloat(args[7]);
        float scaleFactorY = asFloat(args[8]);

        return resliceRadialTop(getCLIJ2(), input, result, angleStepSize, startAngleDegrees, centerX, centerY, scaleFactorX, scaleFactorY);
    }

    private boolean resliceRadialTop(CLIJ2 clij2, ClearCLBuffer input, ClearCLBuffer result, float angleStepSize, float startAngleDegrees, float centerX, float centerY, float scaleFactorX, float scaleFactorY) {
        ClearCLBuffer temp = clij2.create(new long[]{result.getWidth(), result.getDepth(), result.getHeight()}, result.getNativeType());
        clij2.resliceRadial(input, temp, angleStepSize, startAngleDegrees, centerX, centerY, scaleFactorX, scaleFactorY);
        clij2.resliceTop(temp, result);
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
        return getCLIJ2().create(new long[]{maximumRadius, effectiveNumberOfAngles, input.getDepth()}, input.getNativeType());
    }

    @Override
    public String getDescription() {
        return "Computes a radial projection of an image stack and reslices it from top. \n\nStarting point for the line is the given point in any \n" +
                "X/Y-plane of a given input image stack. Furthermore, radius of the resulting projection must be given " +
                "and scaling factors in X and Y in case pixels are not isotropic." +
                "This operation is similar to ImageJs 'Radial Reslice' method combined with 'Reslice from top' but offers less flexibility.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
