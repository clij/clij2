package net.haesleinhuepf.clijx.advancedfilters;

import ij.process.AutoThresholder;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

import java.util.Arrays;

/**
 * AutomaticThresholdInplace
 * <p>
 * Author: @haesleinhuepf
 *         November 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_automaticThresholdInplace")
public class AutomaticThresholdInplace extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        ClearCLBuffer dst = (ClearCLBuffer) (args[0]);
        String userSelectedMethod = (String)args[1];

        ClearCLBuffer buffer = clij.create(dst);
        clij.op().copy(dst, buffer);
        Kernels.automaticThreshold(clij, buffer, dst, userSelectedMethod);
        buffer.close();
        return true;
    }


    @Override
    public String getDescription() {
        StringBuilder doc = new StringBuilder();
        doc.append("The automatic thresholder utilizes the threshold methods from ImageJ on a histogram determined on \n" +
                "the GPU to create binary images as similar as possible to ImageJ 'Apply Threshold' method. Enter one \n" +
                "of these methods in the method text field:\n" +
                Arrays.toString(AutoThresholder.getMethods()) );
        return doc.toString();
    }


    @Override
    public String getParameterHelpText() {
        return "Image input_and_destination, String method";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
