package net.haesleinhuepf.clij2.plugins;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.plugin.Duplicator;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.test.TestUtilities;
import net.haesleinhuepf.clijx.CLIJx;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class Downsample3DTest {

    @Test
    public void downsample3d() throws InterruptedException {

        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testImp1 = IJ.openImage("src/test/resources/flybrain.tif");
        if (testImp1.getNSlices() % 2 == 1) {
            testImp1 = new Duplicator().run(testImp1, 1, testImp1.getNSlices() - 1);
        }
        // do operation with ImageJ
        new ImageJ(); // the menu command 'Scale...' can only be executed successfully if the ImageJ UI is visible; apparently
        testImp1.show();
        IJ.run(testImp1, "Scale...", "x=0.5 y=0.5 z=0.5 interpolation=None process create");
        ImagePlus downsampled = IJ.getImage();


        // do operation with ClearCL
        ClearCLImage src = clijx.convert(testImp1, ClearCLImage.class);
        ClearCLImage dst = clijx.create(new long[]{src.getWidth() / 2, src.getHeight() / 2, (long) (src.getDepth()) / 2}, src.getChannelDataType());


        clijx.downsample(src, dst, 0.5f, 0.5f, 0.5f);

        ImagePlus downsampledCL = clijx.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(downsampled, downsampledCL, 1.0));
        IJ.exit();
        clijx.clear();
    }

    @Test
    public void downsample3d_Buffers() throws InterruptedException {

        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testImp1 = IJ.openImage("src/test/resources/flybrain.tif");
        if (testImp1.getNSlices() % 2 == 1) {
            testImp1 = new Duplicator().run(testImp1, 1, testImp1.getNSlices() - 1);
        }

        // do operation with ImageJ
        new ImageJ(); // the menu command 'Scale...' can only be executed successfully if the ImageJ UI is visible; apparently
        testImp1.show();
        IJ.run(testImp1, "Scale...", "x=0.5 y=0.5 z=0.5 interpolation=None process create");
        //Thread.sleep(1000);
        ImagePlus downsampled = IJ.getImage();


        // do operation with ClearCL
        ClearCLBuffer src = clijx.convert(testImp1, ClearCLBuffer.class);
        ClearCLBuffer dst = clijx.create(new long[]{src.getWidth() / 2, src.getHeight() / 2, (long) (src.getDepth()) / 2}, src.getNativeType());

        clijx.downsample(src, dst, 0.5f, 0.5f, 0.5f);

        ImagePlus downsampledCL = clijx.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(downsampled, downsampledCL, 1.0));
        IJ.exit();
        clijx.clear();
    }
}