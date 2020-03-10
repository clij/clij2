package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import ij.IJ;
import ij.ImagePlus;
import ij.gui.Roi;
import ij.plugin.Duplicator;
import net.haesleinhuepf.clij.test.TestUtilities;
import net.haesleinhuepf.clijx.CLIJx;
import org.junit.Test;

import static org.junit.Assert.*;

public class Maximum2DSphereTest {
    //@Ignore // ignore test as we know and need to accept that maximumSphere does not do the same as ImageJ
    @Test
    public void maximum2d() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testFlyBrain3D = IJ.openImage("src/test/resources/flybrain.tif");

        ImagePlus testImage = new Duplicator().run(testFlyBrain3D, 20, 20);
        IJ.run(testImage, "32-bit", "");

        // do operation with ImageJ
        ImagePlus reference = new Duplicator().run(testImage);
        IJ.run(reference, "Maximum...", "radius=1");

        // do operation with CLIJ
        ClearCLImage inputCL = clijx.convert(testImage, ClearCLImage.class);
        ClearCLImage outputCL = clijx.create(inputCL);

        clijx.maximum2DSphere(inputCL, outputCL, 1, 1);

        ImagePlus result = clijx.convert(outputCL, ImagePlus.class);

        //new ImageJ();
        //clij.show(inputCL, "inp");
        //clij.show(reference, "ref");
        //clij.show(result, "res");
        //new WaitForUserDialog("wait").show();
        assertTrue(TestUtilities.compareImages(reference, result, 0.001));
        IJ.exit();
        clijx.clear();
    }

    //@Ignore // ignore test as we know and need to accept that maximumSphere does not do the same as ImageJ
    @Test
    public void maximum2d_Buffers() {
        CLIJx clijx = CLIJx.getInstance();

        ImagePlus testFlyBrain3D = IJ.openImage("src/test/resources/flybrain.tif");

        ImagePlus testImage = new Duplicator().run(testFlyBrain3D, 20, 20);
        IJ.run(testImage, "32-bit", "");

        // do operation with ImageJ
        ImagePlus reference = new Duplicator().run(testImage);
        IJ.run(reference, "Maximum...", "radius=1");

        // do operation with CLIJ
        ClearCLBuffer inputCL = clijx.convert(testImage, ClearCLBuffer.class);
        ClearCLBuffer outputCL = clijx.create(inputCL);

        clijx.maximum2DSphere(inputCL, outputCL, 1, 1);

        ImagePlus result = clijx.convert(outputCL, ImagePlus.class);

        // ignore edges
        reference.setRoi(new Roi(1, 1, reference.getWidth() - 2, reference.getHeight() - 2));
        result.setRoi(new Roi(1, 1, reference.getWidth() - 2, reference.getHeight() - 2));
        reference = new Duplicator().run(reference);
        result = new Duplicator().run(result);

        //new ImageJ();
        //clij.show(inputCL, "inp");
        //clij.show(reference, "ref");
        //clij.show(result, "res");
        //new WaitForUserDialog("wait").show();
        assertTrue(TestUtilities.compareImages(reference, result, 0.001));
        IJ.exit();
        clijx.clear();
    }


}