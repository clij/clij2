package net.haesleinhuepf.clij.advancedfilters;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.test.TestUtilities;
import net.haesleinhuepf.clijx.CLIJx;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import org.junit.Test;

import static org.junit.Assert.*;

public class PowerImagesTest {
    @Test
    public void test() {
        Img<UnsignedShortType> a = ArrayImgs.unsignedShorts(new short[]{
                1, 2,
                1, 2
        }, new long[]{2, 2});
        Img<UnsignedShortType> b = ArrayImgs.unsignedShorts(new short[]{
                1, 2,
                9, 3
        }, new long[]{2, 2});
        Img<UnsignedShortType> c = ArrayImgs.unsignedShorts(new short[]{
                1, 4,
                1, 8
        }, new long[]{2, 2});

        CLIJx CLIJx = CLIJx.getInstance();

        ClearCLBuffer clA = CLIJx.push(a);
        ClearCLBuffer clB = CLIJx.push(b);
        ClearCLBuffer clTest = CLIJx.create(clA);
        ClearCLBuffer clC = CLIJx.push(c);

        CLIJx.op.powerImages(clA, clB, clTest);
        TestUtilities.printBuffer(CLIJ.getInstance(), clTest);
        assertTrue(CLIJx.op.matrixEqual(clTest, clC, 0f));

        clA.close();
        clB.close();
        clC.close();
        clTest.close();
    }

}