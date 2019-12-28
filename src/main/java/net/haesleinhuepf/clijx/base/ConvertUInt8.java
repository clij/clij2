package net.haesleinhuepf.clijx.base;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.kernels.Kernels;
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

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_convertUInt8")
public class ConvertUInt8 extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        return getCLIJx().copy((ClearCLBuffer)args[0], (ClearCLBuffer)args[1]);
    }

    private ClearCLBuffer convertUInt8(CLIJx cliJx, ClearCLBuffer input) {
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
        return "Convert the input image to a unsigned integer image with 8 bits per pixel.\n" +
                "Pixel values are copied as they are. Use multiplyImageWithScalar in order to scale" +
                "pixel values when reducing bit-depth to prevent cutting-off intensity ranges.\n" +
                "The target image should not exist with a different type before this \n" +
                "method is called.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        return getCLIJx().create(input.getDimensions(), NativeTypeEnum.UnsignedByte);
    }
}
