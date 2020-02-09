package net.haesleinhuepf.clij2.plugins;

import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.CLIJ2;

import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * July 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_meanClosestSpotDistance")
public class MeanClosestSpotDistance extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {

        ClearCLBuffer buffer1 = (ClearCLBuffer)( args[0]);
        ClearCLBuffer buffer2 = (ClearCLBuffer)( args[1]);
        Boolean bidirectional = asBoolean(args[2]);

        double[] minimumDistances = meanClosestSpotDistance(getCLIJ2(), buffer1, buffer2, bidirectional);

        ResultsTable table = ResultsTable.getResultsTable();
        table.incrementCounter();
        table.addValue("mean_closest_spot_distance_A_B", minimumDistances[0]);
        if (bidirectional) {
            table.addValue("mean_closest_spot_distance_B_A", minimumDistances[1]);
        }
        table.show("Results");
        return true;
    }

    public static double meanClosestSpotDistance(CLIJ2 clij2, ClearCLBuffer spotsA, ClearCLBuffer spotsB) {
        return meanClosestSpotDistance(clij2, spotsA, spotsB, false)[0];
    }

    public static double[] meanClosestSpotDistance(CLIJ2 clij2, ClearCLBuffer spotsA, ClearCLBuffer spotsB, Boolean bidirectional) {
        double[] meanDistances = new double[bidirectional?2:1];

        long numberOfSpots1 = (long) CountNonZeroPixels.countNonZeroPixels(clij2, spotsA);
        ClearCLBuffer pointlist1 = clij2.create(new long[]{numberOfSpots1, spotsA.getDimension()}, NativeTypeEnum.Float);
        SpotsToPointList.spotsToPointList(clij2, spotsA, pointlist1);

        long numberOfSpots2 = (long) CountNonZeroPixels.countNonZeroPixels(clij2, spotsB);
        ClearCLBuffer pointlist2 = clij2.create(new long[]{numberOfSpots2, spotsA.getDimension()}, NativeTypeEnum.Float);
        SpotsToPointList.spotsToPointList(clij2, spotsB, pointlist2);

        ClearCLBuffer distanceMatrix = clij2.create(new long[]{ numberOfSpots1+1, numberOfSpots2+1}, NativeTypeEnum.Float);

        GenerateDistanceMatrix.generateDistanceMatrix(clij2, pointlist1, pointlist2, distanceMatrix);

        //new ImageJ();
        //clij2.show(distanceMatrix, "dis");
        //new WaitForUserDialog("dis").show();

        pointlist1.close();
        pointlist2.close();

        ClearCLBuffer result = clij2.create(new long[]{distanceMatrix.getWidth(), 1}, distanceMatrix.getNativeType());
        ShortestDistances.shortestDistances(clij2, distanceMatrix, result);

        //clij2.show(result, "res");
        //new WaitForUserDialog("dis").show();

        meanDistances[0] = clij2.sumPixels(result) / (result.getWidth() - 1) / result.getHeight() / result.getDepth();
        System.out.println("mean distance A B: " + meanDistances[0]);
        result.close();

        if (bidirectional) {
            ClearCLBuffer transposedMatrix = clij2.create(new long[]{distanceMatrix.getHeight(), distanceMatrix.getWidth()}, distanceMatrix.getNativeType());

            TransposeXY.transposeXY(clij2, distanceMatrix, transposedMatrix);

            ClearCLBuffer result2 = clij2.create(new long[]{transposedMatrix.getWidth(), 1}, transposedMatrix.getNativeType());
            ShortestDistances.shortestDistances(clij2, transposedMatrix, result2);

            meanDistances[1] = clij2.sumPixels(result2) / result2.getWidth() / result2.getHeight() / result2.getDepth();
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
