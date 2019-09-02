package net.haesleinhuepf.clij.matrix;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.test.TestUtilities;
import net.haesleinhuepf.clij2.CLIJ2;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.real.FloatType;
import org.junit.Test;

import static org.junit.Assert.*;

public class GaussJordanTest {
    @Test
    public void test() {
        Img<FloatType> a = ArrayImgs.floats(new float[]{
                1, 2,
                3, 4
        }, new long[]{2, 2});
        Img<FloatType> b = ArrayImgs.floats(new float[]{
                3,
                4
        }, new long[]{1, 2});

        // solution according to https://m.matrix.reshish.com/gaussSolution.php
        Img<FloatType> c = ArrayImgs.floats(new float[]{
                -2,
                2.5f
        }, new long[]{1, 2});

        CLIJ2 clij2 = CLIJ2.getInstance();

        ClearCLBuffer clA = clij2.push(a);
        ClearCLBuffer clB = clij2.push(b);

        // a * b using CLIJ
        CLIJ clij = CLIJ.getInstance();
        GaussJordan gj = new GaussJordan();
        gj.setClij(clij);
        gj.setArgs(new Object[]{clA, clB});
        ClearCLBuffer clTest = gj.createOutputBufferFromSource(clA);
        ClearCLBuffer clC = clij2.push(c);
        GaussJordan.gaussJordan(clij, clA, clB, clTest);
        TestUtilities.printBuffer(CLIJ.getInstance(), clTest);
        assertTrue(clij2.op.matrixEqual(clTest, clC, 0f));
        clC.close();
    }

}