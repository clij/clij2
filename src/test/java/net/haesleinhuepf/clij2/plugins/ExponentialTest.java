package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clij2.plugins.Exponential;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import ij.IJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.test.TestUtilities;
import org.junit.Test;

import static org.junit.Assert.*;

public class ExponentialTest {
    private final static double tolerance = 1;

    // @Ignore // because Test failed on Intel(R) UHD Graphics 630
    @Test
    public void exp() {
        CLIJx clijx = CLIJx.getInstance();
        
        ImagePlus testImp2D1 = TestUtilities.getRandomImage(100, 100, 1, 32, 1, 5);

        // do operation with ImageJ
        ImagePlus expIJ = testImp2D1.duplicate();
        IJ.run(expIJ, "Exp", "");
        
        // do operation with ClearCL
        ClearCLImage src = clijx.convert(testImp2D1, ClearCLImage.class);
        ClearCLImage dst = clijx.create(src);

        Exponential.exponential(clijx, src, dst);

        ImagePlus expCL = clijx.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(expIJ, expCL, tolerance));

        src.close();
        dst.close();

        IJ.exit();
        clijx.clear();

    }

    // @Ignore // because Test failed on Intel(R) UHD Graphics 630
    @Test
    public void exp_Buffers() {
        CLIJx clijx = CLIJx.getInstance();
        
        ImagePlus testImp2D1 = TestUtilities.getRandomImage(100, 100, 1, 32, 1, 5);

        // do operation with ImageJ
        ImagePlus expIJ = testImp2D1.duplicate();
        IJ.run(expIJ, "Exp", "");

        // do operation with ClearCL
        ClearCLBuffer src = clijx.convert(testImp2D1, ClearCLBuffer.class);
        ClearCLBuffer dst = clijx.create(src);

        Exponential.exponential(clijx, src, dst);

        ImagePlus expCL = clijx.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(expIJ, expCL, tolerance));

        src.close();
        dst.close();

        IJ.exit();
        clijx.clear();
    }

}