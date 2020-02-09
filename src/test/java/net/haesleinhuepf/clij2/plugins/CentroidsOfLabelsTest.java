package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.test.TestUtilities;
import net.haesleinhuepf.clij2.CLIJ2;
import net.imglib2.img.array.ArrayImgs;
import org.junit.Test;

import static org.junit.Assert.*;

public class CentroidsOfLabelsTest {
    @Test
    public void centroids() {
        CLIJ2 clij2 = CLIJ2.getInstance();

        ClearCLBuffer labelmap = clij2.push(ArrayImgs.floats(new float[]{
                        1, 1, 1, 0, 0,
                        1, 1, 1, 0, 0,
                        1, 1, 1, 2, 0,
                        0, 0, 2, 2, 2,
                        0, 0, 0, 2, 0

                }, new long[]{5, 5}
        ));

        ClearCLBuffer reference = clij2.push(ArrayImgs.floats(new float[]{
                        1, 3,
                        1, 3

                }, new long[]{2, 2}
        ));

        ClearCLBuffer pointlist = clij2.create(new long[]{2,2});

        CentroidsOfLabels.centroidsOfLabels(clij2, labelmap, pointlist);

        assertTrue(TestUtilities.clBuffersEqual(clij2.getCLIJ(), reference, pointlist, 0));

        clij2.clear();
    }

}