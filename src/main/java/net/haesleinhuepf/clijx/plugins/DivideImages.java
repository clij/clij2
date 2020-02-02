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
import static net.haesleinhuepf.clijx.utilities.CLIJUtilities.checkDimensions;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_divideImages")
public class DivideImages extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        return divideImages(getCLIJx(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), (ClearCLBuffer)(args[2]));
    }

    public static boolean divideImages(CLIJx clijx, ClearCLImageInterface src, ClearCLImageInterface src1, ClearCLImageInterface dst) {
        assertDifferent(src, dst);
        assertDifferent(src1, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("src1", src1);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src1.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addImageAndScalar)");
        }

        clijx.execute(DivideImages.class, "divide_images_" + src.getDimension() + "d_x.cl", "divide_images_" + src.getDimension() + "d", dst.getDimensions(), dst.getDimensions(), parameters);
        return true;
    }


    @Override
    public String getParameterHelpText() {
        return "Image divident, Image divisor, Image destination";
    }

    @Override
    public String getDescription() {
        return "Divides two images X and Y by each other pixel wise.\n\n<pre>f(x, y) = x / y</pre>";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
