package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;
import static net.haesleinhuepf.clij2.utilities.CLIJUtilities.checkDimensions;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_addImagesWeighted")
public class AddImagesWeighted extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
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
        return "Math";
    }

    @Override
    public Object[] getDefaultValues() {
        return new Object[]{null, null, null, Double.valueOf(1), Double.valueOf(1)};
    }

    @Override
    public boolean executeCL() {
        return getCLIJ2().addImagesWeighted((ClearCLImageInterface)( args[0]), (ClearCLImageInterface)(args[1]), (ClearCLImageInterface)(args[2]), asFloat(args[3]), asFloat(args[4]));
    }

    public static boolean addImagesWeighted(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface src1, ClearCLImageInterface dst, Float factor, Float factor1) {
        assertDifferent(src, dst);
        assertDifferent(src1, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("src1", src1);
        parameters.put("factor", factor);
        parameters.put("factor1", factor1);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src1.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addImageAndScalar)");
        }
        clij2.execute(AddImagesWeighted.class, "add_images_weighted_"  + src.getDimension() +  "d_x.cl", "add_images_weighted_" + src.getDimension() + "d", dst.getDimensions(), dst.getDimensions(), parameters);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image summand1, Image summand2, ByRef Image destination, Number factor1, Number factor2";
    }

    @Override
    public String getDescription() {
        return "Calculates the sum of pairs of pixels x and y from images X and Y weighted with factors a and b." +
                "\n\n<pre>f(x, y, a, b) = x * a + y * b</pre>";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
