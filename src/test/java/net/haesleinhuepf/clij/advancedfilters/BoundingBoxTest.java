package net.haesleinhuepf.clij.advancedfilters;

import com.sun.prism.shader.FillCircle_Color_Loader;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij2.CLIJ2;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class BoundingBoxTest {
    @Test
    public void test2D() {
        CLIJ2 clij2 = CLIJ2.getInstance();

        ClearCLBuffer buffer = clij2.create(new long[]{100, 100}, NativeTypeEnum.Byte);

        clij2.op.set(buffer, 0f);

        clij2.op.drawBox(buffer, 10f, 10f, 20f, 20f);

        double[] bb = clij2.op.boundingBox(buffer);
        System.out.println("bb " + Arrays.toString(bb));

        double[] reference = {10, 10, 20, 20};
        assertTrue(Arrays.equals(reference, bb));
    }

    @Test
    public void test3D() {
        CLIJ2 clij2 = CLIJ2.getInstance();

        ClearCLBuffer buffer = clij2.create(new long[]{100, 100, 10}, NativeTypeEnum.Byte);

        clij2.op.set(buffer, 0f);

        clij2.op.drawBox(buffer, 10f, 10f, 2f, 20f, 20f, 7f );

        double[] bb = clij2.op.boundingBox(buffer);
        System.out.println("bb " + Arrays.toString(bb));

        double[] reference = {10, 10, 2, 20, 20, 7};
        assertTrue(Arrays.equals(reference, bb));
    }
}