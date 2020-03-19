package net.haesleinhuepf.clij2.converters.implementations;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.converters.AbstractCLIJConverter;
import net.haesleinhuepf.clij.converters.CLIJConverterPlugin;
import net.haesleinhuepf.clij2.converters.helptypes.Double3;
import net.haesleinhuepf.clij2.converters.helptypes.Float3;
import org.scijava.plugin.Plugin;

import java.nio.FloatBuffer;

@Plugin(type = CLIJConverterPlugin.class)
public class ClearCLBufferToFloat3Converter extends AbstractCLIJConverter<ClearCLBuffer, Float3> {

    @Override
    public Float3 convert(ClearCLBuffer source) {
        Float3 target = new Float3(new float[(int)source.getWidth()][(int)source.getHeight()][(int)source.getDepth()]);
        float[] array = new float[(int)(source.getWidth() * source.getHeight() * source.getDepth())];

        FloatBuffer buffer = FloatBuffer.wrap(array);
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
    public Class<Float3> getTargetType() {
        return Float3.class;
    }

    @Override
    public Class<ClearCLBuffer> getSourceType() {
        return ClearCLBuffer.class;
    }
}

