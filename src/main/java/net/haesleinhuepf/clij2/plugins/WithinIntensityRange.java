package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_withinIntensityRange")
public class WithinIntensityRange extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {

    @Override
    public String getParameterHelpText() {
        return "Image input, ByRef Image destination, Number above_intensity, Number below_intensity";
    }

    @Override
    public Object[] getDefaultValues() {
        return new Object[]{null, null, 128, 256};
    }

    @Override
    public boolean executeCL() {
        return withinIntensityRange(getCLIJ2(), (ClearCLBuffer) args[0], (ClearCLBuffer) args[1], asFloat(args[2]), asFloat(args[3]));
    }

    public static boolean withinIntensityRange(CLIJ2 clijx, ClearCLBuffer pushed, ClearCLBuffer result, Float above_intensity, Float below_intensity) {
        ClearCLBuffer above = clijx.create(pushed.getDimensions(), NativeTypeEnum.UnsignedByte);
        ClearCLBuffer below = clijx.create(pushed.getDimensions(), NativeTypeEnum.UnsignedByte);

        clijx.greaterConstant(pushed, above, above_intensity);
        clijx.smallerConstant(pushed, below, below_intensity);

        clijx.binaryAnd(below, above, result);

        above.close();
        below.close();

        return true;
    }

    @Override
    public String getDescription() {
        return "Generates a binary image where pixels with intensity within the given range are 1 and others are 0.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        return getCLIJ2().create(input.getDimensions(), NativeTypeEnum.UnsignedByte);
    }

    @Override
    public String getCategories() {
        return "Binary,Segmentation";
    }

    @Override
    public String getInputType() {
        return "Image";
    }

    @Override
    public String getOutputType() {
        return "Binary Image";
    }
}
