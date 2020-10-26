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

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_bottomHatSphere")
public class BottomHatSphere extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
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
        return "Image input, ByRef Image destination, Number radiusX, Number radiusY, Number radiusZ";
    }

    @Override
    public boolean executeCL() {

        Object[] args = openCLBufferArgs();
        boolean result = getCLIJ2().bottomHatSphere((ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]), asInteger(args[2]), asInteger(args[3]), asInteger(args[4]));
        releaseBuffers(args);
        return result;
    }

    public static boolean bottomHatSphere(CLIJ2 clij2, ClearCLBuffer input, ClearCLBuffer output, Integer radiusX, Integer radiusY, Integer radiusZ) {

        ClearCLBuffer temp1 = clij2.create(input);
        ClearCLBuffer temp2 = clij2.create(input);

        if (input.getDimension() == 3) {
            clij2.maximum3DSphere(input, temp1, radiusX, radiusX, radiusZ);
            clij2.minimum3DSphere(temp1, temp2, radiusX, radiusY, radiusZ);
        } else {
            clij2.maximum2DSphere(input, temp1, radiusX, radiusX);
            clij2.minimum2DSphere(temp1, temp2, radiusX, radiusY);
        }
        clij2.subtractImages(temp2, input, output);

        temp1.close();
        temp2.close();
        return true;
    }

    @Override
    public String getDescription() {
        return "Applies a bottom-hat filter for background subtraction to the input image.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public String getCategories() {
        return "Background, Filter";
    }
}
