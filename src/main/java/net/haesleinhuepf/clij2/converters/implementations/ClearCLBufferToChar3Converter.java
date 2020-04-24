package net.haesleinhuepf.clij2.converters.implementations;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.converters.AbstractCLIJConverter;
import net.haesleinhuepf.clij.converters.CLIJConverterPlugin;
import net.haesleinhuepf.clij2.converters.helptypes.Byte3;
import net.haesleinhuepf.clij2.converters.helptypes.Char3;
import org.scijava.plugin.Plugin;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

@Plugin(type = CLIJConverterPlugin.class)
public class ClearCLBufferToChar3Converter extends AbstractCLIJConverter<ClearCLBuffer, Char3> {

    @Override
    public Char3 convert(ClearCLBuffer source) {
        Char3 target = new Char3(new char[(int)source.getWidth()][(int)source.getHeight()][(int)source.getDepth()]);
        char[] array = new char[(int)(source.getWidth() * source.getHeight() * source.getDepth())];

        CharBuffer buffer = CharBuffer.wrap(array);
        source.writeTo(buffer, true);

        int count = 0;
        for (int z = 0; z < target.data[0][0].length; z++) {
            for (int y = 0; y < target.data[0].length; y++) {
                for (int x = 0; x < target.data.length; x++) {
                    target.data[x][y][z] = array[count];
                    count++;
                }
            }
        }
        return target;
    }

    @Override
    public Class<Char3> getTargetType() {
        return Char3.class;
    }

    @Override
    public Class<ClearCLBuffer> getSourceType() {
        return ClearCLBuffer.class;
    }
}

