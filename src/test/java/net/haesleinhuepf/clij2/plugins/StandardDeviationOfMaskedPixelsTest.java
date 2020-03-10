package net.haesleinhuepf.clij2.plugins;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.Roi;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij2.plugins.StandardDeviationOfMaskedPixels;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clij2.plugins.DrawBox;
import org.junit.Test;

import static org.junit.Assert.*;

public class StandardDeviationOfMaskedPixelsTest {
    final double tolerance = 0.1;

    @Test
    public void testStdDev() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus imp = IJ.openImage("src/test/resources/blobs.tif");

        ClearCLBuffer input = clijx.push(imp);
        ClearCLBuffer mask = clijx.create(input);
        clijx.set(mask, 0f);

        DrawBox.drawBox(clijx, mask, 10f, 10f, 20f, 20f);

        // ERROR
        double stdDev = StandardDeviationOfMaskedPixels.standardDeviationOfMaskedPixels(clijx, input, mask);

        imp.setRoi(new Roi(10,10, 20, 20));
        double stdDevRef = imp.getStatistics().stdDev;

        System.out.println("stdDev: " + stdDev);
        System.out.println("stdDevRef: " + stdDevRef);

        clijx.clear();

        assertEquals(stdDevRef, stdDev, tolerance);
    }
}