package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clij2.plugins.Logarithm;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import ij.IJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.test.TestUtilities;
import org.junit.Test;

import static org.junit.Assert.*;

public class LogarithmTest {
    private final static double tolerance = 0.01;

    @Test
    public void log() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testImp2D1 = TestUtilities.getRandomImage(100, 100, 1, 32, 1, 100);
        IJ.run(testImp2D1, "Abs", "");

        // do operation with ImageJ
        ImagePlus logIJ = testImp2D1.duplicate();
        IJ.run(logIJ, "Log", "");
        
        // do operation with ClearCL
        ClearCLImage src = clijx.convert(testImp2D1, ClearCLImage.class);
        ClearCLImage dst = clijx.create(src);

        Logarithm.logarithm(clijx, src, dst);

        ImagePlus logCL = clijx.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(logIJ, logCL, tolerance));

        src.close();
        dst.close();

        IJ.exit();
        clijx.clear();

    }

    @Test
    public void log_Buffers() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testImp2D1 = TestUtilities.getRandomImage(100, 100, 1, 32, 1, 100);
        IJ.run(testImp2D1, "Abs", "");

        // do operation with ImageJ
        ImagePlus logIJ = testImp2D1.duplicate();
        IJ.run(logIJ, "Log", "");

        // do operation with ClearCL
        ClearCLBuffer src = clijx.convert(testImp2D1, ClearCLBuffer.class);
        ClearCLBuffer dst = clijx.create(src);

        Logarithm.logarithm(clijx, src, dst);

        ImagePlus logCL = clijx.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(logIJ, logCL, tolerance));

        src.close();
        dst.close();

        IJ.exit();
        clijx.clear();
    }

}