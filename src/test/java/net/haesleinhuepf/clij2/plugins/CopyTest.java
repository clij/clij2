package net.haesleinhuepf.clij2.plugins;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.Duplicator;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.test.TestUtilities;
import net.haesleinhuepf.clijx.CLIJx;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class CopyTest {

    @Test
    public void copyImageToBuffer3d() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testImp1 = TestUtilities.getRandomImage(100, 100, 3, 32, 0, 100);

        ClearCLImage src = clijx.convert(testImp1, ClearCLImage.class);
        ClearCLBuffer dst = clijx.create(src.getDimensions(), src.getNativeType());

        clijx.copy(src, dst);
        ImagePlus copyFromCL = clijx.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(testImp1, copyFromCL));

        src.close();
        dst.close();
        IJ.exit();
        clijx.clear();
    }

    @Test
    public void copyImageToBuffer2d() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testImp2D1 = TestUtilities.getRandomImage(100, 100, 1, 32, 0, 100);

        ClearCLImage src = clijx.convert(testImp2D1, ClearCLImage.class);
        ClearCLBuffer dst = clijx.create(src.getDimensions(), src.getNativeType());

        clijx.copy(src, dst);
        ImagePlus copyFromCL = clijx.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(testImp2D1, copyFromCL));

        src.close();
        dst.close();
        IJ.exit();
        clijx.clear();
    }

    @Test
    public void copyBufferToImage3d() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testImp1 = TestUtilities.getRandomImage(100, 100, 3, 32, 0, 100);

        ClearCLBuffer src = clijx.convert(testImp1, ClearCLBuffer.class);
        ClearCLImage dst = clijx.convert(testImp1, ClearCLImage.class);

        clijx.set(dst, 0f);

        clijx.copy(src, dst);
        ImagePlus copyFromCL = clijx.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(testImp1, copyFromCL));

        src.close();
        dst.close();
        IJ.exit();
        clijx.close();
    }

    @Test
    public void copyBufferToImage2d() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testImp2D1 = TestUtilities.getRandomImage(100, 100, 1, 32, 0, 100);

        ClearCLBuffer src = clijx.convert(testImp2D1, ClearCLBuffer.class);
        ClearCLImage dst = clijx.convert(testImp2D1, ClearCLImage.class);

        clijx.set(dst, 0f);

        clijx.copy(src, dst);
        ImagePlus copyFromCL = clijx.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(testImp2D1, copyFromCL));

        src.close();
        dst.close();
        IJ.exit();
        clijx.clear();
    }

    @Test
    public void copy3d() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testImp1 = TestUtilities.getRandomImage(100, 100, 3, 32, 0, 100);

        ClearCLImage src = clijx.convert(testImp1, ClearCLImage.class);
        ClearCLImage dst = clijx.create(src.getDimensions(), src.getChannelDataType());

        clijx.copy(src, dst);
        ImagePlus copyFromCL = clijx.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(testImp1, copyFromCL));

        src.close();
        dst.close();
        IJ.exit();
        clijx.clear();
    }

    @Test
    public void copy2d() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testImp2D1 = TestUtilities.getRandomImage(100, 100, 1, 32, 0, 100);

        ClearCLImage src = clijx.convert(testImp2D1, ClearCLImage.class);
        ClearCLImage dst = clijx.create(src.getDimensions(), src.getChannelDataType());

        clijx.copy(src, dst);
        ImagePlus copyFromCL = clijx.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(testImp2D1, copyFromCL));

        src.close();
        dst.close();
        IJ.exit();
        clijx.clear();
    }

    @Test
    public void copyBuffer3d() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testImp1 = TestUtilities.getRandomImage(100, 100, 3, 32, 0, 100);

        ImagePlus imp = new Duplicator().run(testImp1);
        Img<FloatType> img = ImageJFunctions.convertFloat(testImp1);

        ClearCLBuffer src = clijx.convert(imp, ClearCLBuffer.class);
        ClearCLBuffer dst = clijx.create(src.getDimensions(), src.getNativeType());

        clijx.copy(src, dst);
        ImagePlus copyFromCL = clijx.convert(dst, ImagePlus.class);
        assertTrue(TestUtilities.compareImages(testImp1, copyFromCL));

        RandomAccessibleInterval rai = clijx.convert(dst, RandomAccessibleInterval.class);
        assertTrue(TestUtilities.compareIterableIntervals(img, Views.iterable(rai)));

        src.close();
        dst.close();
        IJ.exit();
        clijx.clear();
    }

    @Test
    public void copyBuffer2d() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testImp2D1 = TestUtilities.getRandomImage(100, 100, 1, 32, 0, 100);

        ImagePlus imp = new Duplicator().run(testImp2D1);
        Img<FloatType> img = ImageJFunctions.convertFloat(testImp2D1);

        ClearCLBuffer src = clijx.convert(imp, ClearCLBuffer.class);
        ClearCLBuffer dst = clijx.create(src.getDimensions(), src.getNativeType());

        clijx.copy(src, dst);
        ImagePlus copyFromCL = clijx.convert(dst, ImagePlus.class);
        assertTrue(TestUtilities.compareImages(testImp2D1, copyFromCL));

        RandomAccessibleInterval rai = clijx.convert(dst, RandomAccessibleInterval.class);
        assertTrue(TestUtilities.compareIterableIntervals(img, Views.iterable(                 rai)));

        src.close();
        dst.close();
        IJ.exit();
        clijx.clear();
    }

}