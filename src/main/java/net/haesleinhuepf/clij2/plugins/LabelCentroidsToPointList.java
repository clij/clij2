package net.haesleinhuepf.clij2.plugins;


import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import org.scijava.plugin.Plugin;

import java.util.Arrays;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_spotsToPointList")
public class LabelCentroidsToPointList extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Label Image";
    }

    @Override
    public String getOutputType() {
        return "Pointlist";
    }


    @Override
    public String getParameterHelpText() {
        return "Image input_spots, ByRef Image destination_pointlist";
    }

    @Override
    public boolean executeCL() {
        return labelCentroidsToPointList(getCLIJ2(), (ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]));
    }

    public static boolean labelCentroidsToPointList(CLIJ2 clij2, ClearCLBuffer input, ClearCLBuffer output) {
        double[][] stats = clij2.statisticsOfLabelledPixels(input, input);

        ClearCLBuffer table = clij2.pushMatXYZ(stats);
        ClearCLBuffer cl_pos = clij2.create(table.getWidth(), 1);

        clij2.crop(table, cl_pos, 0, StatisticsOfLabelledPixels.STATISTICS_ENTRY.CENTROID_X.value);
        clij2.paste(cl_pos, output, 0, 0);
        clij2.crop(table, cl_pos, 0, StatisticsOfLabelledPixels.STATISTICS_ENTRY.CENTROID_Y.value);
        clij2.paste(cl_pos, output, 0, 1);
        if (output.getHeight() > 2) {
            clij2.crop(table, cl_pos, 0, StatisticsOfLabelledPixels.STATISTICS_ENTRY.CENTROID_Z.value);
            clij2.paste(cl_pos, output, 0, 2);
        }
        cl_pos.close();
        table.close();

        return true;
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        long numberOfSpots = (long) getCLIJ2().getMaximumOfAllPixels(input);

        return getCLIJ2().create(new long[]{numberOfSpots, input.getDimension()}, NativeTypeEnum.Float);
    }

    @Override
    public String getDescription() {
        return "Determines centroids of all labels in a label map image and saves corresponding coordinates in a pointlist.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    public static void main(String[] args) {
        CLIJ2 clij2 = CLIJ2.getInstance();
        ClearCLBuffer labelmap = clij2.pushString("" +
        "1 2\n3 4"
        //        "1 1 1 2 2\n" +
        //        "1 1 2 2 2\n" +
        //        "3 3 3 4 4\n" +
        //        "3 4 4 4 4"
        );

        ClearCLBuffer pointlist = clij2.create(4, 2);

        LabelCentroidsToPointList.labelCentroidsToPointList(clij2, labelmap, pointlist);

        clij2.print(pointlist);

    }


}
