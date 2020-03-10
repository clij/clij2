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

public class RotateCounterClockwiseTest {

    @Test
    public void rotateLeft2d() throws InterruptedException {

        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testFlyBrain3D = IJ.openImage("src/test/resources/flybrain.tif");

        testFlyBrain3D.setRoi(0, 0, 256, 128);
        ImagePlus testImage = new Duplicator().run(testFlyBrain3D, 10, 10);
        ImagePlus testImage2 = new Duplicator().run(testFlyBrain3D, 10, 10);
        testImage.show();

        // do operation with ImageJ
        new ImageJ();
        IJ.run(testImage, "Rotate 90 Degrees Left", "");
        ImagePlus reference = IJ.getImage();

        // do operation with OpenCL
        ClearCLImage inputCL = clijx.convert(testImage2, ClearCLImage.class);
        ClearCLImage outputCL = clijx.create(new long[]{inputCL.getHeight(), inputCL.getWidth()}, inputCL.getChannelDataType());

        clijx.rotateLeft(inputCL, outputCL);

        ImagePlus result = clijx.convert(outputCL, ImagePlus.class);

        //clij.show(reference, "ref");
        //clij.show(result, "res");
        //new WaitForUserDialog("wait").show();

        assertTrue(TestUtilities.compareImages(reference, result));

        IJ.exit();
        clijx.clear();
    }

    @Test
    public void rotateLeft2d_Buffers() throws InterruptedException {

        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testFlyBrain3D = IJ.openImage("src/test/resources/flybrain.tif");

        testFlyBrain3D.setRoi(0, 0, 256, 128);
        ImagePlus testImage = new Duplicator().run(testFlyBrain3D, 10, 10);
        ImagePlus testImage2 = new Duplicator().run(testFlyBrain3D, 10, 10);
        testImage.show();

        // do operation with ImageJ
        new ImageJ();
        IJ.run(testImage, "Rotate 90 Degrees Left", "");
        ImagePlus reference = IJ.getImage();

        // do operation with OpenCL
        ClearCLBuffer inputCL = clijx.convert(testImage2, ClearCLBuffer.class);
        ClearCLBuffer outputCL = clijx.create(new long[]{inputCL.getHeight(), inputCL.getWidth()}, inputCL.getNativeType());

        clijx.rotateLeft(inputCL, outputCL);

        ImagePlus result = clijx.convert(outputCL, ImagePlus.class);

        //clij.show(reference, "ref");
        //clij.show(result, "res");
        //new WaitForUserDialog("wait").show();

        assertTrue(TestUtilities.compareImages(reference, result));

        IJ.exit();
        clijx.clear();
    }

    @Test
    public void rotateLeft3d() throws InterruptedException {

        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testFlyBrain3D = IJ.openImage("src/test/resources/flybrain.tif");

        testFlyBrain3D.setRoi(0, 0, 256, 128);
        ImagePlus testImage = new Duplicator().run(testFlyBrain3D);
        ImagePlus testImage2 = new Duplicator().run(testFlyBrain3D);
        testImage.show();

        // do operation with ImageJ
        new ImageJ();
        IJ.run(testImage, "Rotate 90 Degrees Left", "");
        ImagePlus reference = IJ.getImage();

        // do operation with OpenCL
        ClearCLImage inputCL = clijx.convert(testImage2, ClearCLImage.class);
        ClearCLImage outputCL = clijx.create(new long[]{inputCL.getHeight(), inputCL.getWidth(), inputCL.getDepth()}, inputCL.getChannelDataType());

        clijx.rotateLeft(inputCL, outputCL);

        ImagePlus result = clijx.convert(outputCL, ImagePlus.class);

        //clij.show(reference, "ref");
        //clij.show(result, "res");
        //new WaitForUserDialog("wait").show();

        assertTrue(TestUtilities.compareImages(reference, result));

        IJ.exit();
        clijx.clear();
    }

    @Test
    public void rotateLeft3d_Buffers() throws InterruptedException {

        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testFlyBrain3D = IJ.openImage("src/test/resources/flybrain.tif");

        testFlyBrain3D.setRoi(0, 0, 256, 128);
        ImagePlus testImage = new Duplicator().run(testFlyBrain3D);
        ImagePlus testImage2 = new Duplicator().run(testFlyBrain3D);
        testImage.show();

        // do operation with ImageJ
        new ImageJ();
        IJ.run(testImage, "Rotate 90 Degrees Left", "");
        ImagePlus reference = IJ.getImage();

        // do operation with OpenCL
        ClearCLBuffer inputCL = clijx.convert(testImage2, ClearCLBuffer.class);
        ClearCLBuffer outputCL = clijx.create(new long[]{inputCL.getHeight(), inputCL.getWidth(), inputCL.getDepth()}, inputCL.getNativeType());

        clijx.rotateLeft(inputCL, outputCL);

        ImagePlus result = clijx.convert(outputCL, ImagePlus.class);

        //clij.show(reference, "ref");
        //clij.show(result, "res");
        //new WaitForUserDialog("wait").show();

        assertTrue(TestUtilities.compareImages(reference, result));

        IJ.exit();
        clijx.clear();
    }

}