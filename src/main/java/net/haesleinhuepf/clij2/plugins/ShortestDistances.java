package net.haesleinhuepf.clij2.plugins;


import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_shortestDistances")
public class ShortestDistances extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized {
    @Override
    public String getCategories() {
        return "Graph, Measurements";
    }

    @Override
    public String getParameterHelpText() {
        return "Image distance_matrix, ByRef Image destination_minimum_distances";
    }

    @Override
    public boolean executeCL() {
        boolean result = getCLIJ2().shortestDistances((ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]));
        return result;
    }

    public static boolean shortestDistances(CLIJ2 clij2, ClearCLBuffer input, ClearCLBuffer output) {
        ClearCLBuffer temp = clij2.create(new long[]{input.getWidth(), 1, input.getHeight()}, input.getNativeType());

        TransposeYZ.transposeYZ(clij2, input, temp);

        //clij2.show(temp, "temp");

        MinimumZProjectionThresholdedBounded.minimumZProjectionThresholdedBounded(clij2, temp, output, -1f, 1, (int)temp.getDepth());

        temp.close();
        return true;
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        return getCLIJ2().create(new long[]{input.getWidth(), 1}, NativeTypeEnum.Float);
    }

    @Override
    public String getDescription() {
        return "Determine the shortest distance from a distance matrix. \n\nThis corresponds to the minimum for each individial column.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }
}
