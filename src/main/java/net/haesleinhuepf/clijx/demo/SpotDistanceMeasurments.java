package net.haesleinhuepf.clijx.demo;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clij2.plugins.MeanClosestSpotDistance;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.imglib2.realtransform.AffineTransform3D;

public class SpotDistanceMeasurments {
    public static void main(String... args) {
        new ImageJ();
        ImagePlus imp = IJ.openImage("C:/structure/data/blobs.tif");

        CLIJx clijx = CLIJx.getInstance();

        ClearCLBuffer input = clijx.push(imp);
        ClearCLBuffer blurred = clijx.create(input);
        ClearCLBuffer detected = clijx.create(input.getDimensions(), NativeTypeEnum.Float);

        clijx.blur(input, blurred, 5f, 5f);

        clijx.detectMaximaBox(blurred, detected, 3);
        clijx.show(detected, "detected");

        ClearCLBuffer shiftedDetected = clijx.create(detected);
        AffineTransform3D at = new AffineTransform3D();
        at.translate(1.0, 0, 0);
        clijx.affineTransform3D(detected, shiftedDetected, at);

        double distance = MeanClosestSpotDistance.meanClosestSpotDistance(clijx, detected, shiftedDetected);
        System.out.println("Dist: " + distance);
    }
}
