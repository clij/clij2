package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_convertUInt8")
public class ConvertUInt8 extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        return getCLIJ2().copy((ClearCLBuffer)args[0], (ClearCLBuffer)args[1]);
    }

    private ClearCLBuffer convertUInt8(CLIJ2 cliJx, ClearCLBuffer input) {
        ClearCLBuffer output = createOutputBufferFromSource(input);
        getCLIJ2().copy((ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]));
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
        return getCLIJ2().create(input.getDimensions(), NativeTypeEnum.UnsignedByte);
    }
}
