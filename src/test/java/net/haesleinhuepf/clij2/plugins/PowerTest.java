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

public class PowerTest {
    private final static double tolerance = 0.01;

    @Test
    public void power() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testImp2D1 = TestUtilities.getRandomImage(100, 100, 1, 32, 1, 100);

        // do operation with ImageJ
        ImagePlus
                squared =
                new ImageCalculator().run("Multiply create",
                        testImp2D1,
                        testImp2D1);

        // do operation with ClearCL
        ClearCLImage src = clijx.convert(testImp2D1, ClearCLImage.class);
        ClearCLImage dst = clijx.create(src);

        clijx.power(src, dst, 2.0f);

        ImagePlus squaredCL = clijx.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(squared, squaredCL, tolerance));

        src.close();
        dst.close();

        IJ.exit();
        clijx.clear();

    }

    @Test
    public void power_Buffers() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testImp2D1 = TestUtilities.getRandomImage(100, 100, 1, 32, 1, 100);

        // do operation with ImageJ
        ImagePlus
                squared =
                new ImageCalculator().run("Multiply create",
                        testImp2D1,
                        testImp2D1);

        // do operation with ClearCL
        ClearCLBuffer src = clijx.convert(testImp2D1, ClearCLBuffer.class);
        ClearCLBuffer dst = clijx.create(src);

        clijx.power(src, dst, 2.0f);

        ImagePlus squaredCL = clijx.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(squared, squaredCL, tolerance));

        src.close();
        dst.close();


        IJ.exit();
        clijx.clear();
    }

}