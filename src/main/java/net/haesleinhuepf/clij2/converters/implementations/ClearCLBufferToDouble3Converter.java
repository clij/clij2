package net.haesleinhuepf.clij2.converters.implementations;

import net.haesleinhuepf.clij2.converters.helptypes.Double3;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.converters.AbstractCLIJConverter;
import net.haesleinhuepf.clij.converters.CLIJConverterPlugin;
import org.scijava.plugin.Plugin;

import java.nio.FloatBuffer;

@Plugin(type = CLIJConverterPlugin.class)
public class ClearCLBufferToDouble3Converter extends AbstractCLIJConverter<ClearCLBuffer, Double3> {

    @Override
    public Double3 convert(ClearCLBuffer source) {
        Double3 target = new Double3(new double[(int)source.getWidth()][(int)source.getHeight()][(int)source.getDepth()]);
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
    public Class<Double3> getTargetType() {
        return Double3.class;
    }

    @Override
    public Class<ClearCLBuffer> getSourceType() {
        return ClearCLBuffer.class;
    }
}

