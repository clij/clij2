package net.haesleinhuepf.clijx.plugins;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.FloatBuffer;

/**
 * Author: @haesleinhuepf
 *         September 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_pushArray")
public class PushArray extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {

        Object[] args = openCLBufferArgs();

        ClearCLBuffer buffer = (ClearCLBuffer)( args[0]);
        Object array = args[1];

        pushArray(clij, buffer, array);
        return true;
    }

    public static boolean pushArray(CLIJ clij, ClearCLBuffer buffer, Object array) {
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

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        int width = asInteger(args[2]);
        int height = asInteger(args[3]);
        int depth = asInteger(args[4]);
        return clij.create(new long[]{width, height, depth}, NativeTypeEnum.Float);
    }

    @Override
    public String getParameterHelpText() {
        return "Image destination, Array input, Number width, Number height, Number depth";
    }

    @Override
    public String getDescription() {
        return "Converts an array to an image.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
