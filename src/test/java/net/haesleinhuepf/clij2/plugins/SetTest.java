package net.haesleinhuepf.clij2.plugins;

import ij.IJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.test.TestUtilities;
import net.haesleinhuepf.clijx.CLIJx;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class SetTest {

    @Test
    public void set3d() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus mask3d = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);

        ClearCLImage imageCL = clijx.convert(mask3d, ClearCLImage.class);

        clijx.set(imageCL, 2f);

        double sum = clijx.sumPixels(imageCL);

        assertTrue(sum
                == imageCL.getWidth()
                * imageCL.getHeight()
                * imageCL.getDepth()
                * 2);

        imageCL.close();
        IJ.exit();
        clijx.clear();
    }

    @Test
    public void set3d_Buffers() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus mask3d = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);

        ClearCLBuffer imageCL = clijx.convert(mask3d, ClearCLBuffer.class);

        clijx.set(imageCL, 2f);

        double sum = clijx.sumPixels(imageCL);

        assertTrue(sum
                == imageCL.getWidth()
                * imageCL.getHeight()
                * imageCL.getDepth()
                * 2);

        imageCL.close();
        IJ.exit();
        clijx.clear();
    }

    @Test
    public void set2d() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testImp2 = TestUtilities.getRandomImage(100, 100, 1, 32, 1, 100);

        ClearCLImage imageCL = clijx.convert(testImp2, ClearCLImage.class);;

        clijx.set(imageCL, 2f);

        double sum = clijx.sumPixels(imageCL);

        assertTrue(sum == imageCL.getWidth() * imageCL.getHeight() * 2);

        imageCL.close();
        IJ.exit();
        clijx.clear();
    }


    @Test
    public void set2d_Buffers() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testImp2 = TestUtilities.getRandomImage(100, 100, 1, 32, 1, 100);

        ClearCLBuffer imageCL = clijx.convert(testImp2, ClearCLBuffer.class);;

        clijx.set(imageCL, 2f);

        double sum = clijx.sumPixels(imageCL);

        assertTrue(sum == imageCL.getWidth() * imageCL.getHeight() * 2);

        imageCL.close();
        IJ.exit();
        clijx.clear();
    }

}