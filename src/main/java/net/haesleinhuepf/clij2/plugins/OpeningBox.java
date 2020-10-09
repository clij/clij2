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

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_openingBox")
public class OpeningBox extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Binary Image";
    }

    @Override
    public String getOutputType() {
        return "Binary Image";
    }


    @Override
    public String getParameterHelpText() {
        return "Image input, ByRef Image destination, Number number_of_erotions_and_dilations";
    }

    @Override
    public boolean executeCL() {
        boolean result = getCLIJ2().openingBox((ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]), asInteger(args[2]));
        return result;
    }

    public static boolean openingBox(CLIJ2 clij2, ClearCLBuffer input, ClearCLBuffer output, Integer radius) {

        ClearCLBuffer temp1 = clij2.create(input);
        ClearCLBuffer temp2 = clij2.create(input);

        clij2.erodeBox(input, temp1);
        for (int i = 1; i < radius; i++) {
            clij2.erodeBox(temp1, temp2);
            ClearCLBuffer swap = temp1;
            temp1 = temp2;
            temp2 = swap;
        }
        for (int i = 1; i < radius; i++) {
            clij2.dilateBox(temp1, temp2);
            ClearCLBuffer swap = temp1;
            temp1 = temp2;
            temp2 = swap;
        }
        clij2.dilateBox(temp1, output);

        clij2.release(temp1);
        clij2.release(temp2);
        return true;
    }

    @Override
    public String getDescription() {
        return "Apply a binary opening to the input image by calling n erosions and n dilations subsequenntly.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public String getCategories() {
        return "Binary, Filter";
    }
}
