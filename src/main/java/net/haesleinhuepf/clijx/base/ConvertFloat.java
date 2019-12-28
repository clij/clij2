package net.haesleinhuepf.clijx.base;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_convertFloat")
public class ConvertFloat extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        return getCLIJx().copy((ClearCLBuffer)args[0], (ClearCLBuffer)args[1]);
    }

    private ClearCLBuffer convertFloat(CLIJx cliJx, ClearCLBuffer input) {
        ClearCLBuffer output = createOutputBufferFromSource(input);
        getCLIJx().copy((ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]));
        return output;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination";
    }

    @Override
    public String getDescription() {
        return "Convert the input image to a float image with 32 bits per pixel.\n" +
                "The target image should not exist with a different type before this \n" +
                "method is called.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        return getCLIJx().create(input.getDimensions(), NativeTypeEnum.Float);
    }
}
