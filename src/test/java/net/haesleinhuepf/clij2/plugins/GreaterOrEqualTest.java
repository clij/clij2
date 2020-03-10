package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.test.TestUtilities;
import net.haesleinhuepf.clijx.CLIJx;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class GreaterOrEqualTest {
    @Test
    public void test() {
        Img<UnsignedShortType> a = ArrayImgs.unsignedShorts(new short[]{
                1, 2, 3,
                4, 5, 6
        }, new long[]{3, 2});
        Img<UnsignedShortType> b = ArrayImgs.unsignedShorts(new short[]{
                1, 3, 3,
                4, 4, 6
        }, new long[]{3, 2});
        Img<UnsignedShortType> c = ArrayImgs.unsignedShorts(new short[]{
                1, 0, 1,
                1, 1, 1
        }, new long[]{3, 2});

        CLIJx clijx = CLIJx.getInstance();

        ClearCLBuffer clA = clijx.push(a);
        ClearCLBuffer clB = clijx.push(b);
        ClearCLBuffer clTest = clijx.create(clA);
        ClearCLBuffer clC = clijx.push(c);

        clijx.greaterOrEqual(clA, clB, clTest);
        TestUtilities.printBuffer(CLIJ.getInstance(), clTest);
        assertTrue(clijx.matrixEqual(clTest, clC, 0f));

        clA.close();
        clB.close();
        clC.close();
        clTest.close();
    }
}