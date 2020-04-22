package net.haesleinhuepf.clij2.plugins;


import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_spotsToPointList")
public class SpotsToPointList extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image input_spots, ByRef Image destination_pointlist";
    }

    @Override
    public boolean executeCL() {
        return spotsToPointList(getCLIJ2(), (ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]));
    }

    public static boolean spotsToPointList(CLIJ2 clij2, ClearCLBuffer input, ClearCLBuffer output) {
        ClearCLBuffer temp1 = clij2.create(input.getDimensions(), NativeTypeEnum.Float);

        clij2.labelSpots(input, temp1);
        //clij2.setNonZeroPixelsToPixelIndex(input, temp1);
        clij2.labelledSpotsToPointList(temp1, output);

        temp1.close();
        return true;
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        long numberOfSpots = (long) CountNonZeroPixels.countNonZeroPixels(getCLIJ2(), input);

        return getCLIJ2().create(new long[]{numberOfSpots, input.getDimension()}, NativeTypeEnum.Float);
    }

    @Override
    public String getDescription() {
        return "Transforms a spots image as resulting from maximum/minimum detection in an image where every column contains d \n" +
                "pixels (with d = dimensionality of the original image) with the coordinates of the maxima/minima.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
