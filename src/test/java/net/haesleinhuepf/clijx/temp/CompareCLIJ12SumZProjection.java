package net.haesleinhuepf.clijx.temp;

import ij.IJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clijx.CLIJx;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CompareCLIJ12SumZProjection {
    @Test
    public void testCompareCLIJ12VariableDims() {
        ImagePlus imp = IJ.openImage("src/test/resources/Haarlem_DZ_thumbnails_sb_text.gif");

        CLIJx clijx = CLIJx.getInstance();
        CLIJ clij = clijx.getClij();

        ClearCLBuffer input = clijx.push(imp);
        ClearCLBuffer output1 = clijx.create(new long[]{input.getWidth(), input.getHeight()}, clijx.Float);
        ClearCLBuffer output1a = clijx.create(new long[]{input.getWidth(), input.getHeight(), 1}, clijx.Float);
        ClearCLBuffer output1b = clijx.create(new long[]{input.getWidth(), input.getHeight()}, clijx.Float);
        ClearCLBuffer output1c = clijx.create(new long[]{input.getWidth(), input.getHeight()}, clijx.Float);
        ClearCLBuffer output1d = clijx.create(new long[]{input.getWidth(), input.getHeight(), 1}, clijx.Float);
        ClearCLBuffer output2 = clijx.create(output1);

        clijx.sumZProjection(input, output1);
        clij.op().sumZProjection(input, output2);

        double mean1 = clijx.meanOfAllPixels(output1);
        double mean2 = clijx.meanOfAllPixels(output2);

        double mse = clijx.meanSquaredError(output1, output2);

        System.out.println("mean1 " + mean1);
        System.out.println("mean2 " + mean2);
        System.out.println("mse " + mse);


        clijx.sumZProjection(input, output1a); // 3d -> 3d
        clijx.sumZProjection(output1a, output1b); // 3d -> 2d
        clijx.sumZProjection(output1, output1c); // 2d -> 2d
        clijx.sumZProjection(output1, output1d); // 2d -> 3d

        double mean1a = clijx.meanOfAllPixels(output1a);
        double mean1b = clijx.meanOfAllPixels(output1b);
        double mean1c = clijx.meanOfAllPixels(output1c);
        double mean1d = clijx.meanOfAllPixels(output1d);

        System.out.println("mean1a " + mean1a);
        System.out.println("mean1b " + mean1b);
        System.out.println("mean1c " + mean1c);
        System.out.println("mean1d " + mean1d);

        assertEquals(0, mse, 0);
        assertEquals(mean1, mean2, 0);
        assertEquals(mean1, mean1a, 0);
        assertEquals(mean1, mean1b, 0);
        assertEquals(mean1, mean1c, 0);
        assertEquals(mean1, mean1d, 0);

        clijx.clear();
    }
}
