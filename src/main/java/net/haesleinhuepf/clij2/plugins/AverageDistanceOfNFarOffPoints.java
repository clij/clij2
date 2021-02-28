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

import java.util.HashMap;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_averageDistanceOfNFarOffPoints")
public class AverageDistanceOfNFarOffPoints extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Matrix";
    }

    @Override
    public String getOutputType() {
        return "Vector";
    }

    @Override
    public String getCategories() {
        return "Measurements, Graph";
    }

    @Override
    public String getParameterHelpText() {
        return "Image distance_matrix, ByRef Image distance+_list_destination, Number n_far_off_points_to_find";
    }

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();
        boolean result = averageDistanceOfNFarOffPoints(getCLIJ2(), (ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]), asInteger(args[2]));
        releaseBuffers(args);
        return result;

    }

    public static boolean averageDistanceOfNFarOffPoints(CLIJ2 clij2, ClearCLBuffer distance_matrix, ClearCLBuffer indexlist_destination, Integer nPoints) {

            //ClearCLBuffer temp = clij.create(new long[]{input.getWidth(), 1, input.getHeight()}, input.getNativeType());

        if (indexlist_destination.getHeight() > 1000) {
            System.out.println("Warning: NFarOffPoints is limited to n=1000 for technical reasons.");
        }

        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("src_distancematrix", distance_matrix);
        parameters.put("dst_indexlist", indexlist_destination);
        parameters.put("nPoints", nPoints);

        long[] globalSizes = new long[]{distance_matrix.getWidth()};

        clij2.activateSizeIndependentKernelCompilation();
        clij2.execute(AverageDistanceOfNFarOffPoints.class, "average_distance_of_n_far_off_distances_x.cl", "average_distance_of_n_far_off_points", globalSizes, globalSizes, parameters);

        return true;
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        return clij.create(new long[]{input.getWidth(), 1, 1}, NativeTypeEnum.Float);
    }

    @Override
    public String getDescription() {
        return "Determines the average of the n far off (most distant) points for every point in a distance matrix.\n\n" +
                "This corresponds to the average of the n maximum values (rows) for each column of the distance matrix.\n\n" +
                "Parameters\n" +
                "----------\n" +
                "distance_matrix : Image\n" +
                "    The a distance matrix to be processed.\n" +
                "distance_list_destination : Image\n" +
                "    A vector image with the same width as the distance matrix and height=1, depth=1.\n" +
                "    Determined average distances will be written into this vector.\n" +
                "n_far_off_points_to_find : Number\n" +
                "    Number of largest distances which should be averaged.\n";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }

}
