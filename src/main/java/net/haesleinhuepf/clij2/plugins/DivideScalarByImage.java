package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.plugins.DivideImages;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

import java.util.Arrays;
import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;
import static net.haesleinhuepf.clij2.utilities.CLIJUtilities.checkDimensions;

/**
 * Author: @haesleinhuepf
 * February 2021
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_divideScalarByImage")
public class DivideScalarByImage extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
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
    public boolean executeCL() {
        return divideScalarByImage(getCLIJ2(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asFloat(args[2]));
    }

    public static boolean divideScalarByImage(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst, Float scalar) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("scalar", scalar);
        parameters.put("dst", dst);

        clij2.execute(DivideScalarByImage.class, "divide_scalar_by_image_" + src.getDimension() + "d_x.cl", "divide_scalar_by_image_" + src.getDimension() + "d", dst.getDimensions(), dst.getDimensions(), parameters);
        return true;
    }


    @Override
    public String getParameterHelpText() {
        return "Image image, ByRef Image destination, Number scalar";
    }

    @Override
    public String getDescription() {
        return "Divides a scalar s by image X pixel wise. \n\n" +
                "<pre>f(s, x) = s / x</pre>";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        return getCLIJ2().create(input.getDimensions(), NativeTypeEnum.Float);
    }
}
