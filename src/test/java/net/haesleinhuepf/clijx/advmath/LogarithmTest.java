package net.haesleinhuepf.clijx.advmath;

import net.haesleinhuepf.clijx.advancedmath.Logarithm;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import ij.IJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.test.TestUtilities;
import org.junit.Test;

import static org.junit.Assert.*;

public class LogarithmTest {
    private final static double tolerance = 0.01;

    @Test
    public void log() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp2D1 = TestUtilities.getRandomImage(100, 100, 1, 32, 1, 100);
        IJ.run(testImp2D1, "Abs", "");

        // do operation with ImageJ
        ImagePlus logIJ = testImp2D1.duplicate();
        IJ.run(logIJ, "Log", "");
        
        // do operation with ClearCL
        ClearCLImage src = clij.convert(testImp2D1, ClearCLImage.class);
        ClearCLImage dst = clij.createCLImage(src);

        Logarithm.logarithm(clij, src, dst);

        ImagePlus logCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(logIJ, logCL, tolerance));

        src.close();
        dst.close();

        IJ.exit();
        clij.close();

    }

    @Test
    public void log_Buffers() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp2D1 = TestUtilities.getRandomImage(100, 100, 1, 32, 1, 100);
        IJ.run(testImp2D1, "Abs", "");

        // do operation with ImageJ
        ImagePlus logIJ = testImp2D1.duplicate();
        IJ.run(logIJ, "Log", "");

        // do operation with ClearCL
        ClearCLBuffer src = clij.convert(testImp2D1, ClearCLBuffer.class);
        ClearCLBuffer dst = clij.createCLBuffer(src);

        Logarithm.logarithm(clij, src, dst);

        ImagePlus logCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(logIJ, logCL, tolerance));

        src.close();
        dst.close();

        IJ.exit();
        clij.close();
    }

}