package net.haesleinhuepf.clij2.plugins;

import ij.IJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.test.TestUtilities;
import net.haesleinhuepf.clijx.CLIJx;
import org.junit.Test;

public class MultiplyStackWithPlaneTest {
    @Test
    public void multiplyStackWithPlane() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testImp1 = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);
        ImagePlus testImp2 = TestUtilities.getRandomImage(100, 100, 1, 32, 1, 100);

        // do operation with ImageJ

        // do operation with ClearCL
        ClearCLImage src = clijx.convert(testImp1, ClearCLImage.class);
        ClearCLImage mask = clijx.convert(testImp2, ClearCLImage.class);;
        ClearCLImage dst = clijx.create(src);

        clijx.multiplyStackWithPlane(src, mask, dst);

        mask.close();
        dst.close();

        IJ.exit();
        clijx.clear();
    }

    @Test
    public void multiplyStackWithPlane_Buffers() {

        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testImp1 = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);
        ImagePlus testImp2 = TestUtilities.getRandomImage(100, 100, 1, 32, 1, 100);

        // do operation with ImageJ

        // do operation with ClearCL
        ClearCLBuffer src = clijx.convert(testImp1, ClearCLBuffer.class);
        ClearCLBuffer mask = clijx.convert(testImp2, ClearCLBuffer.class);;
        ClearCLBuffer dst = clijx.create(src);

        clijx.multiplyStackWithPlane(src, mask, dst);

        mask.close();
        dst.close();

        IJ.exit();
        clijx.clear();
    }

}