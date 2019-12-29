package net.haesleinhuepf.clijx.advancedfilters.splitstack;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;

import java.util.HashMap;

import static net.haesleinhuepf.clij.kernels.Kernels.copy;
import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;

/**
 * Author: @haesleinhuepf
 * June 2019
 */
public abstract class AbstractSplitStack extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {

        ClearCLBuffer input = (ClearCLBuffer)args[0];

        ClearCLBuffer[] output = new ClearCLBuffer[args.length - 1];
        for (int i = 1; i < args.length; i++) {
            output[i-1] = (ClearCLBuffer) args[i];
        }
        splitStack(getCLIJx(), input, output);
        return true;
    }

    public static boolean splitStack(CLIJx clijx, ClearCLBuffer clImageIn, ClearCLBuffer... clImagesOut) {
        for (int i = 0; i < clImagesOut.length; i++) {
            assertDifferent(clImageIn, clImagesOut[i]);
        }

        if (clImagesOut.length > 12) {
            throw new IllegalArgumentException("Error: splitStack does not support more than 12 stacks.");
        }
        if (clImagesOut.length == 1) {
            return clijx.copy(clImageIn, clImagesOut[0]);
        }
        if (clImagesOut.length == 0) {
            throw new IllegalArgumentException("Error: splitstack didn't get any output images.");
        }

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", clImageIn);
        for (int i = 0; i < clImagesOut.length; i++) {
            parameters.put("dst" + i, clImagesOut[i]);
        }

        clijx.execute(AbstractSplitStack.class, "split_stack_into_" + clImagesOut.length + "_x.cl", "split_" + clImagesOut.length + "_stacks", clImageIn.getDimensions(), clImageIn.getDimensions(), parameters);
        return true;
    }

    protected String getParameterHelpText(int num_images) {
        StringBuilder result = new StringBuilder();
        result.append(" Image source, ");
        for (int i = 0; i < num_images; i++) {
            result.append("Image destination" + (i + 1));
            if (i < num_images - 1) {
                result.append(", ");
            }
        }
        return result.toString();
    }

    @Override
    public String getDescription() {
        return "Splits an input stack into #n# image stacks. \n" +
                " * Slices 0, n, 2*n, ... will become part of the first output stack.\n" +
                " * Slices 1, n+1, 2*n+1, ... will become part of the second output stack.\n" +
                "Only up to 12 output stacks are supported.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        return clij.create(new long[]{input.getWidth(), input.getHeight(), input.getDepth() / (getParameterHelpText().split(",").length - 1)}, input.getNativeType());
    }
}
