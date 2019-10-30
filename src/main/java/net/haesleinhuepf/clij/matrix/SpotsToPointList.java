package net.haesleinhuepf.clij.matrix;


import javafx.scene.effect.Light;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.advancedfilters.ConnectedComponentsLabeling;
import net.haesleinhuepf.clij.advancedfilters.CountNonZeroPixels;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_spotsToPointList")
public class SpotsToPointList extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image input_spots, Image destination_pointlist";
    }

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();
        boolean result = spotsToPointList(clij, (ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]));
        releaseBuffers(args);
        return result;
    }

    public static boolean spotsToPointList(CLIJ clij, ClearCLBuffer input, ClearCLBuffer output) {
        ClearCLBuffer temp1 = clij.create(input.getDimensions(), NativeTypeEnum.Float);


        CLIJx clijx = CLIJx.getInstance();
        ConnectedComponentsLabeling.connectedComponentsLabeling(clijx, input, temp1);
        //clij.show(temp1, "cca");

        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("src", temp1);
        parameters.put("dst_point_list", output);

        long[] globalSizes = temp1.getDimensions();

        clij.execute(SpotsToPointList.class, "pointlists.cl", "generate_spotlist", globalSizes, parameters);

        temp1.close();
        return true;
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        long numberOfSpots = (long) CountNonZeroPixels.countNonZeroPixels(clij, input);

        return clij.create(new long[]{numberOfSpots, input.getDimension()}, NativeTypeEnum.Float);
    }

    @Override
    public String getDescription() {
        return "Transforms a spots image as resulting from maxim detection in an image where every column cotains d \n" +
                "pixels (with d = dimensionality of the original image) with the coordinates of the maxima.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
