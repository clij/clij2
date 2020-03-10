package net.haesleinhuepf.clij2.plugins;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij2.plugins.ExcludeLabelsOnSurface;
import net.haesleinhuepf.clijx.CLIJx;

public class ExcludeLabelsOnSurfaceTest {
    public static void main(String... args) {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus imp = IJ.openImage("src/test/resources/blobs.tif");

        ClearCLBuffer image = clijx.push(imp);
        ClearCLBuffer binary = clijx.create(image);
        ClearCLBuffer labelmap = clijx.create(image);
        ClearCLBuffer labelTemp = clijx.create(image);
        ClearCLBuffer surfaceLabels = clijx.create(image);

        clijx.automaticThreshold(image,binary, "Otsu");
        clijx.connectedComponentsLabeling(binary, labelmap);

        ClearCLBuffer flag = clijx.create(1,1,1);
        for (int i = 0; i < 20; i++) {
            clijx.onlyzeroOverwriteMaximumBox(labelmap, flag, labelTemp);
            clijx.onlyzeroOverwriteMaximumDiamond(labelTemp, flag, labelmap);
        }
        clijx.show(labelmap, "labelamp");
        System.out.println("A");
        clijx.excludeLabelsOnEdges(labelmap, labelTemp);
        System.out.println("b");

        int numberOfPoints = (int) clijx.maximumOfAllPixels(labelTemp);
        ClearCLBuffer pointlist = clijx.create(numberOfPoints, 2);
        System.out.println("c");
        clijx.centroidsOfLabels(labelTemp, pointlist);
        System.out.println("d");
        new ImageJ();
        System.out.println("e");
        clijx.show(labelTemp, "labelTemp");

        System.out.println("e1");

        ExcludeLabelsOnSurface.excludeLabelsOnSurface(clijx, pointlist, labelTemp, surfaceLabels, 128f, 128f, 0f);
        System.out.println("f");

        clijx.show(surfaceLabels, "surf");
    }


}