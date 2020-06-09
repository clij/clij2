package net.haesleinhuepf.clij2.plugins;

import ij.ImagePlus;
import ij.ImageStack;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
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
 *         April 2020
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_pushString")
public class PushString extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {

        ClearCLBuffer buffer = (ClearCLBuffer)( args[0]);
        String image = (String)args[1];

        getCLIJ2().pushString(buffer, image);
        return true;
    }

    public static boolean pushString(CLIJ2 clij2, ClearCLBuffer buffer, String image) {
        ImageStack stack = new ImageStack();

        int depth = image.split("\n\n").length;
        int height = image.split("\n\n")[0].split("\n").length;
        int width = image.split("\n\n")[0].split("\n")[0].split(" ").length;

        String[] slices = image.split("\n\n");
        for (int z = 0; z < depth; z++) {
            String slice = slices[z];
            String[] lines = slice.split("\n");
            FloatProcessor fp = new FloatProcessor(width, height);
            float[] array = (float[]) fp.getPixels();

            for (int y = 0; y < lines.length; y++) {
                String line = lines[y];
                String[] numbers = line.split(" ");
                for (int x = 0; x < numbers.length; x++) {
                    array[x + y * width] = Float.parseFloat(numbers[x]);
                }
            }
            stack.addSlice(fp);
        }
        ImagePlus imp = new ImagePlus("", stack);

        ClearCLBuffer converted = clij2.push(imp);
        clij2.copy(converted, buffer);
        converted.close();
        return true;
    }

    public static ClearCLBuffer pushString(CLIJ2 clij2, String image) {
        int depth = image.split("\n\n").length;
        int height = image.split("\n\n")[0].split("\n").length;
        int width = image.split("\n\n")[0].split("\n")[0].split(" ").length;

        ClearCLBuffer buffer;
        if (depth > 1) {
            buffer = clij2.create(new long[]{width, height, depth}, NativeTypeEnum.Float);
        } else {
            buffer = clij2.create(new long[]{width, height}, NativeTypeEnum.Float);
        }
        pushString(clij2, buffer, image);
        return buffer;
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        String image = (String)args[1];
        int depth = image.split("\n\n").length;
        int height = image.split("\n\n")[0].split("\n").length;
        int width = image.split("\n\n")[0].split("\n")[0].split(" ").length;
        if (depth > 1) {
            return clij.create(new long[]{width, height, depth}, NativeTypeEnum.Float);
        } else {
            return clij.create(new long[]{width, height}, NativeTypeEnum.Float);
        }
    }

    @Override
    public String getParameterHelpText() {
        return "Image destination, Array input, Number width, Number height, Number depth";
    }

    @Override
    public String getDescription() {
        return "Converts an string to an image. \n\n" +
                "The formatting works with double line breaks for slice switches, single line breaks for y swithces and \n" +
                "spaces for x. For example this string is converted to an image with width=4, height=3 and depth=2:\n" +
                "\n" +
                "1 2 3 4\n" +
                "5 6 7 8\n" +
                "9 0 1 2\n" +
                "\n" +
                "3 4 5 6\n" +
                "7 8 9 0\n" +
                "1 2 3 4\n";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
