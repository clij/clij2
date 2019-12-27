package net.haesleinhuepf.clijx.matrix;


import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_shortestDistances")
public class ShortestDistances extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image distance_matrix, Image destination_minimum_distances";
    }

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();
        boolean result = shortestDistances(getCLIJx(), (ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]));
        releaseBuffers(args);
        return result;
    }

    public static boolean shortestDistances(CLIJx clijx, ClearCLBuffer input, ClearCLBuffer output) {
        ClearCLBuffer temp = clijx.create(new long[]{input.getWidth(), 1, input.getHeight()}, input.getNativeType());

        TransposeYZ.transposeYZ(clijx, input, temp);

        clijx.minimumZProjection(temp, output);

        temp.close();
        return true;
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        return clij.create(new long[]{input.getWidth(), 1}, NativeTypeEnum.Float);
    }

    @Override
    public String getDescription() {
        return "Determine the shortest distance from a distance matrix. This corresponds to the minimum in a matrix for each individial column.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }
}
