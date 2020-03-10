package net.haesleinhuepf.clij2.plugins;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.ImageCalculator;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.test.TestUtilities;
import net.haesleinhuepf.clijx.CLIJx;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class DivideImagesTest {

    @Test
    public void dividePixelwise3d() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testImp1 = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);
        ImagePlus testImp2 = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);

        // do operation with ImageJ
        ImagePlus divided = new ImageCalculator().run("Divide create stack", testImp1, testImp2);

        // do operation with ClearCL
        ClearCLImage src = clijx.convert(testImp1, ClearCLImage.class);
        ClearCLImage src1 = clijx.convert(testImp2, ClearCLImage.class);
        ClearCLImage dst = clijx.create(src);

        clijx.divideImages(src, src1, dst);

        ImagePlus dividedCL = clijx.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(divided, dividedCL, 0.0001));

        src.close();
        src1.close();
        dst.close();
        IJ.exit();
        clijx.clear();
    }


    @Test
    public void dividePixelwise3d_Buffers() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testImp1 = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);
        ImagePlus testImp2 = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);

        // do operation with ImageJ
        ImagePlus
                divided =
                new ImageCalculator().run("Divide create stack",
                        testImp1,
                        testImp2);

        // do operation with ClearCL
        ClearCLBuffer src = clijx.convert(testImp1, ClearCLBuffer.class);
        ClearCLBuffer src1 = clijx.convert(testImp2, ClearCLBuffer.class);
        ClearCLBuffer dst = clijx.create(src);

        clijx.divideImages(src, src1, dst);

        ImagePlus dividedCL = clijx.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(divided, dividedCL, 0.0001));

        src.close();
        src1.close();
        dst.close();
        IJ.exit();
        clijx.clear();
    }

    @Test
    public void dividePixelwise2d() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testImp2D1 = TestUtilities.getRandomImage(100, 100, 1, 32, 1, 100);
        ImagePlus testImp2D2 = TestUtilities.getRandomImage(100, 100, 1, 32, 1, 100);

        // do operation with ImageJ
        ImagePlus
                divided =
                new ImageCalculator().run("Divide create",
                        testImp2D1,
                        testImp2D2);

        // do operation with ClearCL
        ClearCLImage src = clijx.convert(testImp2D1, ClearCLImage.class);
        ClearCLImage src1 = clijx.convert(testImp2D2, ClearCLImage.class);
        ClearCLImage dst = clijx.create(src);

        clijx.divideImages(src, src1, dst);

        ImagePlus dividedCL = clijx.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(divided, dividedCL, 0.0001));

        src.close();
        src1.close();
        dst.close();
        IJ.exit();
        clijx.clear();
    }

    @Test
    public void dividePixelwise2d_Buffers() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testImp2D1 = TestUtilities.getRandomImage(100, 100, 1, 32, 1, 100);
        ImagePlus testImp2D2 = TestUtilities.getRandomImage(100, 100, 1, 32, 1, 100);

        // do operation with ImageJ
        ImagePlus divided = new ImageCalculator().run("Divide create", testImp2D1, testImp2D2);

        // do operation with ClearCL
        ClearCLBuffer src = clijx.convert(testImp2D1, ClearCLBuffer.class);
        ClearCLBuffer src1 = clijx.convert(testImp2D2, ClearCLBuffer.class);
        ClearCLBuffer dst = clijx.create(src);

        clijx.divideImages(src, src1, dst);

        ImagePlus dividedCL = clijx.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(divided, dividedCL, 0.0001));

        src.close();
        src1.close();
        dst.close();
        IJ.exit();
        clijx.clear();
    }

}