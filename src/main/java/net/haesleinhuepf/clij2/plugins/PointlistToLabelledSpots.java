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

import java.nio.ShortBuffer;
import java.util.HashMap;

/**
 * Author: @haesleinhuepf
 *         April 2020
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_pointlistToLabelledSpots")
public class PointlistToLabelledSpots extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Pointlist";
    }

    @Override
    public String getOutputType() {
        return "Label Image";
    }


    @Override
    public String getParameterHelpText() {
        return "Image pointlist, ByRef Image spots_destination";
    }

    @Override
    public boolean executeCL() {
        ClearCLBuffer pointlist = (ClearCLBuffer) args[0];
        ClearCLBuffer labelledSpots = (ClearCLBuffer) args[1];

        return getCLIJ2().pointlistToLabelledSpots(pointlist, labelledSpots);
    }

    public static boolean pointlistToLabelledSpots(CLIJ2 clij2, ClearCLBuffer pointlist, ClearCLBuffer labelledSpots) {

        ClearCLBuffer ramp = clij2.create(pointlist.getWidth(), pointlist.getHeight() + 1);
        ClearCLBuffer temp = clij2.create(pointlist.getWidth(), pointlist.getHeight() + 1);
        clij2.setRampX(ramp);
        clij2.addImageAndScalar(ramp, temp, 1);
        clij2.paste(pointlist, temp, 0, 0);

        clij2.writeValuesToPositions(temp, labelledSpots);

        ramp.close();

        return true;
    }


    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        ClearCLBuffer pointlist = (ClearCLBuffer) args[0];

        CLIJ2 clij2 = getCLIJ2();
        ClearCLBuffer temp = clij2.create(new long[]{1, pointlist.getHeight()}, NativeTypeEnum.UnsignedShort);

        clij2.maximumXProjection(input, temp);

        short[] array = new short[(int) temp.getHeight()];
        ShortBuffer buffer = ShortBuffer.wrap(array);

        temp.writeTo(buffer, true);
        clij2.release(temp);

        long[] dimensions = new long[array.length];
        for (int d = 0; d < array.length; d++) {
            dimensions[d] = array[d];
        }

        return clij2.create(dimensions, input.getNativeType());
    }

    @Override
    public String getDescription() {
        return "Takes a pointlist with dimensions n times d with n point coordinates in d dimensions and labels corresponding pixels.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public String getCategories() {
        return "Label";
    }
}
