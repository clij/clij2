package net.haesleinhuepf.clij2.plugins;


import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_differenceOfGaussian2D")
public class DifferenceOfGaussian2D extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image input, ByRef Image destination, Number sigma1x, Number sigma1y, Number sigma2x, Number sigma2y";
    }

    @Override
    public boolean executeCL() {
        boolean result = getCLIJ2().differenceOfGaussian((ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]), asFloat(args[2]), asFloat(args[3]), asFloat(args[4]), asFloat(args[5]));
        return result;
    }

    public static boolean differenceOfGaussian2D(CLIJ2 clij2, ClearCLBuffer input, ClearCLBuffer output, Float sigma1x, Float sigma1y, Float sigma2x, Float sigma2y) {
        return differenceOfGaussian(clij2, input, output, sigma1x, sigma1y, sigma2x, sigma2y);
    }

    public static boolean differenceOfGaussian(CLIJ2 clij2, ClearCLBuffer input, ClearCLBuffer output, Float sigma1x, Float sigma1y, Float sigma2x, Float sigma2y) {

        ClearCLBuffer temp1 = clij2.create(input);
        ClearCLBuffer temp2 = clij2.create(input);

        clij2.blur(input, temp1, sigma1x, sigma1y);
        clij2.blur(input, temp2, sigma2x, sigma2y);

        clij2.subtractImages(temp1, temp2, output);

        clij2.release(temp1);
        clij2.release(temp2);
        return true;
    }

    @Override
    public String getDescription() {
        return "Applies Gaussian blur to the input image twice with different sigma values resulting in two images which are then subtracted from each other.\n\n" +
                "It is recommended to apply this operation to images of type Float (32 bit) as results might be negative.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }
}
