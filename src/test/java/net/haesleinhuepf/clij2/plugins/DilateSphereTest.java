package net.haesleinhuepf.clij2.plugins;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.NewImage;
import ij.process.ImageProcessor;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clijx.CLIJx;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DilateSphereTest {

    @Test
    public void dilate3d() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testImp2 = NewImage.createImage("", 100, 100, 12, 16, NewImage.FILL_BLACK);

        for (int z = 0; z < 5; z++) {
            testImp2.setZ(z + 1);
            ImageProcessor ip2 = testImp2.getProcessor();
            ip2.set(7, 5, 1);
            ip2.set(6, 6, 1);
            ip2.set(5, 7, 1);
        }
        ClearCLImage maskCL = clijx.convert(testImp2, ClearCLImage.class);
        ClearCLImage maskCLafter = clijx.create(maskCL);

        clijx.dilateSphere(maskCL, maskCLafter);

        double sum = clijx.sumPixels(maskCLafter);

        assertEquals(58, sum, 0);

        maskCL.close();
        maskCLafter.close();
        IJ.exit();
        clijx.clear();
    }

    @Test
    public void dilate3d_Buffers() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testImp2 = NewImage.createImage("", 100, 100, 12, 16, NewImage.FILL_BLACK);

        for (int z = 0; z < 5; z++) {
            testImp2.setZ(z + 1);
            ImageProcessor ip2 = testImp2.getProcessor();
            ip2.set(7, 5, 1);
            ip2.set(6, 6, 1);
            ip2.set(5, 7, 1);
        }

        ClearCLBuffer maskCL = clijx.convert(testImp2, ClearCLBuffer.class);
        ClearCLBuffer maskCLafter = clijx.create(maskCL);

        clijx.dilateSphere(maskCL, maskCLafter);

        double sum = clijx.sumPixels(maskCLafter);

        assertEquals(58, sum, 0);

        maskCL.close();
        maskCLafter.close();
        IJ.exit();
        clijx.clear();
    }

    @Test
    public void dilate2d() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testImp2 = NewImage.createImage("", 100, 100, 12, 16, NewImage.FILL_BLACK);

        ImageProcessor ip2 = testImp2.getProcessor();
        ip2.set(7, 5, 1);
        ip2.set(6, 6, 1);
        ip2.set(5, 7, 1);

        ClearCLImage maskCL = clijx.convert(testImp2, ClearCLImage.class);;
        ClearCLImage maskCLafter = clijx.create(maskCL);

        clijx.dilateSphere(maskCL, maskCLafter);

        double sum = clijx.sumPixels(maskCLafter);

        assertEquals(14, sum, 0);

        maskCL.close();
        maskCLafter.close();
        IJ.exit();
        clijx.clear();
    }

    @Test
    public void dilate2d_Buffers() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testImp2 = NewImage.createImage("", 100, 100, 12, 16, NewImage.FILL_BLACK);

        ImageProcessor ip2 = testImp2.getProcessor();
        ip2.set(7, 5, 1);
        ip2.set(6, 6, 1);
        ip2.set(5, 7, 1);

        ClearCLBuffer maskCL = clijx.convert(testImp2, ClearCLBuffer.class);;
        ClearCLBuffer maskCLafter = clijx.create(maskCL);

        clijx.dilateSphere(maskCL, maskCLafter);

        double sum = clijx.sumPixels(maskCLafter);

        assertEquals(14, sum, 0);

        maskCL.close();
        maskCLafter.close();
        IJ.exit();
        clijx.clear();
    }

}