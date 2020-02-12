package net.haesleinhuepf.clijx.plugins.tenengradfusion;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clijx.CLIJx;

import static org.junit.Assert.*;

public class AbstractTenengradFusionTest {

    public static void main(String[] args) {
        CLIJx clijx = CLIJx.getInstance();
        ClearCLBuffer buffer1 = clijx.create(100, 100, 1);
        clijx.set(buffer1, 5);

        ClearCLBuffer buffer2 = clijx.create(buffer1);
        clijx.setRampX(buffer2);

        ClearCLBuffer result = clijx.create(buffer1);

        float[] sigmas = {5,5,0};
        TenengradFusionOf2.tenengradFusion(clijx, result, sigmas, 1.0f, buffer1, buffer2);

        assertTrue(clijx.sumOfAllPixels(buffer2) != 0);
    }
}