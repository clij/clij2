package net.haesleinhuepf.clijx.utilities;

import net.haesleinhuepf.clij.clearcl.enums.ImageChannelDataType;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;

import java.awt.*;

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
}
