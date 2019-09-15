package net.haesleinhuepf.clij.advancedfilters;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageStatistics;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import org.junit.Test;

import static org.junit.Assert.*;

public class VarianceOfAllPixelsTest {
    final double tolerance = 1;

    @Test
    public void testVariance() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus imp = IJ.openImage("src/test/resources/blobs.tif");

        ClearCLBuffer input = clij.push(imp);

        double variance = VarianceOfAllPixels.varianceOfAllPixels(clij, input);

        double varianceRef = Math.pow(imp.getStatistics().stdDev, 2);

        System.out.println("Variance: " + variance);
        System.out.println("VarianceRef: " + varianceRef);

        assertEquals(varianceRef, variance, tolerance);
    }
}