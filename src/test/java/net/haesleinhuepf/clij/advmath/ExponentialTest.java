package net.haesleinhuepf.clij.advmath;

import net.haesleinhuepf.clij.advancedmath.Exponential;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import ij.IJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.test.TestUtilities;
import org.junit.Test;

import static org.junit.Assert.*;

public class ExponentialTest {
    private final static double tolerance = 1;

    // @Ignore // because Test failed on Intel(R) UHD Graphics 630
    @Test
    public void exp() {
        //CLIJ clij = CLIJ.getInstance();    	
        CLIJ clij = CLIJ.getInstance();
        
        ImagePlus testImp2D1 = TestUtilities.getRandomImage(100, 100, 1, 32, 1, 5);

        // do operation with ImageJ
        ImagePlus expIJ = testImp2D1.duplicate();
        IJ.run(expIJ, "Exp", "");
        
        // do operation with ClearCL
        ClearCLImage src = clij.convert(testImp2D1, ClearCLImage.class);
        ClearCLImage dst = clij.createCLImage(src);

        Exponential.exponential(clij, src, dst);

        ImagePlus expCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(expIJ, expCL, tolerance));

        src.close();
        dst.close();

        IJ.exit();
        clij.close();

    }

    // @Ignore // because Test failed on Intel(R) UHD Graphics 630
    @Test
    public void exp_Buffers() {
        //CLIJ clij = CLIJ.getInstance();
        CLIJ clij = CLIJ.getInstance();
        
        ImagePlus testImp2D1 = TestUtilities.getRandomImage(100, 100, 1, 32, 1, 5);

        // do operation with ImageJ
        ImagePlus expIJ = testImp2D1.duplicate();
        IJ.run(expIJ, "Exp", "");

        // do operation with ClearCL
        ClearCLBuffer src = clij.convert(testImp2D1, ClearCLBuffer.class);
        ClearCLBuffer dst = clij.createCLBuffer(src);

        Exponential.exponential(clij, src, dst);

        ImagePlus expCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(expIJ, expCL, tolerance));

        src.close();
        dst.close();

        IJ.exit();
        clij.close();
    }

}