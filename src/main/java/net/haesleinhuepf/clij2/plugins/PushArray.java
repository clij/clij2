package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

import java.awt.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.FloatBuffer;

/**
 * Author: @haesleinhuepf
 *         September 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_pushArray")
public class PushArray extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {

        ClearCLBuffer buffer = (ClearCLBuffer)( args[0]);
        Object array = args[1];

        getCLIJ2().pushArray(buffer, array);
        return true;
    }

    public static boolean pushArray(CLIJ2 clij2, ClearCLBuffer buffer, Object array) {
        Object[] arr = (Object[])array;

        int pixels = (int)(buffer.getWidth() * buffer.getHeight() * buffer.getDepth());
        if (buffer.getNativeType() == NativeTypeEnum.UnsignedByte) {
            byte[] output = new byte[pixels];
            int c = 0;
            for (Object obj : arr) {
                output[c] = ((Double) obj).byteValue();
                c++;
            }
            ByteBuffer byteBuffer = ByteBuffer.wrap(output);
            buffer.readFrom(byteBuffer, true);
        } else if (buffer.getNativeType() == NativeTypeEnum.UnsignedShort) {
            char[] output = new char[pixels];
            int c = 0;
            for (Object obj : arr) {
                output[c] = (char)(((Double) obj).intValue());
                c++;
            }
            CharBuffer charBuffer = CharBuffer.wrap(output);
            buffer.readFrom(charBuffer, true);
        } else if (buffer.getNativeType() == NativeTypeEnum.Float) {
            float[] output = new float[pixels];
            int c = 0;
            for (Object obj : arr) {
                output[c] = ((Double) obj).floatValue();
                c++;
            }
            FloatBuffer floatBuffer = FloatBuffer.wrap(output);
            buffer.readFrom(floatBuffer, true);
        } else {
            throw new IllegalArgumentException("pushArray doesn't support type " + buffer.getNativeType());
        }
        return true;
    }

    public static ClearCLBuffer pushArray(CLIJ2 clij2, float[] array, Integer width, Integer height, Integer depth) {
        ClearCLBuffer buffer = clij2.create(width, height, depth);
        FloatBuffer floatBuffer = FloatBuffer.wrap(array);
        buffer.readFrom(floatBuffer, true);
        return buffer;
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        int width = asInteger(args[2]);
        int height = asInteger(args[3]);
        int depth = asInteger(args[4]);
        return clij.create(new long[]{width, height, depth}, NativeTypeEnum.Float);
    }

    @Override
    public String getParameterHelpText() {
        return "ByRef Image destination, Array input, Number width, Number height, Number depth";
    }

    @Override
    public String getDescription() {
        return "Converts an array to a 3D image stack.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }

}
