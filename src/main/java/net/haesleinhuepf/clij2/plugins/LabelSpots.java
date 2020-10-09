package net.haesleinhuepf.clij2.plugins;


import ij.ImagePlus;
import ij.process.FloatProcessor;
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

import java.util.HashMap;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_labelSpots")
public class LabelSpots extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Binary Image";
    }

    @Override
    public String getOutputType() {
        return "Label Image";
    }


    @Override
    public String getParameterHelpText() {
        return "Image input_spots, ByRef Image labelled_spots_destination";
    }

    @Override
    public boolean executeCL() {
        return getCLIJ2().labelSpots((ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]));
    }

    public static boolean labelSpots(CLIJ2 clij2, ClearCLBuffer input, ClearCLBuffer output) {
        ClearCLBuffer spotCountPerX = clij2.create(new long[]{input.getDepth(), input.getHeight()});
        clij2.sumXProjection(input, spotCountPerX);

        ClearCLBuffer spotCountPerXY = clij2.create(new long[]{input.getDepth(), input.getHeight()});
        clij2.sumYProjection(spotCountPerX, spotCountPerXY);

        long[] dims = new long[]{1, input.getHeight(), input.getDepth()};
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("dst", output);
        parameters.put("src", input);
        parameters.put("spotCountPerX", spotCountPerX);
        parameters.put("spotCountPerXY", spotCountPerXY);
        clij2.execute(LabelSpots.class, "label_spots_in_x.cl", "label_spots_in_x", dims, dims, parameters);

        spotCountPerX.close();
        spotCountPerXY.close();

        return true;
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        return getCLIJ2().create(input.getDimensions(), getCLIJ2().Float);
    }

    @Override
    public String getDescription() {
        return "Transforms a binary image with single pixles set to 1 to a labelled spots image. \n\n" +
                "Transforms a spots image as resulting from maximum/minimum detection in an image of the same size where every spot has " +
                "a number 1, 2, ... n.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public String getCategories() {
        return "Binary, Label, Filter";
    }
}
