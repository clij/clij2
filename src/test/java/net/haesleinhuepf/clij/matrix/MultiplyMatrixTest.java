package net.haesleinhuepf.clij.matrix;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij2.CLIJ2;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.basictypeaccess.array.IntArray;
import net.imglib2.type.numeric.IntegerType;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultiplyMatrixTest {
    @Test
    public void test() {
        Img<UnsignedShortType> a = ArrayImgs.unsignedShorts(new short[]{
                1, 2, 3,
                4, 5, 6
        }, new long[]{3, 2});
        Img<UnsignedShortType> b = ArrayImgs.unsignedShorts(new short[]{
                7, 8,
                9, 10,
                11, 12
        }, new long[]{2, 3});
        Img<UnsignedShortType> c = ArrayImgs.unsignedShorts(new short[]{
                58, 64,
                139, 154
        }, new long[]{2, 2});

        CLIJ2 clij2 = CLIJ2.getInstance();

        ClearCLBuffer clA = clij2.push(a);
        ClearCLBuffer clB = clij2.push(b);
        ClearCLBuffer clTest = clij2.create(clA);
        ClearCLBuffer clC = clij2.push(c);

        clij2.op.multiplyMatrix(clA, clB, clTest);
        clij2.op.matrixEqual(clTest, clC, 0f);
    }

}