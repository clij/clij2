package net.haesleinhuepf.clijx.demo;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clijx.advancedfilters.MeanClosestSpotDistance;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.imglib2.realtransform.AffineTransform3D;

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

        double distance = MeanClosestSpotDistance.meanClosestSpotDistances(clij, detected, shiftedDetected);
        System.out.println("Dist: " + distance);
    }
}
