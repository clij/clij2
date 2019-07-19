package net.haesleinhuepf.clij.advancedfilters.tenengradfusion;

import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.imglib2.Cursor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

/**
 * Author: @haesleinhuepf
 * June 2019
 */
public abstract class AbstractTenengradFusion extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        Object[] args = openCLImageArgs();
        float[] sigmas = new float[3];
        sigmas[0] = asFloat(args[args.length - 3]);
        sigmas[1] = asFloat(args[args.length - 2]);
        sigmas[2] = asFloat(args[args.length - 1]);

        ClearCLImage output = (ClearCLImage)args[args.length - 4];

        ClearCLImage[] input = new ClearCLImage[args.length - 4];
        for (int i = 0; i < args.length - 4; i++) {
            input[i] = (ClearCLImage) args[i];
        }

        clij.op().tenengradFusion(output, sigmas, input);

        clij.op().copy(output, (ClearCLBuffer) this.args[args.length - 4]);
        output.close();
        for (int i = 0; i < args.length - 4; i++) {
            input[i].close();
        }
        return true;
    }

    protected String getParameterHelpText(int num_images) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < num_images; i++) {
            result.append("Image source" + (i + 1) +", ");
        }
        result.append(" Image destination, Number sigmaX, Number sigmaY, Number sigmaZ");
        return result.toString();
    }

    @Override
    public String getDescription() {
        return "Fuses #n# image stacks using Tenengrads algorithm.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }

}
