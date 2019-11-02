package net.haesleinhuepf.clijx.matrix;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_generateDistanceMatrix")
public class GenerateDistanceMatrix extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();
        boolean result = generateDistanceMatrix(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), (ClearCLBuffer)(args[2]));
        releaseBuffers(args);
        return result;
    }

    public static boolean generateDistanceMatrix(CLIJ clij, ClearCLBuffer src_pointlist1, ClearCLBuffer src_pointlist2, ClearCLBuffer dst_distance_matrix) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src_point_list1", src_pointlist1);
        parameters.put("src_point_list2", src_pointlist2);
        parameters.put("dst_matrix", dst_distance_matrix);

        long[] globalSizes = new long[]{src_pointlist1.getWidth(),  1, 1};

        return clij.execute(GenerateDistanceMatrix.class, "distance_matrix.cl", "generate_distance_matrix", globalSizes, parameters);
    }

    @Override
    public String getParameterHelpText() {
        return "Image coordinate_list1, Image coordinate_list2, Image distance_matrix_destination";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        ClearCLBuffer input1 = (ClearCLBuffer) args[0];
        ClearCLBuffer input2 = (ClearCLBuffer) args[1];
        return clij.createCLBuffer(new long[]{input1.getWidth(), input2.getWidth()}, NativeTypeEnum.Float);
    }

    @Override
    public String getDescription() {
        return "Takes two images containing coordinates and builds up a matrix containing distance between the points. Convention: image width represents number of points, height represents dimensionality (2D, 3D, ... 10D). The result image has width the first input image and height equals to the width of the second input image.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }
}
