package net.haesleinhuepf.clij2.plugins;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.NewImage;
import ij.process.ImageProcessor;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.AbstractMacroPluginTest;
import net.haesleinhuepf.clijx.CLIJx;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DilateBoxTest extends AbstractMacroPluginTest {

    @Test
    public void test_dilateBox_3d() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus mask3d = NewImage.createByteImage("Test", 10, 10, 10, NewImage.FILL_BLACK);
        mask3d.setZ(5);
        ImageProcessor ip = mask3d.getProcessor();
        ip.set(5,5, 1);

        ClearCLBuffer maskCL = clijx.convert(mask3d, ClearCLBuffer.class);
        double sum = clijx.sumPixels(maskCL);
        assertEquals(1, sum, 0.0);

        ClearCLBuffer maskCL2 = clijx.create(maskCL);
        clijx.dilateBox(maskCL, maskCL2);


        double sum2 = clijx.sumPixels(maskCL2);

        assertEquals(27, sum2, 0.0);

        ClearCLBuffer maskCL3 = clijx.create(maskCL);
        clijx.dilateBox(maskCL2, maskCL3);
        double sum3 = clijx.sumPixels(maskCL3);
        assertEquals(125, sum3, 0.0);

        maskCL.close();
        maskCL2.close();
        maskCL3.close();
        IJ.exit();
        clijx.clear();
    }
}