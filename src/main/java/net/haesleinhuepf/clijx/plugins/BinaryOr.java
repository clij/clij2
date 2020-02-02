package net.haesleinhuepf.clijx.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_binaryOr")
public class BinaryOr extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        binaryOr(getCLIJx(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), (ClearCLBuffer)(args[2]));
        return true;
    }


    public static boolean binaryOr(CLIJx clijx, ClearCLImageInterface src1, ClearCLImageInterface src2, ClearCLImageInterface dst) {
        assertDifferent(src1, dst);
        assertDifferent(src2, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src1", src1);
        parameters.put("src2", src2);
        parameters.put("dst", dst);

        clijx.execute(BinaryOr.class, "binary_or_" + src1.getDimension() + "d_x.cl", "binary_or_" + src1.getDimension() + "d", dst.getDimensions(), dst.getDimensions(), parameters);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image operand1, Image operand2, Image destination";
    }

    @Override
    public String getDescription() {
        return "Computes a binary image (containing pixel values 0 and 1) from two images X and Y by connecting pairs of\n" +
                "pixels x and y with the binary OR operator |.\n" +
                "All pixel values except 0 in the input images are interpreted as 1." +
                "<pre>f(x, y) = x | y</pre>";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
