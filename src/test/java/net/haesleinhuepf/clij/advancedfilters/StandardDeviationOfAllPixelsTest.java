package net.haesleinhuepf.clij.advancedfilters;

import ij.IJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import org.junit.Test;

import static org.junit.Assert.*;

public class StandardDeviationOfAllPixelsTest {
    final double tolerance = 0.01;

    @Test
    public void testStdDev() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus imp = IJ.openImage("src/test/resources/blobs.tif");

        ClearCLBuffer input = clij.push(imp);

        double stdDev = StandardDeviationOfAllPixels.standardDeviationOfAllPixels(clij, input);

        double stdDevRef = imp.getStatistics().stdDev;

        System.out.println("stdDev: " + stdDev);
        System.out.println("stdDevRef: " + stdDevRef);

        assertEquals(stdDev, stdDevRef, tolerance);
    }
}