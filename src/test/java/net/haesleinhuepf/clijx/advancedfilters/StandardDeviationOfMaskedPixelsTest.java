package net.haesleinhuepf.clijx.advancedfilters;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.Roi;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clijx.painting.DrawBox;
import org.junit.Test;

import static org.junit.Assert.*;

public class StandardDeviationOfMaskedPixelsTest {
    final double tolerance = 0.1;

    @Test
    public void testStdDev() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus imp = IJ.openImage("src/test/resources/blobs.tif");

        ClearCLBuffer input = clij.push(imp);
        ClearCLBuffer mask = clij.create(input);
        clij.op().set(mask, 0f);

        DrawBox.drawBox(clij, mask, 10f, 10f, 20f, 20f);

        double stdDev = StandardDeviationOfMaskedPixels.standardDeviationOfMaskedPixels(clij, input, mask);

        imp.setRoi(new Roi(10,10, 20, 20));
        double stdDevRef = imp.getStatistics().stdDev;

        System.out.println("stdDev: " + stdDev);
        System.out.println("stdDevRef: " + stdDevRef);

        assertEquals(stdDevRef, stdDev, tolerance);
    }
}