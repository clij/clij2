package net.haesleinhuepf.clij2.plugins;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.Roi;
import ij.plugin.Duplicator;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.test.TestUtilities;
import net.haesleinhuepf.clijx.CLIJx;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class Crop2DTest {

    @Test
    public void crop2d() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testImp2D1 = TestUtilities.getRandomImage(100, 100, 1, 32, 0, 100);

        // do operation with ImageJ
        Roi roi = new Roi(2, 2, 10, 10);
        testImp2D1.setRoi(roi);
        ImagePlus crop = new Duplicator().run(testImp2D1);

        // do operation with ClearCL
        ClearCLImage src = clijx.convert(testImp2D1, ClearCLImage.class);
        ClearCLImage dst = clijx.create(new long[]{10, 10}, src.getChannelDataType());

        clijx.crop(src, dst, 2, 2);
        ImagePlus cropFromCL = clijx.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(crop, cropFromCL));

        src.close();
        dst.close();
        IJ.exit();
        clijx.clear();
    }

    @Test
    public void cropBuffer2d() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testImp2D1 = TestUtilities.getRandomImage(100, 100, 1, 32, 0, 100);

        // do operation with ImageJ
        Roi roi = new Roi(2, 2, 10, 10);
        testImp2D1.setRoi(roi);
        ImagePlus crop = new Duplicator().run(testImp2D1);

        // do operation with ClearCL
        ClearCLBuffer src = clijx.convert(testImp2D1, ClearCLBuffer.class);
        ClearCLBuffer dst = clijx.create(new long[]{10, 10}, src.getNativeType());

        clijx.crop(src, dst, 2, 2);
        ImagePlus cropFromCL = clijx.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(crop, cropFromCL));

        src.close();
        dst.close();
        IJ.exit();
        clijx.clear();
    }

}