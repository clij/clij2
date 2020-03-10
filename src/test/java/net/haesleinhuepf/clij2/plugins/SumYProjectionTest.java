package net.haesleinhuepf.clij2.plugins;

import ij.IJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clijx.CLIJx;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class SumYProjectionTest {
    @Test
    public void testCLIJ2VariableDims() {
        ImagePlus imp = IJ.openImage("src/test/resources/Haarlem_DZ_thumbnails_sb_text.gif");

        CLIJx clijx = CLIJx.getInstance();


        ClearCLBuffer input = clijx.push(imp);
        ClearCLBuffer output1 = clijx.create(new long[]{input.getWidth(), input.getDepth()}, clijx.Float);
        ClearCLBuffer output1a = clijx.create(new long[]{input.getWidth(), input.getDepth(), 1}, clijx.Float);
        clijx.sumYProjection(input, output1);

        double mean1 = clijx.meanOfAllPixels(output1);

        System.out.println("mean1 " + mean1);

        clijx.sumYProjection(input, output1a); // 3d -> 3d

        double mean1a = clijx.meanOfAllPixels(output1a);

        System.out.println("mean1a " + mean1a);

        assertEquals(mean1, mean1a, 0);
        assertNotEquals(0, mean1a, 0);

        clijx.clear();
    }
}
