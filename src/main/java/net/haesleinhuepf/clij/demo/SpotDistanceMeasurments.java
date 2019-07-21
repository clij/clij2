package net.haesleinhuepf.clij.demo;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.advancedfilters.CountNonZeroPixels;
import net.haesleinhuepf.clij.clearcl.ClearCL;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.matrix.GenerateDistanceMatrix;
import net.haesleinhuepf.clij.matrix.ShortestDistances;
import net.haesleinhuepf.clij.matrix.SpotsToPointList;
import net.imglib2.realtransform.AffineTransform3D;

import java.awt.geom.AffineTransform;

public class SpotDistanceMeasurments {
    public static void main(String... args) {
        new ImageJ();
        ImagePlus imp = IJ.openImage("C:/structure/data/blobs.tif");

        CLIJ clij = CLIJ.getInstance();

        ClearCLBuffer input = clij.push(imp);
        ClearCLBuffer blurred = clij.create(input);
        ClearCLBuffer detected = clij.create(input.getDimensions(), NativeTypeEnum.Float);

        clij.op().blur(input, blurred, 5f, 5f);

        clij.op().detectMaximaBox(blurred, detected, 3);
        clij.show(detected, "detected");

        ClearCLBuffer shiftedDetected = clij.create(detected);
        AffineTransform3D at = new AffineTransform3D();
        at.translate(1.0, 0, 0);
        clij.op().affineTransform3D(detected, shiftedDetected, at);


        long numberOfSpots1 = (long) CountNonZeroPixels.countNonZeroPixels(clij, detected);
        ClearCLBuffer pointlist1 = clij.create(new long[]{numberOfSpots1, input.getDimension()}, NativeTypeEnum.Float);
        SpotsToPointList.spotsToPointList(clij, detected, pointlist1);

        long numberOfSpots2 = (long) CountNonZeroPixels.countNonZeroPixels(clij, shiftedDetected);
        ClearCLBuffer pointlist2 = clij.create(new long[]{numberOfSpots2, input.getDimension()}, NativeTypeEnum.Float);
        SpotsToPointList.spotsToPointList(clij, shiftedDetected, pointlist2);

        ClearCLBuffer distanceMatrix = clij.create(new long[]{ numberOfSpots1, numberOfSpots2}, NativeTypeEnum.Float);

        GenerateDistanceMatrix.generateDistanceMatrix(clij, pointlist1, pointlist2, distanceMatrix);

        //clij.show(pointlist1, "pointlist2");
        //clij.show(pointlist2, "pointlist2");
        //clij.show(distanceMatrix, "distanceMatrix");

        ClearCLBuffer result = clij.create(new long[]{distanceMatrix.getWidth(), 1}, distanceMatrix.getNativeType());
        ShortestDistances.shortestDistances(clij, distanceMatrix, result);

        //clij.show(result, "shortest distance");

        double meanDistance = clij.op().sumPixels(result) / result.getWidth() / result.getHeight() / result.getDepth();
        System.out.println("mean distance: " + meanDistance);
    }
}
