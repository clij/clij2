package net.haesleinhuepf.clij2.plugins;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.Roi;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij2.plugins.MeanOfMaskedPixels;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clij2.plugins.DrawBox;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MeanOfMaskedPixelsTest {
    final double tolerance = 0.01;

    @Test
    public void testMean() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus imp = IJ.openImage("src/test/resources/blobs.tif");

        ClearCLBuffer input = clijx.push(imp);
        ClearCLBuffer mask = clijx.create(input);
        clijx.set(mask, 0f);

        DrawBox.drawBox(clijx, mask, 10f, 10f, 20f, 20f);

        double mean = MeanOfMaskedPixels.meanOfMaskedPixels(clijx, input, mask);

        imp.setRoi(new Roi(10,10, 20, 20));
        double meanRef = imp.getStatistics().mean;

        System.out.println("mean: " + mean);
        System.out.println("meanRef: " + meanRef);

        clijx.clear();

        assertEquals(mean, meanRef, tolerance);
    }
}