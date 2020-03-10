package net.haesleinhuepf.clij2.plugins;

import ij.IJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij2.plugins.MeanSquaredError;
import net.haesleinhuepf.clij2.plugins.Paste2D;
import net.haesleinhuepf.clij2.plugins.Paste3D;
import net.haesleinhuepf.clijx.CLIJx;
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
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus imp = IJ.openImage("src/test/resources/t1-head.tif");

        ClearCLBuffer input = clijx.push(imp);
        ClearCLBuffer intermediate = clijx.create(new long[]{1000, 1000, 200}, input.getNativeType());
        ClearCLBuffer output = clijx.create(input);
        Paste3D.paste(clijx, input, intermediate, 100, 100, 0);

        clijx.crop(intermediate, output, 100, 100, 0);

        double mse = MeanSquaredError.meanSquaredError(clijx, input, output);
        double sumIn = clijx.sumOfAllPixels(input);
        double sumOut = clijx.sumOfAllPixels(output);

        System.out.println(sumIn);
        assertEquals(mse, 0.0, 0.0);
        assertEquals(sumIn, sumOut, 0.0);

        input.close();
        intermediate.close();
        output.close();
    }

    @Test
    public void pasteCropTest2D() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus imp = IJ.openImage("src/test/resources/blobs.tif");

        ClearCLBuffer input = clijx.push(imp);
        ClearCLBuffer intermediate = clijx.create(new long[]{1000, 1000, 1}, input.getNativeType());
        ClearCLBuffer output = clijx.create(input);
        Paste2D.paste(clijx, input, intermediate, 100, 100);

        clijx.crop(intermediate, output, 100, 100, 0);

        double mse = MeanSquaredError.meanSquaredError(clijx, input, output);
        double sumIn = clijx.sumOfAllPixels(input);
        double sumOut = clijx.sumOfAllPixels(output);

        System.out.println(sumIn);
        assertEquals(mse, 0.0, 0.0);
        assertEquals(sumIn, sumOut, 0.0);

        input.close();
        intermediate.close();
        output.close();
    }
}
