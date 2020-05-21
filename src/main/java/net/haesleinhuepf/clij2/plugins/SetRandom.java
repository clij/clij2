package net.haesleinhuepf.clij2.plugins;

import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.NewImage;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJHandler;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Random;

/**
 * Author: @haesleinhuepf
 *         March 2020
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_setRandom")
public class SetRandom extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    protected Object[] getDefaultValues() {
        return new Object[]{null, 0, 255, 0};
    }

    @Override
    public boolean executeCL() {

        ClearCLBuffer buffer = (ClearCLBuffer) args[0];
        float minimumValue = asFloat(args[1]);
        float maximumValue = asFloat(args[2]);
        float seed = System.currentTimeMillis();
        if (args.length > 3) {
            seed = asFloat(args[3]);
        }

        getCLIJ2().setRandom(buffer, minimumValue, maximumValue, seed);

        return true;
    }

    public static boolean setRandom(CLIJ2 clij2, ClearCLBuffer target, Float minimumValue, Float maximumValue) {
        return setRandom(clij2, target, minimumValue, maximumValue, (float) System.currentTimeMillis());
    }

    public static boolean setRandom(CLIJ2 clij2, ClearCLBuffer target, Float minimumValue, Float maximumValue, Float seed) {
        ImagePlus imp = NewImage.createFloatImage("", (int)target.getWidth(), (int)target.getHeight(), (int)target.getDepth(), NewImage.FILL_BLACK);
        Random random = new Random(seed.intValue());
        double range = maximumValue - minimumValue;
        for (int z = 0; z < imp.getNSlices(); z++) {
            imp.setZ(z + 1);
            float[] inputArray = (float[]) imp.getProcessor().getPixels();
            for (int i = 0; i < inputArray.length; i++) {
                inputArray[i] = (float)(random.nextFloat() * range + minimumValue);
            }
        }

        ClearCLBuffer buffer = clij2.push(imp);

        if (buffer.getDimension() != target.getDimension()) {
            clij2.copySlice(buffer, target, 0);
        } else {
            clij2.copy(buffer, target);
        }
        clij2.release(buffer);
        return true;

        /*
        long[] dimensions = target.getDimensions();
        long numberOfPixelsPerSlice = dimensions[0] *  dimensions[1];



        if (target.getNativeType() == NativeTypeEnum.UnsignedByte) {
            byte[] inputArray = new byte[(int) numberOfPixelsPerSlice];
            ByteBuffer byteBuffer = ByteBuffer.wrap(inputArray);

            for (int z = 0; z < target.getDepth(); z++) {
                for (int i = 0; i < inputArray.length; i++) {
                    inputArray[i] = (byte)(random.nextFloat() * range + minimumValue);
                }
                target.readFrom(byteBuffer, new long[]{0,0,0}, new long[]{0,0, z}, new long[]{dimensions[0], dimensions[1]}, true);
            }
            return true;
        } else if (target.getNativeType() == NativeTypeEnum.UnsignedShort) {
            short[] inputArray = new short[(int) numberOfPixelsPerSlice];
            ShortBuffer shortBuffer = ShortBuffer.wrap(inputArray);

            for (int z = 0; z < target.getDepth(); z++) {
                for (int i = 0; i < inputArray.length; i++) {
                    inputArray[i] = (short) (random.nextFloat() * range + minimumValue);
                }
                target.readFrom(shortBuffer, new long[]{0,0,0}, new long[]{0,0, z}, new long[]{dimensions[0], dimensions[1]}, true);
            }
            return true;
        } else if (target.getNativeType() == NativeTypeEnum.Float) {
            float[] inputArray = new float[(int) numberOfPixelsPerSlice];
            FloatBuffer floatBuffer = FloatBuffer.wrap(inputArray);

            for (int z = 0; z < target.getDepth(); z++) {
                for (int i = 0; i < inputArray.length; i++) {
                    inputArray[i] = (float) (random.nextFloat() * range + minimumValue);
                }
                target.readFrom(floatBuffer, new long[]{0,0,0}, new long[]{0,0, z}, new long[]{dimensions[0], dimensions[1]}, true);
            }
            return true;
        }

        throw new IllegalArgumentException("Image type " + target.getNativeType() + " not supported for random number generation.");
        */
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Number minimumValue, Number maximumValue, Number seed";
    }

    @Override
    public String getDescription() {
        return "Fills an image or image stack with uniformly distributed random numbers between given minimum and maximum values. \n\n" +
                "Recommendation: For the seed, use getTime().";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
