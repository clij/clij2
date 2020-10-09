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

/**
 * Author: @haesleinhuepf
 *         June 2020
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_absoluteDifference")
public class AbsoluteDifference extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
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
        ClearCLBuffer buffer1 = (ClearCLBuffer)( args[0]);
        ClearCLBuffer buffer2 = (ClearCLBuffer)( args[1]);
        ClearCLBuffer result = (ClearCLBuffer)( args[2]);
        return absoluteDifference(getCLIJ2(), buffer1, buffer2, result);
    }

    public static boolean absoluteDifference(CLIJ2 clij2, ClearCLBuffer buffer1, ClearCLBuffer buffer2, ClearCLBuffer result) {
        ClearCLBuffer difference = clij2.create(buffer1.getDimensions(), NativeTypeEnum.Float);

        clij2.subtractImages(buffer1, buffer2, difference);

        clij2.absolute(difference, result);

        difference.close();
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source1, Image source2, ByRef Image destination";
    }

    @Override
    public String getDescription() {
        return "Determines the absolute difference pixel by pixel between two images.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
