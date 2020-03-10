package net.haesleinhuepf.clij2.plugins;

import ij.IJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.clearcl.enums.ImageChannelDataType;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.test.TestUtilities;
import net.haesleinhuepf.clijx.CLIJx;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.integer.ByteType;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class DownsampleSliceBySliceHalfMedianTest {
    @Test
    public void downsampleSliceBySliceMedian() {
        CLIJx clijx = CLIJx.getInstance();

        Img<ByteType> testImgBig = ArrayImgs.bytes(new byte[]{
                1, 2, 4, 4,
                2, 3, 4, 4,
                0, 1, 0, 0,
                1, 2, 0, 0
        }, new long[]{4, 4, 1});

        Img<ByteType> testImgSmall = ArrayImgs.bytes(new byte[]{
                2, 4,
                1, 0
        }, new long[]{2, 2, 1});

        ClearCLImage inputCL = clijx.convert(testImgBig, ClearCLImage.class);
        ClearCLImage outputCL = clijx.create(new long[]{2, 2, 1}, ImageChannelDataType.SignedInt8);

        clijx.downsampleSliceBySliceHalfMedian(inputCL, outputCL);

        ImagePlus result = clijx.convert(outputCL, ImagePlus.class);
        ImagePlus reference = clijx.convert(testImgSmall, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(result, reference));
        IJ.exit();
        clijx.clear();
    }

    @Test
    public void downsampleSliceBySliceMedian_Buffer() {
        CLIJx clijx = CLIJx.getInstance();

        Img<ByteType> testImgBig = ArrayImgs.bytes(new byte[]{
                1, 2, 4, 4,
                2, 3, 4, 4,
                0, 1, 0, 0,
                1, 2, 0, 0
        }, new long[]{4, 4, 1});

        Img<ByteType> testImgSmall = ArrayImgs.bytes(new byte[]{
                2, 4,
                1, 0
        }, new long[]{2, 2, 1});

        ClearCLBuffer inputCL = clijx.convert(testImgBig, ClearCLBuffer.class);
        ClearCLBuffer outputCL = clijx.create(new long[]{2, 2, 1}, NativeTypeEnum.Byte);

        clijx.downsampleSliceBySliceHalfMedian(inputCL, outputCL);

        ImagePlus result = clijx.convert(outputCL, ImagePlus.class);
        ImagePlus reference = clijx.convert(testImgSmall, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(result, reference));
        IJ.exit();
        clijx.clear();
    }


}