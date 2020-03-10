package net.haesleinhuepf.clij2.plugins;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.Duplicator;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.test.TestUtilities;
import net.haesleinhuepf.clijx.CLIJx;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class GaussianBlur2DTest {
    @Test
    public void blur2d() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testFlyBrain3D = IJ.openImage("src/test/resources/flybrain.tif");
        ImagePlus testFlyBrain2D = new Duplicator().run(testFlyBrain3D, 1, 1);

        // do operation with ImageJ
        ImagePlus gauss = new Duplicator().run(testFlyBrain2D);
        ImagePlus gaussCopy = new Duplicator().run(testFlyBrain2D);
        IJ.run(gauss, "Gaussian Blur...", "sigma=2");

        // do operation with ClearCL
        ClearCLImage src = clijx.convert(gaussCopy, ClearCLImage.class);
        ClearCLImage dst = clijx.create(src);

        clijx.blur(src, dst, 2f, 2f);
        ImagePlus gaussFromCL = clijx.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(gauss, gaussFromCL, 2));

        src.close();
        dst.close();
        IJ.exit();
        clijx.clear();
    }

    @Test
    public void blur2d_Buffers() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testFlyBrain3D = IJ.openImage("src/test/resources/flybrain.tif");
        ImagePlus testFlyBrain2D = new Duplicator().run(testFlyBrain3D, 1, 1);

        // do operation with ImageJ
        ImagePlus gauss = new Duplicator().run(testFlyBrain2D);
        ImagePlus gaussCopy = new Duplicator().run(testFlyBrain2D);
        IJ.run(gauss, "Gaussian Blur...", "sigma=2");

        // do operation with ClearCL
        ClearCLBuffer src = clijx.convert(gaussCopy, ClearCLBuffer.class);
        ClearCLBuffer dst = clijx.create(src);

        clijx.blur(src, dst, 2f, 2f);
        ImagePlus gaussFromCL = clijx.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(gauss, gaussFromCL, 2));

        src.close();
        dst.close();
        IJ.exit();
        clijx.clear();
    }

}