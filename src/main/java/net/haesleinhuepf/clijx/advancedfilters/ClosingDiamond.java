package net.haesleinhuepf.clijx.advancedfilters;


import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_closingDiamond")
public class ClosingDiamond extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image input, Image destination, Number number_of_dilations_and_erotions";
    }

    @Override
    public boolean executeCL() {

        Object[] args = openCLBufferArgs();
        boolean result = closingDiamond(clij, (ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]), asInteger(args[2]));
        releaseBuffers(args);
        return result;
    }

    public static boolean closingDiamond(CLIJ clij, ClearCLBuffer input, ClearCLBuffer output, Integer radius) {

        ClearCLBuffer temp1 = clij.create(input);
        ClearCLBuffer temp2 = clij.create(input);

        clij.op().dilateSphere(input, temp1);
        for (int i = 1; i < radius; i++) {
            clij.op().dilateSphere(temp1, temp2);
            ClearCLBuffer swap = temp1;
            temp1 = temp2;
            temp2 = swap;
        }
        for (int i = 1; i < radius; i++) {
            clij.op().erodeSphere(temp1, temp2);
            ClearCLBuffer swap = temp1;
            temp1 = temp2;
            temp2 = swap;
        }
        clij.op().erodeSphere(temp1, output);

        temp1.close();
        temp2.close();
        return true;
    }

    @Override
    public String getDescription() {
        return "Apply a binary closing to the input image by calling n dilations and n erosions subsequenntly.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
