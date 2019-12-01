package net.haesleinhuepf.clijx.advancedfilters;


import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_differenceOfGaussian3D")
public class DifferenceOfGaussian3D extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image input, Image destination, Number sigma1x, Number sigma1y, Number sigma1z, Number sigma2x, Number sigma2y, Number sigma2z";
    }

    @Override
    public boolean executeCL() {

        Object[] args = openCLBufferArgs();
        boolean result = differenceOfGaussian(clij, (ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]), asFloat(args[2]), asFloat(args[3]), asFloat(args[4]), asFloat(args[5]), asFloat(args[6]), asFloat(args[7]));
        releaseBuffers(args);
        return result;
    }

    public static boolean differenceOfGaussian(CLIJ clij, ClearCLBuffer input, ClearCLBuffer output, Float sigma1x, Float sigma1y, Float sigma1z, Float sigma2x, Float sigma2y, Float sigma2z) {

        ClearCLBuffer temp1 = clij.create(input);
        ClearCLBuffer temp2 = clij.create(input);

        clij.op().blur(input, temp1, sigma1x, sigma1y, sigma1z);
        clij.op().blur(input, temp2, sigma2x, sigma2y, sigma2z);

        clij.op().subtractImages(temp1, temp2, output);

        temp1.close();
        temp2.close();
        return true;
    }

    @Override
    public String getDescription() {
        return "Applies Gaussian blur to the input image twice with different sigma values resulting in two images which are then subtracted from each other.\n\n" +
                "It is recommended to apply this operation to images of type Float (32 bit) as results might be negative.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }
}
