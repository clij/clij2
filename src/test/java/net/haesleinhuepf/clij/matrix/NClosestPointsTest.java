package net.haesleinhuepf.clij.matrix;

import ij.ImageJ;
import ij.gui.WaitForUserDialog;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij2.CLIJ2;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import org.junit.Test;

import static org.junit.Assert.*;

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

        CLIJ2 clij2 = CLIJ2.getInstance();

        ClearCLBuffer clPointsA = clij2.convert(pointsA, ClearCLBuffer.class);
        ClearCLBuffer clPointsB = clij2.convert(pointsB, ClearCLBuffer.class);

        ClearCLBuffer distanceMatrix = clij2.create(new long[]{clPointsA.getWidth(), clPointsB.getWidth()}, NativeTypeEnum.Float);

        clij2.op.generateDistanceMatrix(clPointsA, clPointsB, distanceMatrix);

        new ImageJ();
        clij2.show(distanceMatrix, "dist");

        int n = 3;
        ClearCLBuffer nClosestPointIndices = clij2.create(new long[]{clPointsA.getWidth(), n}, NativeTypeEnum.Float);
        clij2.op.nClosestPoints(distanceMatrix, nClosestPointIndices);

        clij2.show(nClosestPointIndices, "closestPoints");
        new WaitForUserDialog("closestPoints").show();


    }

}