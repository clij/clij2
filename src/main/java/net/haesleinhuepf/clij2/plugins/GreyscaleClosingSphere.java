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

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_greyscaleClosingSphere")
public class GreyscaleClosingSphere extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
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
        return "Image source, ByRef Image destination, Number radius_x, Number radius_y, Number radius_z";
    }

    @Override
    public boolean executeCL() {

        boolean result = greyscaleClosingSphere(getCLIJ2(), (ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]), asInteger(args[2]), asInteger(args[3]), asInteger(args[4]));

        return result;
    }

    public static boolean greyscaleClosingSphere(CLIJ2 clij2, ClearCLBuffer source, ClearCLBuffer destination, Integer radius_x, Integer radius_y, Integer radius_z) {

        ClearCLBuffer temp1 = clij2.create(source);
        if (source.getDimension() == 2) {
            clij2.maximum2DSphere(source, temp1, radius_x, radius_y);
            clij2.minimum2DSphere(temp1, destination, radius_x, radius_y);
        } else {
            clij2.maximum3DSphere(source, temp1, radius_x, radius_y, radius_z);
            clij2.minimum3DSphere(temp1, destination, radius_x, radius_y, radius_y);
        }
        temp1.close();

        return true;
    }

    @Override
    public String getDescription() {
        return "Apply a greyscale morphological closing to the input image.\n\n" +
                "It applies a maximum filter first and the result is processed by a minimum filter with given radii.\n" +
                "Low intensity regions smaller than radius will disappear.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public String getCategories() {
        return "Filter, Background";
    }
}
