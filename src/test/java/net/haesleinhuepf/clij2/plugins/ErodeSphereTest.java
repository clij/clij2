package net.haesleinhuepf.clij2.plugins;

import ij.IJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clijx.CLIJx;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.display.imagej.ImageJFunctions;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ErodeSphereTest {

    @Test
    public void erode3d() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus mask3d = ImageJFunctions.wrap(ArrayImgs.bytes(new byte[]{

                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,

                0, 0, 0, 0, 0,
                0, 1, 1, 1, 0,
                0, 1, 1, 1, 0,
                0, 1, 1, 1, 0,
                0, 0, 0, 0, 0,

                0, 0, 0, 0, 0,
                0, 1, 1, 1, 0,
                0, 1, 1, 1, 0,
                0, 1, 1, 1, 0,
                0, 0, 0, 0, 0,

                0, 0, 0, 0, 0,
                0, 1, 1, 1, 0,
                0, 1, 1, 1, 0,
                0, 1, 1, 1, 0,
                0, 0, 0, 0, 0,

                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0
        }, new long[]{5, 5, 5}), "");


        ClearCLImage maskCL = clijx.convert(mask3d, ClearCLImage.class);
        ClearCLImage maskCLafter = clijx.create(maskCL);

        clijx.erodeSphere(maskCL, maskCLafter);

        double sum = clijx.sumPixels(maskCLafter);
        System.out.println(sum);

        assertTrue(sum == 1);

        maskCL.close();
        maskCLafter.close();
        IJ.exit();
        clijx.clear();
    }

    @Test
    public void erode3d_Buffer() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus mask3d = ImageJFunctions.wrap(ArrayImgs.bytes(new byte[]{

                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,

                0, 0, 0, 0, 0,
                0, 1, 1, 1, 0,
                0, 1, 1, 1, 0,
                0, 1, 1, 1, 0,
                0, 0, 0, 0, 0,

                0, 0, 0, 0, 0,
                0, 1, 1, 1, 0,
                0, 1, 1, 1, 0,
                0, 1, 1, 1, 0,
                0, 0, 0, 0, 0,

                0, 0, 0, 0, 0,
                0, 1, 1, 1, 0,
                0, 1, 1, 1, 0,
                0, 1, 1, 1, 0,
                0, 0, 0, 0, 0,

                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0
        }, new long[]{5, 5, 5}), "");

        ClearCLBuffer maskCL = clijx.convert(mask3d, ClearCLBuffer.class);
        ClearCLBuffer maskCLafter = clijx.create(maskCL);

        clijx.erodeSphere(maskCL, maskCLafter);
        //new ImageJ();
        //clij.show(maskCLafter,"mask");
        //new WaitForUserDialog("wait").show();

        double sum = clijx.sumPixels(maskCLafter);

        assertTrue(sum == 1);

        maskCL.close();
        maskCLafter.close();
        IJ.exit();
        clijx.clear();
    }

    @Test
    public void erode2d() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testImp2 = ImageJFunctions.wrap(ArrayImgs.bytes(new byte[]{
                0, 0, 0, 0, 0,
                0, 1, 1, 1, 0,
                0, 1, 1, 1, 0,
                0, 1, 1, 1, 0,
                0, 0, 0, 0, 0
        }, new long[]{5, 5, 1}), "");

        ClearCLImage maskCL = clijx.convert(testImp2, ClearCLImage.class);;
        ClearCLImage
                maskCLafter = clijx.create(maskCL);

        clijx.erodeSphere(maskCL, maskCLafter);

        double sum = clijx.sumPixels(maskCLafter);

        assertTrue(sum == 1);

        maskCL.close();
        maskCLafter.close();
        IJ.exit();
        clijx.clear();
    }

    @Test
    public void erode2d_Buffers() {

        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testImp2 = ImageJFunctions.wrap(ArrayImgs.bytes(new byte[]{
                0, 0, 0, 0, 0,
                0, 1, 1, 1, 0,
                0, 1, 1, 1, 0,
                0, 1, 1, 1, 0,
                0, 0, 0, 0, 0
        }, new long[]{5, 5, 1}), "");

        ClearCLBuffer maskCL = clijx.convert(testImp2, ClearCLBuffer.class);;
        ClearCLBuffer
                maskCLafter = clijx.create(maskCL);

        clijx.erodeSphere(maskCL, maskCLafter);

        double sum = clijx.sumPixels(maskCLafter);

        assertTrue(sum == 1);

        maskCL.close();
        maskCLafter.close();
        IJ.exit();
        clijx.clear();
    }


}