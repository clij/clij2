package net.haesleinhuepf.clij2.plugins;

import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij2.plugins.Image2DToResultsTable;
import net.haesleinhuepf.clij2.plugins.MatrixEqual;
import net.haesleinhuepf.clij2.plugins.ResultsTableToImage2D;
import net.haesleinhuepf.clijx.CLIJx;
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
        CLIJx clijx = CLIJx.getInstance();
        ClearCLBuffer buffer1 = clijx.push(a);

        ResultsTable table = Image2DToResultsTable.image2DToResultsTable(clijx, buffer1, ResultsTable.getResultsTable());

        ClearCLBuffer buffer2 = clijx.create(buffer1);
        ResultsTableToImage2D.resultsTableToImage2D(clijx, buffer2, table);

        assertTrue(MatrixEqual.matrixEqual(clijx, buffer1, buffer2, 0f));
        ResultsTable.getResultsTable().reset();
    }
}