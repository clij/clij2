package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import org.scijava.plugin.Plugin;

/**
 * Release
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_create2D")
public class Create2D extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public Object[] getDefaultValues() {
        return new Object[]{null, 100, 100, 32};
    }

    @Override
    public boolean executeCL() {
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "ByRef Image destination, Number width, Number height, Number bitDepth";
    }

    @Override
    public String getDescription() {
        return "Allocated memory for a new 2D image in the GPU memory.\n\n" +
                "Parameters\n" +
                "----------\n" +
                "destination : Image\n" +
                "    The new image will be stored in this variable.\n" +
                "width : Number\n" +
                "    The width of the new image.\n" +
                "height : Number\n" +
                "    The height of the new image.\n" +
                "bit-depth : Number\n" +
                "    The bit-depth of the new image. Can be either 8, 16 or 32, to create an image of unsigned-byte, unsigned-short or float type.\n";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        NativeTypeEnum typeEnum = NativeTypeEnum.Float;
        if (asInteger(args[3]) == 8) {
            typeEnum = NativeTypeEnum.UnsignedByte;
        } else if (asInteger(args[3]) == 16) {
            typeEnum = NativeTypeEnum.UnsignedShort;
        }
        return getCLIJ2().create(new long[]{asInteger(args[1]), asInteger(args[2])}, typeEnum);
    }

}
