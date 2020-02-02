package net.haesleinhuepf.clijx.matrix;


import net.haesleinhuepf.clijx.plugins.ConnectedComponentsLabeling;
import net.haesleinhuepf.clij2.plugins.CountNonZeroPixels;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_spotsToPointList")
public class SpotsToPointList extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image input_spots, Image destination_pointlist";
    }

    @Override
    public boolean executeCL() {
        return spotsToPointList(getCLIJx(), (ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]));
    }

    public static boolean spotsToPointList(CLIJx clijx, ClearCLBuffer input, ClearCLBuffer output) {
        ClearCLBuffer temp1 = clijx.create(input.getDimensions(), NativeTypeEnum.Float);

        ConnectedComponentsLabeling.connectedComponentsLabeling(clijx, input, temp1);

        clijx.labelledSpotsToPointList(temp1, output);

        temp1.close();
        return true;
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        long numberOfSpots = (long) CountNonZeroPixels.countNonZeroPixels(getCLIJx(), input);

        return clij.create(new long[]{numberOfSpots, input.getDimension()}, NativeTypeEnum.Float);
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
