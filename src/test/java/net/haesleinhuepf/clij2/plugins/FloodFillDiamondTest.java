package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.test.TestUtilities;
import net.haesleinhuepf.clij2.CLIJ2;
import net.imglib2.img.array.ArrayImgs;
import org.junit.Test;

import static org.junit.Assert.*;

public class FloodFillDiamondTest {
    @Test
    public void testFloodFill() {
        CLIJ2 clij2 = CLIJ2.getInstance();

        ClearCLBuffer input_image = clij2.push(ArrayImgs.floats(new float[]{
                        1, 1, 1, 0, 0,
                        1, 1, 1, 1, 1,
                        1, 1, 1, 1, 1,
                        0, 0, 2, 1, 1,
                        0, 0, 0, 0, 1
                }, new long[]{5, 5}
        ));

        ClearCLBuffer refereanceResult = clij2.push(ArrayImgs.floats(new float[]{
                        1, 1, 1, 0, 0,
                        1, 1, 1, 1, 1,
                        1, 1, 1, 1, 1,
                        2, 2, 2, 1, 1,
                        2, 2, 2, 2, 1
                }, new long[]{5, 5}
        ));

        ClearCLBuffer result = clij2.create(input_image);

        FloodFillDiamond.floodFillDiamond(clij2, input_image, result, 0f, 2f);

        System.out.println("Result: ");
        clij2.print(result);

        TestUtilities.clBuffersEqual(clij2.getCLIJ(), result, refereanceResult, 0);

        clij2.clear();
    }

}