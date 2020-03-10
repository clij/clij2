package net.haesleinhuepf.clij2.plugins;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.Duplicator;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.test.TestUtilities;
import net.haesleinhuepf.clijx.CLIJx;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MultiplyImageAndScalarTest {

    @Test
    public void multiplyScalar3d() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testImp1 = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);

        // do operation with ImageJ
        ImagePlus added = new Duplicator().run(testImp1);
        IJ.run(added, "Multiply...", "value=2 stack");

        // do operation with ClearCL
        ClearCLImage src = clijx.convert(testImp1, ClearCLImage.class);
        ClearCLImage dst = clijx.create(src);

        clijx.multiplyImageAndScalar(src, dst, 2f);
        ImagePlus addedFromCL = clijx.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(added, addedFromCL));

        src.close();
        dst.close();
        IJ.exit();
        clijx.clear();
    }


    @Test
    public void multiplyScalar3d_Buffer() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testImp1 = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);

        // do operation with ImageJ
        ImagePlus added = new Duplicator().run(testImp1);
        IJ.run(added, "Multiply...", "value=2 stack");

        // do operation with ClearCL
        ClearCLBuffer src = clijx.convert(testImp1, ClearCLBuffer.class);
        ClearCLBuffer dst = clijx.create(src);

        clijx.multiplyImageAndScalar(src, dst, 2f);
        ImagePlus addedFromCL = clijx.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(added, addedFromCL));

        src.close();
        dst.close();
        IJ.exit();
        clijx.close();
    }

    @Test
    public void multiplyScalar2d() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testImp2D1 = TestUtilities.getRandomImage(100, 100, 1, 32, 1, 100);

        // do operation with ImageJ
        ImagePlus added = new Duplicator().run(testImp2D1);
        IJ.run(added, "Multiply...", "value=2");

        // do operation with ClearCL
        ClearCLImage src = clijx.convert(testImp2D1, ClearCLImage.class);
        ClearCLImage dst = clijx.create(src);

        clijx.multiplyImageAndScalar(src, dst, 2f);
        ImagePlus addedFromCL = clijx.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(added, addedFromCL));

        src.close();
        dst.close();
        IJ.exit();
        clijx.clear();
    }

    @Test
    public void multiplyScalar2d_Buffers() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus testImp2D1 = TestUtilities.getRandomImage(100, 100, 1, 32, 1, 100);

        // do operation with ImageJ
        ImagePlus added = new Duplicator().run(testImp2D1);
        IJ.run(added, "Multiply...", "value=2");

        // do operation with ClearCL
        ClearCLBuffer src = clijx.convert(testImp2D1, ClearCLBuffer.class);
        ClearCLBuffer dst = clijx.create(src);

        clijx.multiplyImageAndScalar(src, dst, 2f);
        ImagePlus addedFromCL = clijx.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(added, addedFromCL));

        src.close();
        dst.close();
        IJ.exit();
        clijx.clear();
    }

    @Test
    public void multiplySliceBySliceWithScalars() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus mask3d = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);

        ClearCLImage maskCL = clijx.convert(mask3d, ClearCLImage.class);
        ClearCLImage multipliedBy2 = clijx.create(maskCL);

        float[] factors = new float[(int) maskCL.getDepth()];
        for (int i = 0; i < factors.length; i++) {
            factors[i] = 2;
        }
        clijx.multiplySliceBySliceWithScalars(maskCL, multipliedBy2, factors);

        assertEquals(clijx.sumPixels(maskCL) * 2, clijx.sumPixels(multipliedBy2), 0.001);

        multipliedBy2.close();
        maskCL.close();
        IJ.exit();
        clijx.clear();

    }

    @Test
    public void multiplySliceBySliceWithScalars_Buffer() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus mask3d = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);

        ClearCLBuffer maskCL = clijx.convert(mask3d, ClearCLBuffer.class);
        ClearCLBuffer multipliedBy2 = clijx.create(maskCL);

        float[] factors = new float[(int) maskCL.getDepth()];
        for (int i = 0; i < factors.length; i++) {
            factors[i] = 2;
        }
        clijx.multiplySliceBySliceWithScalars(maskCL, multipliedBy2, factors);

        assertEquals(clijx.sumPixels(maskCL) * 2, clijx.sumPixels(multipliedBy2), 0.001);

        multipliedBy2.close();
        maskCL.close();
        IJ.exit();
        clijx.clear();

    }

}