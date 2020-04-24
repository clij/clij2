package net.haesleinhuepf.clij2.converters.implementations;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.converters.AbstractCLIJConverter;
import net.haesleinhuepf.clij.converters.CLIJConverterPlugin;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij2.converters.helptypes.Byte1;
import net.haesleinhuepf.clij2.converters.helptypes.Char1;
import org.scijava.plugin.Plugin;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

@Plugin(type = CLIJConverterPlugin.class)
public class Char1ToClearCLBufferConverter extends AbstractCLIJConverter<Char1, ClearCLBuffer> {

    @Override
    public ClearCLBuffer convert(Char1 source) {
        long[] dimensions = new long[]{
                source.data.length,
                1
        };

        int numberOfPixelsPerSlice = (int)(dimensions[0] * dimensions[1]);
        long numberOfPixels = numberOfPixelsPerSlice;


        ClearCLBuffer target = clij.createCLBuffer(dimensions, NativeTypeEnum.UnsignedShort);
        char[] inputArray = new char[(int)numberOfPixels];

        int count = 0;
        for (int x = 0; x < dimensions[0]; x++) {
            inputArray[count] = source.data[x];
            count++;
        }
        CharBuffer charBuffer = CharBuffer.wrap(inputArray);
        target.readFrom(charBuffer, true);
        return target;
    }

    @Override
    public Class<Char1> getSourceType() {
        return Char1.class;
    }

    @Override
    public Class<ClearCLBuffer> getTargetType() {
        return ClearCLBuffer.class;
    }
}

