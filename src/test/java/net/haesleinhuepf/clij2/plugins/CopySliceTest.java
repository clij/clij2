package net.haesleinhuepf.clij2.plugins;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.Duplicator;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.test.TestUtilities;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class CopySliceTest {

    @Test
    public void copySlice() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp1 = TestUtilities.getRandomImage(100, 100, 3, 32, 0, 100);

        // do operation with ImageJ
        ImagePlus copy = new Duplicator().run(testImp1, 3, 3);

        // do operation with ClearCL
        ClearCLImage src = clij.convert(testImp1, ClearCLImage.class);
        ClearCLImage
                dst =
                clij.createCLImage(new long[]{src.getWidth(),
                                src.getHeight()},
                        src.getChannelDataType());

        Kernels.copySlice(clij, src, dst, 2);
        ImagePlus copyFromCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(copy, copyFromCL));

        // also test if putSliceInStack works
        Kernels.copySlice(clij, dst, src, 2);

        src.close();
        dst.close();
        IJ.exit();
        clij.close();
    }

    @Test
    public void copySliceBuffer() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp1 = TestUtilities.getRandomImage(100, 100, 3, 32, 0, 100);

        // do operation with ImageJ
        ImagePlus copy = new Duplicator().run(testImp1, 3, 3);

        // do operation with ClearCL
        ClearCLBuffer src = clij.convert(testImp1, ClearCLBuffer.class);
        ClearCLBuffer
                dst =
                clij.createCLBuffer(new long[]{src.getWidth(),
                                src.getHeight()},
                        src.getNativeType());

        Kernels.copySlice(clij, src, dst, 2);
        ImagePlus copyFromCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(copy, copyFromCL));

        src.close();
        dst.close();
        IJ.exit();
        clij.close();
    }

}