package net.haesleinhuepf.clij.advancedfilters;

import ij.IJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * PasteCropTest
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 06 2019
 */
public class PasteCropTest {
    @Test
    public void pasteCropTest3D() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus imp = IJ.openImage("src/test/resources/t1-head.tif");

        ClearCLBuffer input = clij.push(imp);
        ClearCLBuffer intermediate = clij.create(new long[]{1000, 1000, 200}, input.getNativeType());
        ClearCLBuffer output = clij.createCLBuffer(input);
        Paste3D.paste(clij, input, intermediate, 100, 100, 0);

        clij.op().crop(intermediate, output, 100, 100, 0);

        double mse = MeanSquaredError.mse(clij, input, output);
        double sumIn = clij.op().sumPixels(input);
        double sumOut = clij.op().sumPixels(output);

        System.out.println(sumIn);
        assertEquals(mse, 0.0, 0.0);
        assertEquals(sumIn, sumOut, 0.0);

        input.close();
        intermediate.close();
        output.close();
    }

    @Test
    public void pasteCropTest2D() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus imp = IJ.openImage("src/test/resources/blobs.tif");

        ClearCLBuffer input = clij.push(imp);
        ClearCLBuffer intermediate = clij.create(new long[]{1000, 1000, 1}, input.getNativeType());
        ClearCLBuffer output = clij.createCLBuffer(input);
        Paste2D.paste(clij, input, intermediate, 100, 100);

        clij.op().crop(intermediate, output, 100, 100, 0);

        double mse = MeanSquaredError.mse(clij, input, output);
        double sumIn = clij.op().sumPixels(input);
        double sumOut = clij.op().sumPixels(output);

        System.out.println(sumIn);
        assertEquals(mse, 0.0, 0.0);
        assertEquals(sumIn, sumOut, 0.0);

        input.close();
        intermediate.close();
        output.close();
    }
}
