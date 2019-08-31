package net.haesleinhuepf.clij.advancedfilters;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.integer.ByteType;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Author: @haesleinhuepf
 * July 2019
 */
public class SorensenDiceJaccardIndexTest {
    double tolerance = 0.01;

    @Test
    public void testJaccardIndex() {
        Img<ByteType> binary1 = ArrayImgs.bytes(new byte[]{
                0, 0, 0, 0, 0,
                0, 1, 1, 1, 1,
                0, 1, 1, 1, 1,
                0, 1, 1, 1, 1,
                0, 0, 0, 0, 0
        }, new long[]{5,5});
        Img<ByteType> binary2 = ArrayImgs.bytes(new byte[]{
                0, 0, 0, 0, 0,
                0, 1, 1, 0, 0,
                0, 1, 1, 0, 0,
                0, 1, 1, 0, 0,
                0, 0, 0, 0, 0
        }, new long[]{5,5});

        CLIJ clij = CLIJ.getInstance();

        ClearCLBuffer clBinary1 = clij.push(binary1);
        ClearCLBuffer clBinary2 = clij.push(binary2);

        double jaccardIndex = SorensenDiceJaccardIndex.jaccardIndex(clij, clBinary1, clBinary2);
        assertEquals(0.5, jaccardIndex, tolerance);

        clBinary1.close();
        clBinary2.close();
    }

}