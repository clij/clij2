package net.haesleinhuepf.clij2.plugins;


import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_differenceOfGaussian3D")
public class DifferenceOfGaussian3D extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
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
        return "Background, Noise, Filter";
    }

    @Override
    public Object[] getDefaultValues() {
        return new Object[]{null, null, 2, 2, 2, 10, 10, 10};
    }

    @Override
    public String getParameterHelpText() {
        return "Image input, ByRef Image destination, Number sigma1x, Number sigma1y, Number sigma1z, Number sigma2x, Number sigma2y, Number sigma2z";
    }

    @Override
    public boolean executeCL() {
        boolean result = getCLIJ2().differenceOfGaussian((ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]), asFloat(args[2]), asFloat(args[3]), asFloat(args[4]), asFloat(args[5]), asFloat(args[6]), asFloat(args[7]));
        return result;
    }

    public static boolean differenceOfGaussian3D(CLIJ2 clij2, ClearCLBuffer input, ClearCLBuffer output, Float sigma1x, Float sigma1y, Float sigma1z, Float sigma2x, Float sigma2y, Float sigma2z) {
        return differenceOfGaussian(clij2, input, output,sigma1x,sigma1y,sigma1z,sigma2x,sigma2y,sigma2z);
    }

    public static boolean differenceOfGaussian(CLIJ2 clij2, ClearCLBuffer input, ClearCLBuffer output, Float sigma1x, Float sigma1y, Float sigma1z, Float sigma2x, Float sigma2y, Float sigma2z) {

        ClearCLBuffer temp1 = clij2.create(input);
        ClearCLBuffer temp2 = clij2.create(input);

        clij2.gaussianBlur(input, temp1, sigma1x, sigma1y, sigma1z);
        clij2.gaussianBlur(input, temp2, sigma2x, sigma2y, sigma2z);

        clij2.subtractImages(temp1, temp2, output);

        clij2.release(temp1);
        clij2.release(temp2);
        return true;
    }

    @Override
    public String getDescription() {
        return "Applies Gaussian blur to the input image twice with different sigma values resulting in two images which are then subtracted from each other.\n\n" +
                "It is recommended to apply this operation to images of type Float (32 bit) as results might be negative.\n\n" +
                "Parameters\n" +
                "----------\n" +
                "input : Image\n" +
                "    The input image to be processed.\n" +
                "destination : Image\n" +
                "    The output image where results are written into.\n" +
                "sigma1_x : float\n" +
                "    Sigma of the first Gaussian filter in x\n" +
                "sigma1_y : float\n" +
                "    Sigma of the first Gaussian filter in y\n" +
                "sigma1_z : float\n" +
                "    Sigma of the first Gaussian filter in z\n" +
                "sigma2_x : float\n" +
                "    Sigma of the second Gaussian filter in x\n" +
                "sigma2_y : float\n" +
                "    Sigma of the second Gaussian filter in y\n" +
                "sigma2_z : float\n" +
                "    Sigma of the second Gaussian filter in z";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }
}
