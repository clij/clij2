package net.haesleinhuepf.clij2.plugins;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.ImageCalculator;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.test.TestUtilities;
import net.haesleinhuepf.clijx.CLIJx;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MultiplyImagesTest {
    @Test
    public void multiplyPixelwise3d() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testImp1 = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);
        ImagePlus testImp2 = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);

        // do operation with ImageJ
        ImagePlus
                multiplied =
                new ImageCalculator().run("Multiply create stack",
                        testImp1,
                        testImp2);

        // do operation with ClearCL
        ClearCLImage src = clijx.convert(testImp1, ClearCLImage.class);
        ClearCLImage src1 = clijx.convert(testImp2, ClearCLImage.class);
        ClearCLImage dst = clijx.convert(testImp1, ClearCLImage.class);

        clijx.multiplyImages(src, src1, dst);

        ImagePlus multipliedCL = clijx.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(multiplied, multipliedCL));

        src.close();
        src1.close();
        dst.close();
        IJ.exit();
        clijx.clear();
    }

    @Ignore
    @Test
    public void multiplyPixelwise3dathousandtimes() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testImp1 = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);
        ImagePlus testImp2 = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);

        // do operation with ImageJ
        ImagePlus
                multiplied =
                new ImageCalculator().run("Multiply create stack",
                        testImp1,
                        testImp2);

        for (int i = 0; i < 1000; i++) {
            // do operation with ClearCL
            ClearCLImage src = clijx.convert(testImp1, ClearCLImage.class);
            ClearCLImage src1 = clijx.convert(testImp2, ClearCLImage.class);
            ClearCLImage dst = clijx.convert(testImp1, ClearCLImage.class);

            clijx.multiplyImages(src, src1, dst);

            ImagePlus multipliedCL = clijx.convert(dst, ImagePlus.class);

            assertTrue(TestUtilities.compareImages(multiplied, multipliedCL));

            src.close();
            src1.close();
            dst.close();
        }
        IJ.exit();
        clijx.clear();
    }

    @Test
    public void multiplyPixelwise3d_Buffers() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testImp1 = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);
        ImagePlus testImp2 = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);

        // do operation with ImageJ
        ImagePlus
                multiplied =
                new ImageCalculator().run("Multiply create stack",
                        testImp1,
                        testImp2);

        // do operation with ClearCL
        ClearCLBuffer src = clijx.convert(testImp1, ClearCLBuffer.class);
        ClearCLBuffer src1 = clijx.convert(testImp2, ClearCLBuffer.class);
        ClearCLBuffer dst = clijx.convert(testImp1, ClearCLBuffer.class);

        clijx.multiplyImages(src, src1, dst);

        ImagePlus multipliedCL = clijx.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(multiplied, multipliedCL));

        src.close();
        src1.close();
        dst.close();
        IJ.exit();
        clijx.clear();
    }

    @Ignore
    @Test
    public void multiplyPixelwise3d_Buffers_athousandtimes() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testImp1 = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);
        ImagePlus testImp2 = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);

        // do operation with ImageJ
        ImagePlus
                multiplied =
                new ImageCalculator().run("Multiply create stack",
                        testImp1,
                        testImp2);

        for (int i = 0; i < 1000; i++) {
            // do operation with ClearCL
            ClearCLBuffer src = clijx.convert(testImp1, ClearCLBuffer.class);
            ClearCLBuffer src1 = clijx.convert(testImp2, ClearCLBuffer.class);
            ClearCLBuffer dst = clijx.convert(testImp1, ClearCLBuffer.class);

            clijx.multiplyImages(src, src1, dst);

            ImagePlus multipliedCL = clijx.convert(dst, ImagePlus.class);

            assertTrue(TestUtilities.compareImages(multiplied, multipliedCL));

            src.close();
            src1.close();
            dst.close();
        }
        IJ.exit();
        clijx.clear();
    }



    @Test
    public void multiplyPixelwise2d() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testImp2D1 = TestUtilities.getRandomImage(100, 100, 1, 32, 1, 100);
        ImagePlus testImp2D2 = TestUtilities.getRandomImage(100, 100, 1, 32, 1, 100);

        // do operation with ImageJ
        ImagePlus
                multiplied =
                new ImageCalculator().run("Multiply create",
                        testImp2D1,
                        testImp2D2);

        // do operation with ClearCL
        ClearCLImage src = clijx.convert(testImp2D1, ClearCLImage.class);
        ClearCLImage src1 = clijx.convert(testImp2D2, ClearCLImage.class);
        ClearCLImage dst = clijx.convert(testImp2D1, ClearCLImage.class);

        clijx.multiplyImages( src, src1, dst);

        ImagePlus multipliedCL = clijx.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(multiplied, multipliedCL));

        src.close();
        src1.close();
        dst.close();
        IJ.exit();
        clijx.clear();
    }

    @Test
    public void multiplyPixelwise2d_Buffers() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testImp2D1 = TestUtilities.getRandomImage(100, 100, 1, 32, 1, 100);
        ImagePlus testImp2D2 = TestUtilities.getRandomImage(100, 100, 1, 32, 1, 100);
        // do operation with ImageJ
        ImagePlus
                multiplied =
                new ImageCalculator().run("Multiply create",
                        testImp2D1,
                        testImp2D2);

        // do operation with ClearCL
        ClearCLBuffer src = clijx.convert(testImp2D1, ClearCLBuffer.class);
        ClearCLBuffer src1 = clijx.convert(testImp2D2, ClearCLBuffer.class);
        ClearCLBuffer dst = clijx.convert(testImp2D1, ClearCLBuffer.class);

        clijx.multiplyImages( src, src1, dst);

        ImagePlus multipliedCL = clijx.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(multiplied, multipliedCL));

        src.close();
        src1.close();
        dst.close();
        IJ.exit();
        clijx.clear();
    }

}