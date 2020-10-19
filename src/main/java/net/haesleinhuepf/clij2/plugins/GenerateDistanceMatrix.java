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

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_generateDistanceMatrix")
public class GenerateDistanceMatrix extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Vector";
    }

    @Override
    public String getOutputType() {
        return "Matrix";
    }


    @Override
    public boolean executeCL() {
        boolean result = getCLIJ2().generateDistanceMatrix((ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), (ClearCLBuffer)(args[2]));
        return result;
    }

    public static boolean generateDistanceMatrix(CLIJ2 clij2, ClearCLBuffer src_pointlist1, ClearCLBuffer src_pointlist2, ClearCLBuffer dst_distance_matrix) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src_point_list1", src_pointlist1);
        parameters.put("src_point_list2", src_pointlist2);
        parameters.put("dst_matrix", dst_distance_matrix);

        long[] globalSizes = new long[]{src_pointlist1.getWidth(),  1, 1};

        clij2.activateSizeIndependentKernelCompilation();
        clij2.execute(GenerateDistanceMatrix.class, "generate_distance_matrix_x.cl", "generate_distance_matrix", globalSizes, globalSizes, parameters);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image coordinate_list1, Image coordinate_list2, ByRef Image distance_matrix_destination";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        ClearCLBuffer input1 = (ClearCLBuffer) args[0];
        ClearCLBuffer input2 = (ClearCLBuffer) args[1];
        return clij.createCLBuffer(new long[]{input1.getWidth() + 1, input2.getWidth() + 1}, NativeTypeEnum.Float);
    }

    @Override
    public String getDescription() {
        return "Computes the distance between all point coordinates given in two point lists.\n\n" +
                "Takes two images containing pointlists (dimensionality n * d, n: number of points and d: dimensionality) " +
                "and builds up a matrix containing the distances between these points. \n\n" +
                "Convention: Given two point lists with dimensionality n * d and m * d, the distance matrix will be of size" +
                "(n + 1) * (m + 1). The first row and column contain zeros. They represent the distance of the objects to a theoretical background object. " +
                "In that way, distance matrices are of the same size as touch matrices (see generateTouchMatrix). " +
                "Thus, one can threshold a distance matrix to generate a touch matrix out of it for drawing meshes.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }

    @Override
    public String getCategories() {
        return "Measurement, Graph";
    }
}
