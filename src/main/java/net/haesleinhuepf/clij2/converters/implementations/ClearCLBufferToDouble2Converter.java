package net.haesleinhuepf.clij2.converters.implementations;

import net.haesleinhuepf.clij2.converters.helptypes.Double2;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.converters.AbstractCLIJConverter;
import net.haesleinhuepf.clij.converters.CLIJConverterPlugin;
import org.scijava.plugin.Plugin;

import java.nio.FloatBuffer;

/**
 * Todo: Shall we deal with float images differently than with double images?
 */
@Plugin(type = CLIJConverterPlugin.class)
public class ClearCLBufferToDouble2Converter extends AbstractCLIJConverter<ClearCLBuffer, Double2> {

    @Override
    public Double2 convert(ClearCLBuffer source) {
        Double2 target = new Double2(new double[(int)source.getWidth()][(int)source.getHeight()]);
        float[] array = new float[(int)(source.getWidth() * source.getHeight())];

        FloatBuffer buffer = FloatBuffer.wrap(array);
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
    public Class<Double2> getTargetType() {
        return Double2.class;
    }

    @Override
    public Class<ClearCLBuffer> getSourceType() {
        return ClearCLBuffer.class;
    }
}

