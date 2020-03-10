package net.haesleinhuepf.clij2.plugins;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.test.TestUtilities;
import net.haesleinhuepf.clijx.CLIJx;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class Downsample2DTest {
    @Test
    public void downsample2d() throws InterruptedException {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testImp2D1 = TestUtilities.getRandomImage(100, 100, 1, 32, 1, 100);

        // do operation with ImageJ
        new ImageJ(); // the menu command 'Scale...' can only be executed successfully if the ImageJ UI is visible; apparently
        testImp2D1.show();
        IJ.run(testImp2D1, "Scale...", "x=0.5 y=0.5 width=50 height=50 interpolation=None create");
        ImagePlus downsampled = IJ.getImage();

        // do operation with ClearCL
        ClearCLImage src = clijx.convert(testImp2D1, ClearCLImage.class);
        ClearCLImage dst = clijx.create(new long[]{src.getWidth() / 2, src.getHeight() / 2}, src.getChannelDataType());

        clijx.downsample( src, dst, 0.5f, 0.5f);

        ImagePlus downsampledCL = clijx.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(downsampled, downsampledCL, 1.0));

        IJ.exit();
        clijx.clear();
    }


    @Test
    public void downsample2d_Buffers() throws InterruptedException {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testImp2D1 = TestUtilities.getRandomImage(100, 100, 1, 32, 1, 100);

        // do operation with ImageJ
        new ImageJ(); // the menu command 'Scale...' can only be executed successfully if the ImageJ UI is visible; apparently
        testImp2D1.show();
        IJ.run(testImp2D1, "Scale...", "x=0.5 y=0.5 width=50 height=50 interpolation=None create");
        ImagePlus downsampled = IJ.getImage();

        // do operation with ClearCL
        ClearCLBuffer src = clijx.convert(testImp2D1, ClearCLBuffer.class);
        ClearCLBuffer dst = clijx.create(new long[]{src.getWidth() / 2, src.getHeight() / 2}, src.getNativeType());

        clijx.downsample(src, dst, 0.5f, 0.5f);

        ImagePlus downsampledCL = clijx.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(downsampled, downsampledCL, 1.0));

        IJ.exit();
        clijx.clear();
    }

}