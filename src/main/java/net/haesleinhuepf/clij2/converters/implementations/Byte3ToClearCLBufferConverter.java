package net.haesleinhuepf.clij2.converters.implementations;

import net.haesleinhuepf.clij2.converters.helptypes.Byte3;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.converters.AbstractCLIJConverter;
import net.haesleinhuepf.clij.converters.CLIJConverterPlugin;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import org.scijava.plugin.Plugin;

import java.nio.ByteBuffer;

@Plugin(type = CLIJConverterPlugin.class)
public class Byte3ToClearCLBufferConverter extends AbstractCLIJConverter<Byte3, ClearCLBuffer> {

    @Override
    public ClearCLBuffer convert(Byte3 source) {
        long[] dimensions = new long[]{
                source.data.length,
                source.data[0].length,
                source.data[0][0].length
        };

        int numberOfPixelsPerSlice = (int)(dimensions[0] * dimensions[1]);
        long numberOfPixels = numberOfPixelsPerSlice * dimensions[2];


        ClearCLBuffer target = clij.createCLBuffer(dimensions, NativeTypeEnum.UnsignedByte);
        byte[] inputArray = new byte[(int)numberOfPixels];

        int count = 0;
        for (int z = 0; z < dimensions[2]; z++) {
            for (int y = 0; y < dimensions[1]; y++) {
                for (int x = 0; x < dimensions[0]; x++) {
                    inputArray[count] = source.data[x][y][z];
                    count++;
                }
            }
        }
        ByteBuffer byteBuffer = ByteBuffer.wrap(inputArray);
        target.readFrom(byteBuffer, true);
        return target;
    }

    @Override
    public Class<Byte3> getSourceType() {
        return Byte3.class;
    }

    @Override
    public Class<ClearCLBuffer> getTargetType() {
        return ClearCLBuffer.class;
    }
}

