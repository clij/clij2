package net.haesleinhuepf.clij2.converters.implementations;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.converters.AbstractCLIJConverter;
import net.haesleinhuepf.clij.converters.CLIJConverterPlugin;
import net.haesleinhuepf.clij2.converters.helptypes.Byte2;
import net.haesleinhuepf.clij2.converters.helptypes.Float2;
import org.scijava.plugin.Plugin;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

@Plugin(type = CLIJConverterPlugin.class)
public class ClearCLBufferToByte2Converter extends AbstractCLIJConverter<ClearCLBuffer, Byte2> {

    @Override
    public Byte2 convert(ClearCLBuffer source) {
        Byte2 target = new Byte2(new byte[(int)source.getWidth()][(int)source.getHeight()]);
        byte[] array = new byte[(int)(source.getWidth() * source.getHeight())];

        ByteBuffer buffer = ByteBuffer.wrap(array);
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
    public Class<Byte2> getTargetType() {
        return Byte2.class;
    }

    @Override
    public Class<ClearCLBuffer> getSourceType() {
        return ClearCLBuffer.class;
    }
}

