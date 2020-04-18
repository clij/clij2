package net.haesleinhuepf.clij2.plugins;


import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_labelledSpotsToPointList")
public class LabelledSpotsToPointList extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image input_labelled_spots, ByRef Image destination_pointlist";
    }

    @Override
    public boolean executeCL() {
        return labelledSpotsToPointList(getCLIJ2(), (ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]));
    }

    public static boolean labelledSpotsToPointList(CLIJ2 clij2, ClearCLBuffer input_labelmap, ClearCLBuffer output) {

        HashMap<String, Object> parameters = new HashMap<String, Object>();
        System.out.println("src: " + input_labelmap);
        parameters.put("src", input_labelmap);
        parameters.put("dst_point_list", output);

        long[] globalSizes = input_labelmap.getDimensions();
        clij2.activateSizeIndependentKernelCompilation();
        clij2.execute(LabelledSpotsToPointList.class, "labelled_spots_to_point_list_x.cl", "labelled_spots_to_point_list", globalSizes, globalSizes, parameters);
        return true;
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        long numberOfSpots = (long) CountNonZeroPixels.countNonZeroPixels(getCLIJ2(), input);

        return clij.create(new long[]{numberOfSpots, input.getDimension()}, NativeTypeEnum.Float);
    }

    @Override
    public String getDescription() {
        return "Generates a coordinate list of points in a labelled spot image. \n\n" +
                "Transforms a labelmap of spots (single pixels with values 1, 2, ..., n for n spots) as resulting \n" +
                "from connected components analysis in an image where every column contains d \n" +
                "pixels (with d = dimensionality of the original image) with the coordinates of the maxima/minima.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
