package net.haesleinhuepf.clij2.plugins;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.Roi;
import ij.plugin.Duplicator;
import ij.plugin.GaussianBlur3D;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.test.TestUtilities;
import net.haesleinhuepf.clijx.CLIJx;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class GaussianBlur3DTest {
    private final static double relativeTolerance = 0.05;

    @Test
    public void blur3d() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testImp1 = IJ.openImage("src/test/resources/flybrain.tif");

        // do operation with ImageJ
        ImagePlus gauss = new Duplicator().run(testImp1);
        GaussianBlur3D.blur(gauss, 2, 2, 2);

        // do operation with ClearCL
        ClearCLImage src = clijx.convert(testImp1, ClearCLImage.class);
        ClearCLImage dst = clijx.create(src);

        clijx.blur(src, dst, 2f, 2f, 2f);
        ImagePlus gaussFromCL = clijx.convert(dst, ImagePlus.class);

        double tolerance = relativeTolerance * clijx.maximumOfAllPixels(dst);

        assertTrue(TestUtilities.compareImages(gauss, gaussFromCL, tolerance));

        src.close();
        dst.close();
        IJ.exit();
        clijx.clear();
    }

    @Test
    public void blur3d_Buffers() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testImp1 = IJ.openImage("src/test/resources/flybrain.tif");
        IJ.run(testImp1, "32-bit","");

        // do operation with ImageJ
        ImagePlus gauss = new Duplicator().run(testImp1);
        GaussianBlur3D.blur(gauss, 2, 2, 2);

        // do operation with ClearCL
        ClearCLBuffer src = clijx.convert(testImp1, ClearCLBuffer.class);
        ClearCLBuffer dst = clijx.create(src);

        clijx.blur(src, dst, 2f, 2f, 2f);
        ImagePlus gaussFromCL = clijx.convert(dst, ImagePlus.class);

        // ignore borders
        int bordersize = 5;
        gauss.setRoi(new Roi(bordersize, bordersize, gauss.getWidth() - bordersize * 2, gauss.getHeight() - bordersize * 2));
        gaussFromCL.setRoi(new Roi(bordersize, bordersize, gaussFromCL.getWidth() - bordersize * 2, gaussFromCL.getHeight() - bordersize * 2));
        gauss = new Duplicator().run(gauss, bordersize, gauss.getNSlices() - bordersize);
        gaussFromCL = new Duplicator().run(gaussFromCL, bordersize, gaussFromCL.getNSlices() - bordersize);

        double tolerance = relativeTolerance * clijx.maximumOfAllPixels(dst);

        assertTrue(TestUtilities.compareImages(gauss, gaussFromCL, tolerance));

        src.close();
        dst.close();
        IJ.exit();
        clijx.clear();
    }

}