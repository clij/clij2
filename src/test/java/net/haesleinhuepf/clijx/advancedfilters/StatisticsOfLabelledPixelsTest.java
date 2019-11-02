package net.haesleinhuepf.clijx.advancedfilters;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import org.junit.Test;

import static org.junit.Assert.*;

public class StatisticsOfLabelledPixelsTest {


    @Test
    public void testStats() {
        CLIJ clij = CLIJ.getInstance();
        Img<UnsignedShortType> img = ArrayImgs.unsignedShorts(new short[]{
                1, 1, 2,
                1, 2, 2,
                2, 2, 5,
                2, 5, 5
        }, new long[]{3, 2, 2});

        Img<UnsignedShortType> lab = ArrayImgs.unsignedShorts(new short[]{
                1, 1, 2,
                1, 2, 2,
                1, 1, 2,
                1, 2, 2
        }, new long[]{3, 2, 2});


        ClearCLBuffer input = clij.push(img);
        ClearCLBuffer labels = clij.push(lab);

        double[][]stats = StatisticsOfLabelledPixels.statisticsOfLabelledPixels(clij, input, labels);
        System.out.println("Objects found: " + stats.length);

        assertEquals(6, stats[0][StatisticsOfLabelledPixels.STATISTICS_ENTRY.PIXEL_COUNT.value], 0);
        assertEquals(9, stats[0][StatisticsOfLabelledPixels.STATISTICS_ENTRY.SUM_INTENSITY.value], 0);
        assertEquals(1.5, stats[0][StatisticsOfLabelledPixels.STATISTICS_ENTRY.MEAN_INTENSITY.value], 0);
        assertEquals(0.5, stats[0][StatisticsOfLabelledPixels.STATISTICS_ENTRY.STANDARD_DEVIATION_INTENSITY.value], 0);

        assertEquals(6, stats[1][StatisticsOfLabelledPixels.STATISTICS_ENTRY.PIXEL_COUNT.value], 0);
        assertEquals(21, stats[1][StatisticsOfLabelledPixels.STATISTICS_ENTRY.SUM_INTENSITY.value], 0);
        assertEquals(3.5, stats[1][StatisticsOfLabelledPixels.STATISTICS_ENTRY.MEAN_INTENSITY.value], 0);
        assertEquals(1.5, stats[1][StatisticsOfLabelledPixels.STATISTICS_ENTRY.STANDARD_DEVIATION_INTENSITY.value], 0);

    }

}