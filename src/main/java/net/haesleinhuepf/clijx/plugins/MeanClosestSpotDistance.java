package net.haesleinhuepf.clijx.plugins;

import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.plugins.CountNonZeroPixels;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clij2.plugins.GenerateDistanceMatrix;
import net.haesleinhuepf.clijx.matrix.ShortestDistances;
import net.haesleinhuepf.clijx.matrix.SpotsToPointList;
import net.haesleinhuepf.clij2.plugins.TransposeXY;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * July 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_meanClosestSpotDistance")
public class MeanClosestSpotDistance extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {

        ClearCLBuffer buffer1 = (ClearCLBuffer)( args[0]);
        ClearCLBuffer buffer2 = (ClearCLBuffer)( args[1]);
        Boolean bidirectional = asBoolean(args[2]);

        double[] minimumDistances = meanClosestSpotDistances(getCLIJx(), buffer1, buffer2, bidirectional);

        ResultsTable table = ResultsTable.getResultsTable();
        table.incrementCounter();
        table.addValue("mean_closest_spot_distance_A_B", minimumDistances[0]);
        if (bidirectional) {
            table.addValue("mean_closest_spot_distance_B_A", minimumDistances[1]);
        }
        table.show("Results");
        return true;
    }

    public static double meanClosestSpotDistances(CLIJx clijx, ClearCLBuffer spotsA, ClearCLBuffer spotsB) {
        return meanClosestSpotDistances(clijx, spotsA, spotsB, false)[0];
    }

    public static double[] meanClosestSpotDistances(CLIJx clijx, ClearCLBuffer spotsA, ClearCLBuffer spotsB, Boolean bidirectional) {
        double[] meanDistances = new double[bidirectional?2:1];

        long numberOfSpots1 = (long) CountNonZeroPixels.countNonZeroPixels(clijx, spotsA);
        ClearCLBuffer pointlist1 = clijx.create(new long[]{numberOfSpots1, spotsA.getDimension()}, NativeTypeEnum.Float);
        SpotsToPointList.spotsToPointList(clijx, spotsA, pointlist1);

        long numberOfSpots2 = (long) CountNonZeroPixels.countNonZeroPixels(clijx, spotsB);
        ClearCLBuffer pointlist2 = clijx.create(new long[]{numberOfSpots2, spotsA.getDimension()}, NativeTypeEnum.Float);
        SpotsToPointList.spotsToPointList(clijx, spotsB, pointlist2);

        ClearCLBuffer distanceMatrix = clijx.create(new long[]{ numberOfSpots1+1, numberOfSpots2+1}, NativeTypeEnum.Float);

        GenerateDistanceMatrix.generateDistanceMatrix(clijx, pointlist1, pointlist2, distanceMatrix);

        //new ImageJ();
        //clijx.show(distanceMatrix, "dis");
        //new WaitForUserDialog("dis").show();

        pointlist1.close();
        pointlist2.close();

        ClearCLBuffer result = clijx.create(new long[]{distanceMatrix.getWidth(), 1}, distanceMatrix.getNativeType());
        ShortestDistances.shortestDistances(clijx, distanceMatrix, result);

        //clijx.show(result, "res");
        //new WaitForUserDialog("dis").show();

        meanDistances[0] = clijx.sumPixels(result) / (result.getWidth() - 1) / result.getHeight() / result.getDepth();
        System.out.println("mean distance A B: " + meanDistances[0]);
        result.close();

        if (bidirectional) {
            ClearCLBuffer transposedMatrix = clijx.create(new long[]{distanceMatrix.getHeight(), distanceMatrix.getWidth()}, distanceMatrix.getNativeType());

            TransposeXY.transposeXY(clijx, distanceMatrix, transposedMatrix);

            ClearCLBuffer result2 = clijx.create(new long[]{transposedMatrix.getWidth(), 1}, transposedMatrix.getNativeType());
            ShortestDistances.shortestDistances(clijx, transposedMatrix, result2);

            meanDistances[1] = clijx.sumPixels(result2) / result2.getWidth() / result2.getHeight() / result2.getDepth();
            System.out.println("mean distance B A: " + meanDistances[1]);

            transposedMatrix.close();
        }

        distanceMatrix.close();

        return meanDistances;
    }

    @Override
    public String getParameterHelpText() {
        return "Image spotsA, Image spotsB, Boolean bidirectional";
    }

    @Override
    public String getDescription() {
        return "Takes two binary images A and B with marked spots and determines for each spot in image A the closest " +
                "spot in image B. Afterwards, it saves the average shortest distances from image A to image B as " +
                "'mean_closest_spot_distance_A_B' and from image B to image A as 'mean_closest_spot_distance_B_A' to the results table. " +
                "The distance between B and A is only determined if the `bidirectional` checkbox is checked.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
