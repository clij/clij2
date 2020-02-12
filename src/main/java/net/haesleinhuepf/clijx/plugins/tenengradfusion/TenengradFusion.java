package net.haesleinhuepf.clijx.plugins.tenengradfusion;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.plugins.splitstack.AbstractSplitStack;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_tenengradFusion")
public class TenengradFusion extends AbstractTenengradFusion{

    @Override
    public boolean executeCL() {
        ClearCLBuffer input = (ClearCLBuffer) args[0];
        ClearCLBuffer output = (ClearCLBuffer) args[1];
        int numberOfSubstacks = asInteger(args[2]);
        float sigmaX = asFloat(args[3]);
        float sigmaY = asFloat(args[4]);
        float sigmaZ = asFloat(args[5]);
        float exponent = asFloat(args[6]);

        return tenengradFusion(getCLIJx(), input, output, numberOfSubstacks, sigmaX, sigmaY, sigmaZ, exponent);
    }

    public static boolean tenengradFusion(CLIJx clijx, ClearCLBuffer input, ClearCLBuffer output, Integer number_of_substacks, Float sigmaX, Float sigmaY, Float sigmaZ, Float exponent) {
        ClearCLBuffer[] splitStacks = new ClearCLBuffer[number_of_substacks];
        for (int i = 0; i < number_of_substacks; i++) {
            splitStacks[i] = clijx.create(new long[]{input.getWidth(), input.getHeight(), input.getDepth() / number_of_substacks}, input.getNativeType());
        }

        clijx.getClij().op().splitStack(input, splitStacks);

        float[] sigmas = {sigmaX, sigmaY, sigmaZ};

        AbstractTenengradFusion.tenengradFusion(clijx, output, sigmas, exponent, splitStacks);

        for (int i = 0; i < number_of_substacks; i++) {
            clijx.release(splitStacks[i]);
        }

        return true;
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        return getCLIJx().create(new long[]{input.getWidth(), input.getHeight(), input.getDepth() / asInteger(args[2])}, input.getNativeType());
    }

    @Override
    public String getParameterHelpText() {
        return "Image input, Image destination, Number number_of_substacks, Number sigmaX, Number sigmaY, Number sigmaZ, Number exponent";
    }

}
