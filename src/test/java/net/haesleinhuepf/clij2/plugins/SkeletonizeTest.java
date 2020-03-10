package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.test.TestUtilities;
import net.haesleinhuepf.clij2.CLIJ2;
import net.imglib2.img.array.ArrayImgs;
import org.junit.Test;

import static org.junit.Assert.*;

public class SkeletonizeTest {
    @Test
    public void testSkeletonize2D() {
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
                        0, 0, 0, 0, 1, 1, 1, 1, 1, 0,
                }, new long[]{10, 10}
        ));

        ClearCLBuffer refereanceResult = clij2.push(ArrayImgs.floats(new float[]{
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 1, 1, 1, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 1, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 1, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 1, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 1, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 1, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                }, new long[]{10, 10}
        ));

        ClearCLBuffer skeleton = clij2.create(binary_image);

        Skeletonize.skeletonize(clij2, binary_image, skeleton);

        System.out.println("Result: ");
        clij2.print(skeleton);

        TestUtilities.clBuffersEqual(clij2.getCLIJ(), skeleton, refereanceResult, 0);

        clij2.clear();
    }

}