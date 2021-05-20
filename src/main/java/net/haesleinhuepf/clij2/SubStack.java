package net.haesleinhuepf.clij2;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;

/**
 * Author: @haesleinhuepf
 * April 2021
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_subStack")
public class SubStack extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Image";
    }

    @Override
    public String getOutputType() {
        return "Image";
    }


    @Override
    public boolean executeCL() {
        return subStack(getCLIJ2() ,(ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asInteger(args[2]), asInteger(args[3]));
    }

    public static boolean subStack(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst, int start_z, int end_z) {
        assertDifferent(src, dst);

        clij2.crop3D(src, dst, 0, 0, start_z);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source_stack, ByRef Image destination, Number start_z, Number end_z";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        int start_z = asInteger(args[3]);
        int end_z = asInteger(args[4]);

        int range = end_z - start_z + 1;

        return getCLIJ2().create(new long[]{input.getWidth(), input.getHeight(), range}, input.getNativeType());
    }

    @Override
    public String getDescription() {
        return "Crops multiple Z-slices of a 3D stack into a new 3D stack.\n\n";
    }

    @Override
    public Object[] getDefaultValues() {
        return new Object[]{null, null, 0, 100000000};
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }

    @Override
    public String getCategories() {
        return "Projection";
    }
}
