package net.haesleinhuepf.clij2.plugins;

import ij.plugin.Histogram;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_gammaCorrection")
public class GammaCorrection extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
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
        return "Image input, ByRef Image destination, Number gamma";
    }

    @Override
    public Object[] getDefaultValues() {
        return new Object[]{null, null, 1};
    }

    @Override
    public boolean executeCL() {
        return gammaCorrection(getCLIJ2(), (ClearCLBuffer) args[0], (ClearCLBuffer) args[1], asFloat(args[2]));
    }

    public static boolean gammaCorrection(CLIJ2 clij2, ClearCLBuffer pushed, ClearCLBuffer result, Float gamma) {

        double max_intensity = clij2.maximumOfAllPixels(pushed);

        ClearCLBuffer temp1 = clij2.create(pushed.getDimensions(), NativeTypeEnum.Float);
        ClearCLBuffer temp2 = clij2.create(pushed.getDimensions(), NativeTypeEnum.Float);

        clij2.multiplyImageAndScalar(pushed, temp1, 1.0 / max_intensity);
        clij2.power(temp1, temp2, gamma);
        clij2.multiplyImageAndScalar(temp2, result, max_intensity);

        temp1.close();
        temp2.close();

        new Histogram();

        return true;
    }

    @Override
    public String getDescription() {
        return "Applies a gamma correction to an image.\n\n" +
                "Therefore, all pixels x of the Image X are normalized and the power to gamma g is computed, before normlization is reversed (^ is the power operator):" +
                "f(x) = (x / max(X)) ^ gamma * max(X)";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public String getCategories() {
        return "Filter";
    }
}
