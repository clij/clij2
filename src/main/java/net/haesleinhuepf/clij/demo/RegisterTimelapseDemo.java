package net.haesleinhuepf.clij.demo;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.registration.TranslationTimelapseRegistration;

public class RegisterTimelapseDemo {
    public static void main(String... args) {
        new ImageJ();
        CLIJ clij = CLIJ.getInstance();

        ImagePlus imp = IJ.openImage("C:/structure/data/piv/julia/z16_t30-50.tif");
        imp.show();

        TranslationTimelapseRegistration ttr = new TranslationTimelapseRegistration();
        ttr.setClij(clij);

        ClearCLBuffer input = clij.push(imp);
        ClearCLBuffer output = clij.create(new long[]{input.getWidth(), input.getHeight(), input.getDepth() - 1}, input.getNativeType());

        ttr.setArgs(new Object[]{input, output});
        ttr.executeCL();

        clij.show(input, "input");
        clij.show(output, "output");

        input.close();
        output.close();
    }
}
