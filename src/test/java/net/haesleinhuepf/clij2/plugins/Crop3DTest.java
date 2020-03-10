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

public class Crop3DTest {

    @Test
    public void crop3d() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testImp1 = TestUtilities.getRandomImage(100, 100, 20, 32, 0, 100);

        // do operation with ImageJ
        Roi roi = new Roi(2, 2, 10, 10);
        testImp1.setRoi(roi);
        ImagePlus crop = new Duplicator().run(testImp1, 3, 12);

        // do operation with ClearCL
        ClearCLImage src = clijx.convert(testImp1, ClearCLImage.class);
        ClearCLImage dst = clijx.create(new long[]{10, 10, 10}, src.getChannelDataType());

        clijx.crop(src, dst, 2, 2, 2);
        ImagePlus cropFromCL = clijx.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(crop, cropFromCL));

        src.close();
        dst.close();
        IJ.exit();
        clijx.clear();
    }


    @Test
    public void cropBuffer3d() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testImp1 = TestUtilities.getRandomImage(100, 100, 20, 32, 0, 100);

        // do operation with ImageJ
        Roi roi = new Roi(2, 2, 10, 10);
        testImp1.setRoi(roi);
        ImagePlus crop = new Duplicator().run(testImp1, 3, 12);

        // do operation with ClearCL
        ClearCLBuffer src = clijx.convert(testImp1, ClearCLBuffer.class);
        ClearCLBuffer dst = clijx.create(new long[]{10, 10, 10}, src.getNativeType());

        clijx.crop(src, dst, 2, 2, 2);
        ImagePlus cropFromCL = clijx.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(crop, cropFromCL));

        src.close();
        dst.close();
        IJ.exit();
        clijx.clear();
    }

}