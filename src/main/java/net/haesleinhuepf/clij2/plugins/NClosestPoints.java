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

import java.util.HashMap;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_nClosestPoints")
public class NClosestPoints extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized {
    @Override
    public String getCategories() {
        return "Graph, Measurements";
    }

    @Override
    public String getParameterHelpText() {
        return "Image distance_matrix, ByRef Image indexlist_destination, Number n";
    }

    @Override
    public boolean executeCL() {
        boolean result = getCLIJ2().nClosestPoints((ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]));
        return result;
    }

    public static boolean nClosestPoints(CLIJ2 clij2, ClearCLBuffer distance_matrix, ClearCLBuffer indexlist_destination) {
        boolean ignore_background = false;
        boolean ignore_self = false;
        return nClosestPoints(clij2, distance_matrix, indexlist_destination, ignore_background, ignore_self);
    }

    public static boolean nClosestPoints(CLIJ2 clij2, ClearCLBuffer distance_matrix, ClearCLBuffer indexlist_destination, boolean ignore_background, boolean ignore_self) {
        ClearCLBuffer distance_matrix_internal = distance_matrix;
        if (ignore_background) {
            distance_matrix_internal = clij2.create(new long[]{distance_matrix.getWidth() - 1, distance_matrix.getHeight() - 1}, NativeTypeEnum.Float);
            clij2.crop(distance_matrix, distance_matrix_internal, 1, 1);
        }

        if (ignore_self) {
            if (!ignore_background) {
                distance_matrix_internal = clij2.create(new long[]{distance_matrix.getWidth(), distance_matrix.getHeight()}, NativeTypeEnum.Float);
                clij2.copy(distance_matrix, distance_matrix_internal);
            }
            clij2.setWhereXequalsY(distance_matrix, Float.MAX_VALUE);
        }

        if (indexlist_destination.getHeight() > 1000) {
            System.out.println("Warning: NClosestPoints is limited to n=1000 for technical reasons.");
        }

        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("src_distancematrix", distance_matrix_internal);
        parameters.put("dst_indexlist", indexlist_destination);

        long[] globalSizes = new long[]{distance_matrix_internal.getWidth(), 1, 1};
        clij2.activateSizeIndependentKernelCompilation();
        clij2.execute(NClosestPoints.class, "n_shortest_points_x.cl", "find_n_closest_points", globalSizes, globalSizes, parameters);

        if (distance_matrix_internal != distance_matrix) {
            distance_matrix_internal.close();
        }

        if (ignore_background) {
            ClearCLBuffer temp = clij2.create(indexlist_destination);
            clij2.copy(indexlist_destination, temp);
            clij2.addImageAndScalar(temp, indexlist_destination, 1);
            temp.close();
        }

            //temp.close();
        return true;
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        return clij.create(new long[]{input.getWidth(), asInteger(args[2])}, NativeTypeEnum.Float);
    }

    @Override
    public String getDescription() {
        return "Determine the n point indices with shortest distance for all points in a distance matrix. \n\n" +
                "This corresponds to the n row indices with minimum values for each column of the distance matrix.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }
}
