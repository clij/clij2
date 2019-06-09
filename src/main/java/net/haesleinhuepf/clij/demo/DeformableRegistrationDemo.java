package net.haesleinhuepf.clij.demo;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.viewer.ClearCLImageViewer;
import net.haesleinhuepf.clij.registration.DeformableRegistration2D;

public class DeformableRegistrationDemo {
    public static void main(String... args) {
        new ImageJ();
        CLIJ clij = CLIJ.getInstance();

        ImagePlus imp = IJ.openImage("C:\\structure\\data\\piv\\julia\\z16_t40-50.tif");
        //ImagePlus imp = IJ.openImage("C:\\structure\\data\\piv\\bruno\\G1.tif");
        IJ.run(imp, "32-bit", "");

        imp.setT(10);
        ClearCLBuffer slice1 = clij.pushCurrentSlice(imp);
        imp.setT(11);
        ClearCLBuffer slice2 = clij.pushCurrentSlice(imp);

        ClearCLBuffer deformedSlice2 = clij.create(slice1);

        int maxDelta = 5;

        DeformableRegistration2D.deformableRegister(clij, slice1, slice2, deformedSlice2, maxDelta, maxDelta);

        clij.show(slice1, "slice1");
        clij.show(slice2, "slice2");
        clij.show(deformedSlice2, "deformedSlice2");

        slice1.close();
        slice2.close();
        deformedSlice2.close();

    }
}
