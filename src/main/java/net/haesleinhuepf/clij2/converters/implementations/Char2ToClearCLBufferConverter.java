package net.haesleinhuepf.clij2.converters.implementations;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.converters.AbstractCLIJConverter;
import net.haesleinhuepf.clij.converters.CLIJConverterPlugin;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij2.converters.helptypes.Byte2;
import net.haesleinhuepf.clij2.converters.helptypes.Char2;
import org.scijava.plugin.Plugin;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

@Plugin(type = CLIJConverterPlugin.class)
public class Char2ToClearCLBufferConverter extends AbstractCLIJConverter<Char2, ClearCLBuffer> {

    @Override
    public ClearCLBuffer convert(Char2 source) {
        long[] dimensions = new long[]{
                source.data.length,
                source.data[0].length
        };

        int numberOfPixelsPerSlice = (int)(dimensions[0] * dimensions[1]);
        long numberOfPixels = numberOfPixelsPerSlice;


        ClearCLBuffer target = clij.createCLBuffer(dimensions, NativeTypeEnum.UnsignedShort);
        char[] inputArray = new char[(int)numberOfPixels];

        int count = 0;
        for (int y = 0; y < dimensions[1]; y++) {
            for (int x = 0; x < dimensions[0]; x++) {
                inputArray[count] = source.data[x][y];
                count++;
            }
        }
        CharBuffer charBuffer = CharBuffer.wrap(inputArray);
        target.readFrom(charBuffer, true);
        return target;
    }

    @Override
    public Class<Char2> getSourceType() {
        return Char2.class;
    }

    @Override
    public Class<ClearCLBuffer> getTargetType() {
        return ClearCLBuffer.class;
    }
}

