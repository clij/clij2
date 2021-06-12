package net.haesleinhuepf.clij2.plugins;


import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_divideByGaussianBackground")
public class DivideByGaussianBackground extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Image";
    }

    @Override
    public String getOutputType() {
        return "Image";
    }

    @Override
    public String getParameterHelpText() {
        return "Image input, ByRef Image destination, Number sigmaX, Number sigmaY, Number sigmaZ";
    }

    @Override
    public boolean executeCL() {
        boolean result = divideByGaussianBackground(getCLIJ2(), (ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]), asFloat(args[2]), asFloat(args[3]), asFloat(args[4]));
        return result;
    }

    public static boolean divideByGaussianBackground(CLIJ2 clij2, ClearCLImageInterface input, ClearCLImageInterface output, Float sigmaX, Float sigmaY, Float sigmaZ) {

        ClearCLBuffer background = clij2.create(input.getDimensions(), input.getNativeType());

        clij2.gaussianBlur(input, background, sigmaX, sigmaY, sigmaZ);

        clij2.divideImages(input, background, output);

        clij2.release(background);
        return true;
    }

    @Override
    public String getDescription() {
        return "Applies Gaussian blur to the input image and divides the original by the result.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public String getCategories() {
        return "Filter, Background";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        return getCLIJ2().create(input.getDimensions(), NativeTypeEnum.Float);
    }

}
