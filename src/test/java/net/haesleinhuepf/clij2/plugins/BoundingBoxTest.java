package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clijx.CLIJx;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class BoundingBoxTest {
    @Test
    public void test2D() {
        CLIJ2 clij2 = CLIJ2.getInstance();

        ClearCLBuffer buffer = clij2.create(new long[]{100, 100}, NativeTypeEnum.Byte);

        clij2.set(buffer, 0f);

        clij2.drawBox(buffer, 10f, 10f, 20f, 20f);

        double[] bb = clij2.boundingBox(buffer);
        System.out.println("bb " + Arrays.toString(bb));

        double[] reference = {10, 10, 0, 20, 20, 0};
        assertTrue(Arrays.equals(reference, bb));

        clij2.clear();
    }

    @Test
    public void test3D() {
        CLIJ2 clij2 = CLIJ2.getInstance();

        ClearCLBuffer buffer = clij2.create(new long[]{100, 100, 10}, NativeTypeEnum.Byte);

        clij2.set(buffer, 0f);

        clij2.drawBox(buffer, 10f, 10f, 2f, 20f, 20f, 7f );

        double[] bb = clij2.boundingBox(buffer);
        System.out.println("bb " + Arrays.toString(bb));

        double[] reference = {10, 10, 2, 20, 20, 7};
        assertTrue(Arrays.equals(reference, bb));

        clij2.clear();
    }
}