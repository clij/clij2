package net.haesleinhuepf.clij2.converters.implementations;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.converters.AbstractCLIJConverter;
import net.haesleinhuepf.clij.converters.CLIJConverterPlugin;
import net.haesleinhuepf.clij2.converters.helptypes.Byte2;
import net.haesleinhuepf.clij2.converters.helptypes.Char2;
import org.scijava.plugin.Plugin;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

@Plugin(type = CLIJConverterPlugin.class)
public class ClearCLBufferToChar2Converter extends AbstractCLIJConverter<ClearCLBuffer, Char2> {

    @Override
    public Char2 convert(ClearCLBuffer source) {
        Char2 target = new Char2(new char[(int)source.getWidth()][(int)source.getHeight()]);
        char[] array = new char[(int)(source.getWidth() * source.getHeight())];

        CharBuffer buffer = CharBuffer.wrap(array);
        source.writeTo(buffer, true);

        int count = 0;
        for (int y = 0; y < target.data[0].length; y++) {
            for (int x = 0; x < target.data.length; x++) {
                target.data[x][y] = array[count];
                count++;
            }
        }
        return target;
    }

    @Override
    public Class<Char2> getTargetType() {
        return Char2.class;
    }

    @Override
    public Class<ClearCLBuffer> getSourceType() {
        return ClearCLBuffer.class;
    }
}

