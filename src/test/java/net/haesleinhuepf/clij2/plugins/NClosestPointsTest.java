package net.haesleinhuepf.clij2.plugins;

import ij.ImageJ;
import ij.gui.WaitForUserDialog;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clijx.CLIJx;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import org.junit.Test;

public class NClosestPointsTest {
    @Test
    public void test() {
        Img<UnsignedShortType> pointsA = ArrayImgs.unsignedShorts(new short[]{
                1, 1, 1,
                1, 2, 15
        }, new long[]{3, 2});

        Img<UnsignedShortType> pointsB = ArrayImgs.unsignedShorts(new short[]{
                1, 1, 1, 1, 1,
                11, 12, 13, 14, 15
        }, new long[]{5, 2});

        CLIJx clijx = CLIJx.getInstance();

        ClearCLBuffer clPointsA = clijx.convert(pointsA, ClearCLBuffer.class);
        ClearCLBuffer clPointsB = clijx.convert(pointsB, ClearCLBuffer.class);

        ClearCLBuffer distanceMatrix = clijx.create(new long[]{clPointsA.getWidth(), clPointsB.getWidth()}, NativeTypeEnum.Float);

        clijx.generateDistanceMatrix(clPointsA, clPointsB, distanceMatrix);

        new ImageJ();
        clijx.show(distanceMatrix, "dist");

        int n = 3;
        ClearCLBuffer nClosestPointIndices = clijx.create(new long[]{clPointsA.getWidth(), n}, NativeTypeEnum.Float);
        clijx.nClosestPoints(distanceMatrix, nClosestPointIndices);

        clijx.show(nClosestPointIndices, "closestPoints");
        //new WaitForUserDialog("closestPoints").show();


    }

}