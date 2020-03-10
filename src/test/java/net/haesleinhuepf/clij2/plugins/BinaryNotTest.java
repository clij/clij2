package net.haesleinhuepf.clij2.plugins;

import ij.IJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.test.TestUtilities;
import net.haesleinhuepf.clijx.CLIJx;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BinaryNotTest {

    @Test
    public void binaryNot2d() throws InterruptedException {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testImp2 = TestUtilities.getRandomImage(10, 10, 1, 8, 0, 1);

        ClearCLImage clearCLImage = clijx.convert(testImp2, ClearCLImage.class);;
        ClearCLImage clearCLImageNot = clijx.create(clearCLImage.getDimensions(), clearCLImage.getChannelDataType());

        clijx.binaryNot(clearCLImage, clearCLImageNot);

        long numberOfPixels = clearCLImage.getWidth() * clearCLImage.getHeight() * clearCLImage.getDepth();

        long numberOfPositivePixels = (long) clijx.sumPixels(clearCLImage);
        long numberOfNegativePixels = (long) clijx.sumPixels(clearCLImageNot);

        assertEquals(numberOfPixels, numberOfNegativePixels + numberOfPositivePixels);
        clearCLImage.close();
        clearCLImageNot.close();
        IJ.exit();
        clijx.clear();
    }

    @Test
    public void binaryNot2d_Buffers() throws InterruptedException {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testImp2 = TestUtilities.getRandomImage(100, 100, 3, 8, 0, 1);

        ClearCLBuffer clearCLImage = clijx.convert(testImp2, ClearCLBuffer.class);;
        ClearCLBuffer clearCLImageNot = clijx.create(clearCLImage.getDimensions(), clearCLImage.getNativeType());

        clijx.binaryNot(clearCLImage, clearCLImageNot);

        long numberOfPixels = clearCLImage.getWidth() * clearCLImage.getHeight() * clearCLImage.getDepth();

        long numberOfPositivePixels = (long) clijx.sumPixels(clearCLImage);
        long numberOfNegativePixels = (long) clijx.sumPixels(clearCLImageNot);

        assertEquals(numberOfPixels, numberOfNegativePixels + numberOfPositivePixels);
        clearCLImage.close();
        clearCLImageNot.close();
        IJ.exit();
        clijx.clear();
    }

    @Test
    public void binaryNot3d() throws InterruptedException {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus mask3d = TestUtilities.getRandomImage(100, 100, 3, 8, 0, 1);

        ClearCLImage clearCLImage = clijx.convert(mask3d, ClearCLImage.class);
        ClearCLImage clearCLImageNot = clijx.create(clearCLImage.getDimensions(), clearCLImage.getChannelDataType());

        clijx.binaryNot(clearCLImage, clearCLImageNot);

        long numberOfPixels = clearCLImage.getWidth() * clearCLImage.getHeight() * clearCLImage.getDepth();

        long numberOfPositivePixels = (long) clijx.sumPixels(clearCLImage);
        long numberOfNegativePixels = (long) clijx.sumPixels(clearCLImageNot);

        assertEquals(numberOfPixels, numberOfNegativePixels + numberOfPositivePixels);
        clearCLImage.close();
        clearCLImageNot.close();
        IJ.exit();
        clijx.clear();
    }


    @Test
    public void binaryNot3d_Buffers() throws InterruptedException {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus mask3d = TestUtilities.getRandomImage(100, 100, 3, 8, 0, 1);

        ClearCLBuffer clearCLImage = clijx.convert(mask3d, ClearCLBuffer.class);
        ClearCLBuffer clearCLImageNot = clijx.create(clearCLImage.getDimensions(), clearCLImage.getNativeType());

        clijx.binaryNot(clearCLImage, clearCLImageNot);

        long numberOfPixels = clearCLImage.getWidth() * clearCLImage.getHeight() * clearCLImage.getDepth();

        long numberOfPositivePixels = (long) clijx.sumPixels(clearCLImage);
        long numberOfNegativePixels = (long) clijx.sumPixels(clearCLImageNot);

        assertEquals(numberOfPixels, numberOfNegativePixels + numberOfPositivePixels);
        clearCLImage.close();
        clearCLImageNot.close();
        IJ.exit();
        clijx.clear();
    }

}