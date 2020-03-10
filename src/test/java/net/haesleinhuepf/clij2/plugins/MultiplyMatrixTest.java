package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.test.TestUtilities;
import net.haesleinhuepf.clij2.plugins.MultiplyMatrix;
import net.haesleinhuepf.clijx.CLIJx;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultiplyMatrixTest {
    @Test
    public void test() {
        Img<UnsignedShortType> a = ArrayImgs.unsignedShorts(new short[]{
                7, 8,
                9, 10,
                11, 12
        }, new long[]{2, 3});
        Img<UnsignedShortType> b = ArrayImgs.unsignedShorts(new short[]{
                1, 2, 3,
                4, 5, 6
        }, new long[]{3, 2});

        // a * b according to matlab
        Img<UnsignedShortType> c = ArrayImgs.unsignedShorts(new short[]{
                39, 54, 69,
                49, 68, 87,
                59, 82, 105
        }, new long[]{3, 3});

        // b * a according to matlab
        Img<UnsignedShortType> cInv = ArrayImgs.unsignedShorts(new short[]{
                58, 64,
                139, 154
        }, new long[]{2, 2});

        CLIJx clijx = CLIJx.getInstance();

        ClearCLBuffer clA = clijx.push(a);
        ClearCLBuffer clB = clijx.push(b);

        // a * b using CLIJ
        MultiplyMatrix mm = new MultiplyMatrix();
        mm.setClij(CLIJ.getInstance());
        mm.setArgs(new Object[]{clA, clB});
        ClearCLBuffer clTest = mm.createOutputBufferFromSource(clA);
        ClearCLBuffer clC = clijx.push(c);
        clijx.multiplyMatrix(clA, clB, clTest);
        TestUtilities.printBuffer(CLIJ.getInstance(), clTest);
        assertTrue(clijx.matrixEqual(clTest, clC, 0f));
        clC.close();

        // b * a using CLIJ
        mm = new MultiplyMatrix();
        mm.setClij(CLIJ.getInstance());
        mm.setArgs(new Object[]{clB, clA});
        clTest = mm.createOutputBufferFromSource(clA);
        clC = clijx.push(cInv);
        clijx.multiplyMatrix(clB, clA, clTest);
        TestUtilities.printBuffer(CLIJ.getInstance(), clTest);
        assertTrue(clijx.matrixEqual(clTest, clC, 0f));



        clA.close();
        clB.close();
        clC.close();
        clTest.close();
    }


    @Test
    public void test2() {
        Img<UnsignedShortType> a = ArrayImgs.unsignedShorts(new short[]{
                1, 2, 3
        }, new long[]{3, 1});
        Img<UnsignedShortType> b = ArrayImgs.unsignedShorts(new short[]{
                1,
                2,
                3
        }, new long[]{1, 3});
        Img<UnsignedShortType> c = ArrayImgs.unsignedShorts(new short[]{
                14
        }, new long[]{1, 1});

        CLIJx clijx = CLIJx.getInstance();

        ClearCLBuffer clA = clijx.push(a);
        ClearCLBuffer clB = clijx.push(b);
        MultiplyMatrix mm = new MultiplyMatrix();
        mm.setClij(CLIJ.getInstance());
        mm.setArgs(new Object[]{clA, clB});
        ClearCLBuffer clTest = mm.createOutputBufferFromSource(clA);
        ClearCLBuffer clC = clijx.push(c);

        clijx.multiplyMatrix(clA, clB, clTest);
        TestUtilities.printBuffer(CLIJ.getInstance(), clTest);
        assertTrue(clijx.matrixEqual(clTest, clC, 0f));

        clA.close();
        clB.close();
        clC.close();
        clTest.close();
    }

    @Test
    public void test3() {
        Img<UnsignedShortType> a = ArrayImgs.unsignedShorts(new short[]{
                1, 2, 3
        }, new long[]{3, 1});
        Img<UnsignedShortType> b = ArrayImgs.unsignedShorts(new short[]{
                1, 1,
                2, 3,
                4, 3
        }, new long[]{2, 3});
        Img<UnsignedShortType> c = ArrayImgs.unsignedShorts(new short[]{
                17, 16
        }, new long[]{2, 1});

        CLIJx clijx = CLIJx.getInstance();

        ClearCLBuffer clA = clijx.push(a);
        ClearCLBuffer clB = clijx.push(b);
        MultiplyMatrix mm = new MultiplyMatrix();
        mm.setClij(CLIJ.getInstance());
        mm.setArgs(new Object[]{clA, clB});
        ClearCLBuffer clTest = mm.createOutputBufferFromSource(clA);
        ClearCLBuffer clC = clijx.push(c);

        clijx.multiplyMatrix(clA, clB, clTest);
        TestUtilities.printBuffer(CLIJ.getInstance(), clTest);
        assertTrue(clijx.matrixEqual(clTest, clC, 0f));

        clA.close();
        clB.close();
        clC.close();
        clTest.close();
    }
}