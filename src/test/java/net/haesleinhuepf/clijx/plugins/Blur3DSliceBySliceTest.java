package net.haesleinhuepf.clijx.plugins;

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

public class Blur3DSliceBySliceTest {

    @Test
    public void blurSliceBySlice() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testFlyBrain3D = IJ.openImage("src/test/resources/flybrain.tif");

        // do operation with ImageJ
        ImagePlus gauss = new Duplicator().run(testFlyBrain3D);
        IJ.run(gauss, "Gaussian Blur...", "sigma=2 stack");

        // do operation with ClearCL
        ClearCLImage src = clijx.convert(testFlyBrain3D, ClearCLImage.class);;
        ClearCLImage dst = clijx.create(src);

        clijx.blurSliceBySlice(src, dst, 15, 15, 2f, 2f);
        ImagePlus gaussFromCL = clijx.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(gauss, gaussFromCL, 2));

        src.close();
        dst.close();
        IJ.exit();
        clijx.clear();
    }

    @Test public void blurSliceBySlice_Buffers()
    {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testFlyBrain3D = IJ.openImage("src/test/resources/flybrain.tif");

        // do operation with ImageJ
        ImagePlus gauss = new Duplicator().run(testFlyBrain3D);
        IJ.run(gauss, "Gaussian Blur...", "sigma=2 stack");

        // do operation with ClearCL
        ClearCLBuffer src = clijx.convert(testFlyBrain3D, ClearCLBuffer.class);
        ClearCLBuffer dst = clijx.create(src);

        clijx.blurSliceBySlice(src, dst, 15, 15, 2, 2);
        ImagePlus gaussFromCL = clijx.convert(dst, ImagePlus.class);

        // ignore borders
        gauss.setRoi(new Roi(2, 2, gauss.getWidth() - 4, gauss.getHeight() - 4));
        gaussFromCL.setRoi(new Roi(2, 2, gaussFromCL.getWidth() - 4, gaussFromCL.getHeight() - 4));
        gauss = new Duplicator().run(gauss);
        gaussFromCL = new Duplicator().run(gaussFromCL);

        assertTrue(TestUtilities.compareImages(gauss, gaussFromCL, 2));

        src.close();
        dst.close();
        IJ.exit();
        clijx.clear();
    }

}