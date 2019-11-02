package net.haesleinhuepf.clijx.io;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Author: @haesleinhuepf
 * June 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_readRawImageFromDisc")
public class ReadRawImageFromDisc extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        ClearCLBuffer buffer = (ClearCLBuffer)( args[0]);
        String filename = args[1].toString();

        return readRawImageFromDisc(clij, buffer, filename);
    }

    public static boolean readRawImageFromDisc(CLIJ clij, ClearCLBuffer buffer, String filename) {
        try {
            RandomAccessFile aFile = new RandomAccessFile(filename,"r");
            FileChannel inChannel = aFile.getChannel();
            long fileSize = inChannel.size();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) fileSize);
            inChannel.read(byteBuffer);
            //buffer.rewind();
            byteBuffer.flip();
            buffer.readFrom(byteBuffer, true);
            //for (int i = 0; i < fileSize; i++)
            //{
            //    System.out.print((char) buffer.get());
            //}
            inChannel.close();
            aFile.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        Integer width = asInteger(args[2]);
        Integer height = asInteger(args[3]);
        Integer depth = asInteger(args[4]);
        Integer bitsPerPixel = asInteger(args[5]);
        return createBuffer(clij, width, height, depth, bitsPerPixel);
    }

    private static ClearCLBuffer createBuffer(CLIJ clij, Integer width, Integer height, Integer depth, Integer bitsPerPixel) {
        NativeTypeEnum nativeTypeEnum = null;
        if (bitsPerPixel == 8) {
            nativeTypeEnum = NativeTypeEnum.UnsignedByte;
        } else if (bitsPerPixel == 16) {
            nativeTypeEnum = NativeTypeEnum.UnsignedShort;
        } else { // TODO: add other image types
            nativeTypeEnum = NativeTypeEnum.Float;
        }
        return clij.create(new long[]{width, height, depth}, nativeTypeEnum);
    }

    public static ClearCLBuffer readRawImageFromDisc(CLIJ clij, String filename, Integer width, Integer height, Integer depth, Integer bitsPerPixel) {
        ClearCLBuffer buffer = createBuffer(clij, width, height, depth, bitsPerPixel);
        readRawImageFromDisc(clij, buffer, filename);
        return buffer;
    }

    @Override
    public String getParameterHelpText() {
        return "Image destination, String filename, Number width, Number height, Number depth, Number bitsPerPixel";
    }

    @Override
    public String getDescription() {
        return "Reads a raw file from disc and pushes it immediately to the GPU.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }

}
