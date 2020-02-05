package net.haesleinhuepf.clijx.matrix;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.test.TestUtilities;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.plugins.GaussJordan;
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

        CLIJx clijx = CLIJx.getInstance();

        ClearCLBuffer clA = clijx.push(a);
        ClearCLBuffer clB = clijx.push(b);

        // a * b using CLIJ
        CLIJ clij = CLIJ.getInstance();
        GaussJordan gj = new GaussJordan();
        gj.setClij(clij);
        gj.setArgs(new Object[]{clA, clB});
        ClearCLBuffer clTest = gj.createOutputBufferFromSource(clA);
        ClearCLBuffer clC = clijx.push(c);
        GaussJordan.gaussJordan(clij, clA, clB, clTest);
        TestUtilities.printBuffer(CLIJ.getInstance(), clTest);
        assertTrue(clijx.matrixEqual(clTest, clC, 0f));
        clC.close();
    }

}