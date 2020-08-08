package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

/**
 * Author: @haesleinhuepf
 *         August 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_powerImages")
public class PowerImages extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized {
    @Override
    public String getCategories() {
        return "Math";
    }

    @Override
    public boolean executeCL() {
        boolean result = getCLIJ2().powerImages((ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), (ClearCLBuffer)(args[2]));
        return result;
    }

    public static boolean powerImages(CLIJ2 clij2, ClearCLBuffer input1, ClearCLBuffer input2, ClearCLBuffer output) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src1", input1);
        parameters.put("src2", input2);
        parameters.put("dst", output);
        clij2.execute(PowerImages.class, "power_images_" + input1.getDimension() + "d_x.cl", "power_images_" + input1.getDimension() +"d", output.getDimensions(), output.getDimensions(), parameters);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image input, Image exponent, ByRef Image destination";
    }

    @Override
    public String getDescription() {
        return "Calculates x to the power of y pixel wise of two images X and Y.";
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
