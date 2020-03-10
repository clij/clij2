package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCL;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.test.TestUtilities;
import net.haesleinhuepf.clijx.CLIJx;
import net.imglib2.img.array.ArrayImgs;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class SpotsToPointListTest {
    @Test
    public void test() {
        float[] flt = {
                0, 0, 0, 0,
                0, 1, 0, 2
        };

        float[] ref = {
                1, 3,
                1, 1
        };

        CLIJx clijx = CLIJx.getInstance();

        ClearCLBuffer input2D = clijx.push(ArrayImgs.floats(flt, 4, 2));
        ClearCLBuffer input3D = clijx.push(ArrayImgs.floats(flt, 4, 2, 1));
        ClearCLBuffer output2Da = clijx.create(new long[]{2,2}, clijx.Float);
        ClearCLBuffer output3Da = clijx.create(new long[]{2,2,1}, clijx.Float);
        ClearCLBuffer output2Db = clijx.create(new long[]{2,2}, clijx.Float);
        ClearCLBuffer output3Db = clijx.create(new long[]{2,2,1}, clijx.Float);

        ClearCLBuffer ref2D = clijx.push(ArrayImgs.floats(ref, 2, 2));
        ClearCLBuffer ref3D = clijx.push(ArrayImgs.floats(ref, 2, 2, 1));

        clijx.labelledSpotsToPointList(input2D, output2Da);
        clijx.labelledSpotsToPointList(input2D, output3Da);
        clijx.labelledSpotsToPointList(input3D, output2Db);
        clijx.labelledSpotsToPointList(input3D, output3Db);

        double mse = clijx.meanSquaredError(output2Da, ref2D);
        System.out.println("mse " + mse);
        assertTrue(TestUtilities.clBuffersEqual(clijx.getClij(), output2Da, ref2D, 0));
        assertTrue(TestUtilities.clBuffersEqual(clijx.getClij(), output2Db, ref2D, 0));
        assertTrue(TestUtilities.clBuffersEqual(clijx.getClij(), output3Da, ref3D, 0));
        assertTrue(TestUtilities.clBuffersEqual(clijx.getClij(), output3Db, ref3D, 0));

    }
}
