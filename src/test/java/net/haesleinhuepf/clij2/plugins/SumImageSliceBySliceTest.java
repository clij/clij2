package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clijx.CLIJx;
import net.imglib2.img.array.ArrayImgs;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class SumImageSliceBySliceTest {
    @Test
    public void testSum() {
        CLIJx clijx = CLIJx.getInstance();

        ClearCLBuffer stack = clijx.push(ArrayImgs.floats(new float[]{
                        1, 1,
                        1, 1,

                        2, 2,
                        2, 2,

                        3, 3,
                        3, 3
                }, new long[]{2, 2, 3}
        ));

        clijx.print(stack);


        double[] intensities = clijx.sumImageSliceBySlice(stack);

        System.out.println("Int " + Arrays.toString(intensities));

        ClearCLBuffer stack2 = clijx.create(stack);
        clijx.multiplyImageStackWithScalars(stack, stack2, new float[]{3, 2, 1});
        assertEquals(4, intensities[0], 0);
        assertEquals(8, intensities[1], 0);
        assertEquals(12, intensities[2], 0);


        intensities = clijx.sumImageSliceBySlice(stack2);
        System.out.println("Int " + Arrays.toString(intensities));
        assertEquals(12, intensities[0], 0);
        assertEquals(16, intensities[1], 0);
        assertEquals(12, intensities[2], 0);

        clijx.clear();
    }

}