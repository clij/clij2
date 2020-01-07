package net.haesleinhuepf.clijx.temp;

import ij.IJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clijx.CLIJx;

public class CompareCLIJ12MaxProjection {
    public static void main(String[] args) {
        ImagePlus imp = IJ.openImage("src/test/resources/Haarlem_DZ_thumbnails_sb_text.gif");

        CLIJx clijx = CLIJx.getInstance();
        CLIJ clij = clijx.getClij();

        ClearCLBuffer input = clijx.push(imp);
        ClearCLBuffer output1 = clijx.create(new long[]{input.getWidth(), input.getHeight()}, input.getNativeType());
        ClearCLBuffer output1a = clijx.create(new long[]{input.getWidth(), input.getHeight(), 1}, input.getNativeType());
        ClearCLBuffer output1b = clijx.create(new long[]{input.getWidth(), input.getHeight()}, input.getNativeType());
        ClearCLBuffer output1c = clijx.create(new long[]{input.getWidth(), input.getHeight()}, input.getNativeType());
        ClearCLBuffer output2 = clijx.create(output1);

        clijx.maximumZProjection(input, output1);
        clij.op().maximumZProjection(input, output2);

        double mean1 = clijx.meanOfAllPixels(output1);
        double mean2 = clijx.meanOfAllPixels(output2);

        double mse = clijx.meanSquaredError(output1, output2);

        System.out.println("mean1 " + mean1);
        System.out.println("mean2 " + mean2);
        System.out.println("mse " + mse);


        clijx.maximumZProjection(input, output1a); // 3d -> 3d
        clijx.maximumZProjection(output1a, output1b); // 3d -> 2d
        clijx.maximumZProjection(output1, output1c); // 2d -> 2d

        System.out.println("mean1a " + clijx.meanOfAllPixels(output1a));
        System.out.println("mean1b " + clijx.meanOfAllPixels(output1b));
        System.out.println("mean1c " + clijx.meanOfAllPixels(output1c));


    }
}
