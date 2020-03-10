package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import ij.IJ;
import ij.ImagePlus;
import ij.plugin.Duplicator;
import net.haesleinhuepf.clij.test.TestUtilities;
import net.haesleinhuepf.clijx.CLIJx;
import org.junit.Test;

import static org.junit.Assert.*;

public class Minimum2DSphereTest {
   // @Ignore //ignore test as we know and need to accept that the tested method does not do the same its ImageJ counterpart
    @Test
    public void minimum2d() {
        CLIJx clijx = CLIJx.getInstance();

        ImagePlus testFlyBrain3D = IJ.openImage("src/test/resources/flybrain.tif");

        ImagePlus testImage = new Duplicator().run(testFlyBrain3D, 20, 20);
        IJ.run(testImage, "32-bit", "");

        // do operation with ImageJ
        ImagePlus reference = new Duplicator().run(testImage);
        IJ.run(reference, "Minimum...", "radius=1");

        // do operation with CLIJ
        ClearCLImage inputCL = clijx.convert(testImage, ClearCLImage.class);
        ClearCLImage outputCL = clijx.create(inputCL);

        clijx.minimum2DSphere(inputCL, outputCL, 1, 1);

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

   // @Ignore //ignore test as we know and need to accept that the tested method does not do the same its ImageJ counterpart
    @Test
    public void minimum2d_Buffers() {
        CLIJx clijx = CLIJx.getInstance();

        ImagePlus testFlyBrain3D = IJ.openImage("src/test/resources/flybrain.tif");

        ImagePlus testImage = new Duplicator().run(testFlyBrain3D, 20, 20);
        IJ.run(testImage, "32-bit", "");

        // do operation with ImageJ
        ImagePlus reference = new Duplicator().run(testImage);
        IJ.run(reference, "Minimum...", "radius=1");

        // do operation with CLIJ
        ClearCLBuffer inputCL = clijx.convert(testImage, ClearCLBuffer.class);
        ClearCLBuffer outputCL = clijx.create(inputCL);

        clijx.minimum2DSphere( inputCL, outputCL, 1, 1);

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

}