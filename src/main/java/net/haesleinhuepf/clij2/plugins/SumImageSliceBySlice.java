package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

import java.nio.FloatBuffer;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_sumImageSliceBySlice")
public class SumImageSliceBySlice extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {


        return sumImageSliceBySlice(getCLIJ2(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]));
    }

    @Deprecated
    public static double[] sumPixelsSliceByslice(CLIJ2 clij2, ClearCLImageInterface src) {
        return sumImageSliceBySlice(clij2, src);
    }

    public static double[] sumImageSliceBySlice(CLIJ2 clij2, ClearCLImageInterface src) {
        ClearCLBuffer intensities = clij2.create(new long[]{src.getDepth(), 1, 1}, NativeTypeEnum.Float);
        sumImageSliceBySlice(clij2, src, intensities);

        float[] array = new float[(int) intensities.getWidth()];
        FloatBuffer fBuffer = FloatBuffer.wrap(array);

        intensities.writeTo(fBuffer, true);
        clij2.release(intensities);

        double[] result = new double[array.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = array[i];
        }
        return result;
    }

    public static boolean sumImageSliceBySlice(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst) {
        assertDifferent(src, dst);

        ClearCLBuffer temp1 = clij2.create(new long[]{src.getDepth(), src.getHeight()});
        clij2.sumXProjection(src, temp1);

        //System.out.println("temp1");
        //clij2.show(temp1, "temp1");
        //clij2.print(temp1);

        clij2.sumYProjection(temp1, dst);

        //System.out.println("dst");
        //clij2.print(dst);

        clij2.release(temp1);
        return true;
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        return getCLIJ2().create(input.getDepth(), 1,1);
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Image destination";
    }

    @Override
    public String getDescription() {
        return "Sums all pixels slice by slice and returns them in an array.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
