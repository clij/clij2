package net.haesleinhuepf.clij.advancedfilters;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clijx.CLIJx;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class BoundingBoxTest {
    @Test
    public void test2D() {
        CLIJx CLIJx = CLIJx.getInstance();

        ClearCLBuffer buffer = CLIJx.create(new long[]{100, 100}, NativeTypeEnum.Byte);

        CLIJx.op.set(buffer, 0f);

        CLIJx.op.drawBox(buffer, 10f, 10f, 20f, 20f);

        double[] bb = CLIJx.op.boundingBox(buffer);
        System.out.println("bb " + Arrays.toString(bb));

        double[] reference = {10, 10, 20, 20};
        assertTrue(Arrays.equals(reference, bb));
    }

    @Test
    public void test3D() {
        CLIJx CLIJx = CLIJx.getInstance();

        ClearCLBuffer buffer = CLIJx.create(new long[]{100, 100, 10}, NativeTypeEnum.Byte);

        CLIJx.op.set(buffer, 0f);

        CLIJx.op.drawBox(buffer, 10f, 10f, 2f, 20f, 20f, 7f );

        double[] bb = CLIJx.op.boundingBox(buffer);
        System.out.println("bb " + Arrays.toString(bb));

        double[] reference = {10, 10, 2, 20, 20, 7};
        assertTrue(Arrays.equals(reference, bb));
    }
}