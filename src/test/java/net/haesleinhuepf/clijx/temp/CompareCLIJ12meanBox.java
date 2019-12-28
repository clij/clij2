package net.haesleinhuepf.clijx.temp;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clijx.CLIJx;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class CompareCLIJ12meanBox {
    @Test
    public void compareMeanBox(){
        CLIJ clij = CLIJ.getInstance();

        CLIJx clijx = CLIJx.getInstance();

        System.out.println("CLIJ: " + clij.getGPUName() + " " + clij.getClearCLContext().toString());
        System.out.println("CLIJx: " + clijx.getGPUName() + " " + clijx.getClij().getClearCLContext().toString());

        ClearCLBuffer input = clij.create(new long[]{100, 100, 50}, NativeTypeEnum.Float);

        clijx.drawBox(input, 10, 10, 10, 10, 10, 10);

        ClearCLBuffer output1 = clijx.create(input);
        ClearCLBuffer output2 = clijx.create(input);

        clij.op().meanBox(input, output1, 3, 3, 3);
        clijx.meanBox(input, output2, 3, 3, 3);

        double mean1 = clijx.meanOfAllPixels(output1);
        double mean2 = clijx.meanOfAllPixels(output2);

        double mse = clijx.meanSquaredError(output1, output2);
        System.out.println("mean1: " + mean1);
        System.out.println("mean2: " + mean2);
        System.out.println("mse: " + mse);
        assertEquals(0, mse, 0.001);
        assertNotEquals(0, mean1);
        assertNotEquals(0, mean2);
    }
}
