package net.haesleinhuepf.clij2.utilities;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.clearcl.enums.ImageChannelDataType;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij2.CLIJ2;

import java.awt.*;
import java.util.HashMap;

public class CLIJUtilities extends net.haesleinhuepf.clij.utilities.CLIJUtilities {
    public static ImageChannelDataType nativeToChannelType(NativeTypeEnum type) {
        switch (type) {
            case Float:
                return ImageChannelDataType.Float;
            case UnsignedByte:
                return ImageChannelDataType.UnsignedInt8;
            case UnsignedShort:
                return ImageChannelDataType.UnsignedInt16;
            case UnsignedInt:
                return ImageChannelDataType.UnsignedInt32;
            // todo: there is no long!
            case Byte:
                return ImageChannelDataType.SignedInt8;
            case Short:
                return ImageChannelDataType.SignedInt16;
            case Int:
                return ImageChannelDataType.SignedInt32;
        }
        return null;
    }

    public static boolean checkDimensions(long... numberOfDimensions) {
        for (int i = 0; i < numberOfDimensions.length - 1; i++) {
            if (!(numberOfDimensions[i] == numberOfDimensions[i + 1])) {
                return false;
            }
        }
        return true;
    }

    public static boolean executeSeparableKernel(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst, Class anchorClass, String clFilename, String kernelname, int kernelSizeX, int kernelSizeY, int kernelSizeZ, float blurSigmaX, float blurSigmaY, float blurSigmaZ, long dimensions) {
        assertDifferent(src, dst);

        int[] n = new int[]{kernelSizeX, kernelSizeY, kernelSizeZ};
        float[] blurSigma = new float[]{blurSigmaX, blurSigmaY, blurSigmaZ};

        ClearCLImageInterface temp1;
        ClearCLImageInterface temp2;
        if (src instanceof ClearCLBuffer) {
            temp1 = clij2.create(((ClearCLBuffer) src).getDimensions(), NativeTypeEnum.Float);
            temp2 = clij2.create(((ClearCLBuffer) src).getDimensions(), NativeTypeEnum.Float);
        } else if (src instanceof ClearCLImage) {
            temp1 = clij2.create(((ClearCLImage) src).getDimensions(), ImageChannelDataType.Float);
            temp2 = clij2.create(((ClearCLImage) src).getDimensions(), ImageChannelDataType.Float);
        } else {
            throw new IllegalArgumentException("Error: Wrong type of images in blurFast");
        }

        HashMap<String, Object> parameters = new HashMap<>();

        if (blurSigma[0] > 0) {
            parameters.clear();
            parameters.put("N", n[0]);
            parameters.put("s", blurSigma[0]);
            parameters.put("dim", 0);
            parameters.put("src", src);
            if (dimensions == 2) {
                parameters.put("dst", temp1);
            } else {
                parameters.put("dst", temp2);
            }
            clij2.execute(anchorClass, clFilename, kernelname, src.getDimensions(), src.getDimensions(), parameters);
        } else {
            if (dimensions == 2) {
                clij2.copy(src, temp1);
            } else {
                clij2.copy(src, temp2);
            }
        }

        if (blurSigma[1] > 0) {
            parameters.clear();
            parameters.put("N", n[1]);
            parameters.put("s", blurSigma[1]);
            parameters.put("dim", 1);
            if (dimensions == 2) {
                parameters.put("src", temp1);
                parameters.put("dst", dst);
            } else {
                parameters.put("src", temp2);
                parameters.put("dst", temp1);
            }
            clij2.execute(anchorClass, clFilename, kernelname, src.getDimensions(), src.getDimensions(), parameters);
        } else {
            if (dimensions == 2) {
                clij2.copy(temp1, dst);
            } else {
                clij2.copy(temp2, temp1);
            }
        }

        if (dimensions == 3) {
            if (blurSigma[2] > 0) {
                parameters.clear();
                parameters.put("N", n[2]);
                parameters.put("s", blurSigma[2]);
                parameters.put("dim", 2);
                parameters.put("src", temp1);
                parameters.put("dst", dst);
                clij2.execute(anchorClass, clFilename, kernelname, src.getDimensions(), src.getDimensions(), parameters);
            } else {
                clij2.copy(temp1, dst);
            }
        }

        if (temp1 instanceof ClearCLBuffer) {
            ((ClearCLBuffer) temp1).close();
            ((ClearCLBuffer) temp2).close();
        } else if (temp1 instanceof ClearCLImage) {
            ((ClearCLImage) temp1).close();
            ((ClearCLImage) temp2).close();
        }

        return true;
    }

}
