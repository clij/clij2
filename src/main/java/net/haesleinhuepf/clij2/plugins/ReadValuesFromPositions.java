package net.haesleinhuepf.clij2.plugins;


import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_readValuesFromPositions")
public class ReadValuesFromPositions extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Pointlist, Image";
    }

    @Override
    public String getOutputType() {
        return "Vector";
    }


    @Override
    public String getParameterHelpText() {
        return "Image pointlist, Image map_image, ByRef Image values_destination";
    }

    @Override
    public boolean executeCL() {
        boolean result = readValuesFromPositions(getCLIJ2(), (ClearCLBuffer) (args[0]),(ClearCLBuffer) (args[1]), (ClearCLBuffer) (args[2]));
        return result;
    }

    public static boolean readValuesFromPositions(CLIJ2 clij2, ClearCLImageInterface pointlist, ClearCLImageInterface map_image, ClearCLImageInterface values_destination) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.clear();
        parameters.put("pointlist", pointlist);
        parameters.put("map_image", map_image);
        parameters.put("intensities", values_destination);

        long[] dims = new long[]{pointlist.getWidth(), 1, 1};

        clij2.activateSizeIndependentKernelCompilation();
        clij2.execute(ReadValuesFromPositions.class, "read_values_from_positions_x.cl", "read_values_from_positions", dims, dims, parameters);
        return true;
    }

    @Override
    public String getDescription() {
        return "Takes a pointlist and a parametric image and reads parametric values from the positions.\n\n" +
                "The read intensity values are stored in a new vector.\n" +
                "\n" +
                "Parameters\n" +
                "----------\n" +
                "pointlist\n" +
                "map_image\n" +
                "values_destination";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public String getCategories() {
        return "Measurement";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        ClearCLBuffer pointlist = (ClearCLBuffer) args[0];
        return getCLIJ2().create(pointlist.getWidth(), 1, 1);
    }
}
