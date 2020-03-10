package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.test.TestUtilities;
import net.haesleinhuepf.clij2.CLIJ2;
import net.imglib2.img.array.ArrayImgs;
import org.junit.Test;

public class SetImageBordersTest {
    @Test
    public void testSetImageBorders2D() {
        CLIJ2 clij2 = CLIJ2.getInstance();

        ClearCLBuffer binary_image = clij2.push(ArrayImgs.floats(new float[]{
                        1, 1, 1, 0, 0, 0, 0, 0, 0, 0,
                        1, 1, 1, 1, 1, 1, 0, 0, 0, 0,
                        1, 1, 1, 1, 1, 1, 1, 0, 0, 0,
                        0, 0, 0, 1, 1, 1, 1, 0, 0, 0,
                        0, 0, 0, 0, 1, 1, 1, 0, 0, 0,

                        0, 0, 0, 0, 1, 1, 1, 1, 0, 0,
                        0, 0, 0, 0, 1, 1, 1, 1, 1, 0,
                        0, 0, 0, 0, 1, 1, 1, 1, 1, 0,
                        0, 0, 0, 0, 1, 1, 1, 1, 1, 0,
                        0, 0, 0, 0, 1, 1, 1, 1, 1, 0
                }, new long[]{10, 10}
        ));

        ClearCLBuffer refereanceResult = clij2.push(ArrayImgs.floats(new float[]{
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 1, 1, 1, 1, 1, 0, 0, 0, 0,
                0, 1, 1, 1, 1, 1, 1, 0, 0, 0,
                0, 0, 0, 1, 1, 1, 1, 0, 0, 0,
                0, 0, 0, 0, 1, 1, 1, 0, 0, 0,

                0, 0, 0, 0, 1, 1, 1, 1, 0, 0,
                0, 0, 0, 0, 1, 1, 1, 1, 1, 0,
                0, 0, 0, 0, 1, 1, 1, 1, 1, 0,
                0, 0, 0, 0, 1, 1, 1, 1, 1, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                }, new long[]{10, 10}
        ));

        ClearCLBuffer result = clij2.create(binary_image);

        clij2.copy(binary_image, refereanceResult);
        SetImageBorders.setImageBorders(clij2, result, 0f);

        System.out.println("Result: ");
        clij2.print(result);

        TestUtilities.clBuffersEqual(clij2.getCLIJ(), result, refereanceResult, 0);

        clij2.clear();
    }

}