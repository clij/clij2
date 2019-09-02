package net.haesleinhuepf.clij.advancedfilters;

import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.matrix.MatrixEqual;
import net.haesleinhuepf.clij2.CLIJ2;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;
import org.junit.Test;

import static org.junit.Assert.*;

public class ResultsTableToImage2DTest {
    @Test
    public void test8bit() {
        Img<UnsignedByteType> a = ArrayImgs.unsignedBytes(new byte[]{
                1, 2, 3,
                4, 5, 6
        }, new long[]{3, 2});
        test(a);
    }

    @Test
    public void test16bit() {
        Img<UnsignedShortType> a = ArrayImgs.unsignedShorts(new short[]{
                1, 2, 3,
                4, 5, 6
        }, new long[]{3, 2});
        test(a);
    }

    @Test
    public void tes32bit() {
        Img<FloatType> a = ArrayImgs.floats(new float[]{
                1, 2, 3,
                4, 5, 6
        }, new long[]{3, 2});
        test(a);
    }

    private void test(Img a) {
        CLIJ2 clij2 = CLIJ2.getInstance();
        ClearCLBuffer buffer1 = clij2.push(a);

        ResultsTable table = Image2DToResultsTable.image2DToResultsTable(CLIJ.getInstance(), buffer1, ResultsTable.getResultsTable());

        ClearCLBuffer buffer2 = clij2.create(buffer1);
        ResultsTableToImage2D.resultsTableToImage2D(CLIJ.getInstance(), buffer2, table);

        assertTrue(MatrixEqual.matrixEqual(CLIJ.getInstance(), buffer1, buffer2, 0f));
        ResultsTable.getResultsTable().reset();
    }
}