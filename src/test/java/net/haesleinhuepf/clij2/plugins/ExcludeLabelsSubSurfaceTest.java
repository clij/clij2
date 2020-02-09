package net.haesleinhuepf.clij2.plugins;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij2.CLIJ2;

import static org.junit.Assert.*;

public class ExcludeLabelsSubSurfaceTest {
    public static void main(String... args) {
        CLIJ2 clij2 = CLIJ2.getInstance();
        ImagePlus imp = IJ.openImage("src/test/resources/blobs.tif");

        ClearCLBuffer image = clij2.push(imp);
        ClearCLBuffer binary = clij2.create(image);
        ClearCLBuffer labelmap = clij2.create(image);
        ClearCLBuffer labelTemp = clij2.create(image);
        ClearCLBuffer surfaceLabels = clij2.create(image);

        clij2.automaticThreshold(image,binary, "Otsu");
        clij2.connectedComponentsLabeling(binary, labelmap);

        ClearCLBuffer flag = clij2.create(1,1,1);
        for (int i = 0; i < 20; i++) {
            clij2.onlyzeroOverwriteMaximumBox(labelmap, flag, labelTemp);
            clij2.onlyzeroOverwriteMaximumDiamond(labelTemp, flag, labelmap);
        }
        clij2.show(labelmap, "labelamp");

        int numberOfPoints = (int) clij2.maximumOfAllPixels(labelmap);
        ClearCLBuffer pointlist = clij2.create(numberOfPoints, 2);
        clij2.centroidsOfLabels(labelmap, pointlist);
        new ImageJ();

        ExcludeLabelsSubSurface.excludeLabelsSubSurface(clij2, pointlist, labelmap, surfaceLabels, 128f, 128f, 0f);

        clij2.show(surfaceLabels, "surf");
    }


}