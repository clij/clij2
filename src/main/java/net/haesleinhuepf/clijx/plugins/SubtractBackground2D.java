package net.haesleinhuepf.clijx.plugins;


import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_subtractBackground2D")
public class SubtractBackground2D extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image input, Image destination, Number sigmaX, Number sigmaY";
    }

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();
        boolean result = subtractBackground(getCLIJx(), (ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]), asFloat(args[2]), asFloat(args[3]));
        releaseBuffers(args);
        return result;
    }

    public static boolean subtractBackground2D(CLIJx clijx, ClearCLImageInterface input, ClearCLImageInterface output, Float sigmaX, Float sigmaY) {
        return subtractBackground(clijx, input, output, sigmaX, sigmaY);
    }

    public static boolean subtractBackground(CLIJx clijx, ClearCLImageInterface input, ClearCLImageInterface output, Float sigmaX, Float sigmaY) {

        ClearCLBuffer background = clijx.create(input.getDimensions(), input.getNativeType());

        clijx.blur(input, background, sigmaX, sigmaY);

        clijx.subtractImages(input, background, output);

        clijx.release(background);
        return true;
    }

    @Override
    public String getDescription() {
        return "Applies Gaussian blur to the input image and subtracts the result from the original image.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }
}
