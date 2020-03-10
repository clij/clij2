package net.haesleinhuepf.clij2.plugins;

import ij.IJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij2.plugins.VarianceOfAllPixels;
import net.haesleinhuepf.clijx.CLIJx;
import org.junit.Test;

import static org.junit.Assert.*;

public class VarianceOfAllPixelsTest {
    final double tolerance = 1;

    @Test
    public void testVariance() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus imp = IJ.openImage("src/test/resources/blobs.tif");

        ClearCLBuffer input = clijx.push(imp);

        double variance = VarianceOfAllPixels.varianceOfAllPixels(clijx, input);

        double varianceRef = Math.pow(imp.getStatistics().stdDev, 2);

        System.out.println("Variance: " + variance);
        System.out.println("VarianceRef: " + varianceRef);

        clijx.clear();

        assertEquals(varianceRef, variance, tolerance);
    }
}