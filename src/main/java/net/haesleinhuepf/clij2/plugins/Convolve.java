package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

/**
 *
 *
 * Author: @haesleinhuepf
 *         December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_convolve")
public class Convolve extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
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
        return "Filter";
    }

    @Override
    public boolean executeCL() {
        boolean result = getCLIJ2().convolve((ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), (ClearCLBuffer)(args[2]));
        return result;
    }

    public static boolean convolve(CLIJ2 clij2, ClearCLBuffer src, ClearCLBuffer kernel, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("kernelImage", kernel);
        parameters.put("dst", dst);

        clij2.execute(Convolve.class,
                "convolve_" + src.getDimension() + "d_x.cl",
                "convolve_" + src.getDimension() + "d",
                src.getDimensions(),
                src.getDimensions(),
                parameters);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image convolution_kernel, ByRef Image destination";
    }

    @Override
    public String getDescription() {
        return "Convolve the image with a given kernel image.\n\nIt is recommended that the kernel image has an odd size in X, Y and Z.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        return clij.create(((ClearCLBuffer)args[0]).getDimensions(), NativeTypeEnum.Float);
    }

}