package net.haesleinhuepf.clij2.plugins;

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

import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;

/**
 * Author: @haesleinhuepf
 * July 2020
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_zPositionRangeProjection")
public class ZPositionRangeProjection extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
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
        return zPositionRangeProjection(getCLIJ2() ,(ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), (ClearCLBuffer)(args[2]), asInteger(args[3]), asInteger(args[4]));
    }

    public static boolean zPositionRangeProjection(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface position, ClearCLImageInterface dst, int start_z, int end_z) {
        assertDifferent(src, dst);

        ClearCLBuffer temp_position = clij2.create(position.getDimensions(), NativeTypeEnum.Float);
        ClearCLBuffer temp_slice = clij2.create(position.getDimensions(), dst.getNativeType());
        for (int z = start_z; z <= end_z; z++) {
            clij2.addImageAndScalar(position, temp_position, z);

            ZPositionProjection.zPositionProjection(clij2, src, temp_position, temp_slice);

            clij2.copySlice(temp_slice, dst, z - start_z);
        }
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source_stack, Image z_position, ByRef Image destination, Number start_z, Number end_z";
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
        return "Project multiple Z-slices of a 3D stack into a new 3D stack.\n\n" +
                "Which Z-slice is defined as the z_position image, which represents an altitude map. " +
                "The two additional numbers define the range relative to the given z-position.";
    }

    @Override
    public Object[] getDefaultValues() {
        return new Object[]{null, null, null, -5, 5};
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
