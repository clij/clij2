package net.haesleinhuepf.clij.advancedfilters;


import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_subtractBackground3D")
public class SubtractBackground3D extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image input, Image destination, Number sigmaX, Number sigmaY, Number sigmaZ";
    }

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();
        boolean result = subtractBackground(clij, (ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]), asFloat(args[2]), asFloat(args[3]), asFloat(args[4]));
        releaseBuffers(args);
        return result;
    }

    public static boolean subtractBackground(CLIJ clij, ClearCLBuffer input, ClearCLBuffer output, Float sigmaX, Float sigmaY, Float sigmaZ) {

        ClearCLBuffer background = clij.create(input);

        clij.op().blur(input, background, sigmaX, sigmaY, sigmaZ);

        clij.op().subtractImages(input, background, output);

        background.close();
        return true;
    }

    @Override
    public String getDescription() {
        return "Applies Gaussian blur to the input image and subtracts the result from the original image.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }
}
