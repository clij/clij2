package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

import java.nio.FloatBuffer;
import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;
import static net.haesleinhuepf.clij2.utilities.CLIJUtilities.checkDimensions;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_multiplyImageStackWithScalars")
public class MultiplyImageStackWithScalars extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized {
    @Override
    public String getCategories() {
        return "Math";
    }

    @Override
    public boolean executeCL() {

        Object[] array = (Object[]) args[2];
        float[] scalars = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            scalars[i] = asFloat(array[i]);
        }

        return getCLIJ2().multiplyImageStackWithScalars((ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), scalars);
    }

    @Deprecated
    public static boolean multiplySliceBySliceWithScalars(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst, float[] scalars) {
        return multiplyImageStackWithScalars(clij2, src, dst, scalars);
    }

    public static boolean multiplyImageStackWithScalars(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst, float[] scalars) {
        FloatBuffer buffer = FloatBuffer.wrap(scalars);

        ClearCLBuffer clBuffer = clij2.create(new long[]{scalars.length, 1, 1}, NativeTypeEnum.Float);
        clBuffer.readFrom(buffer, true);

        boolean result =  multiplyImageStackWithScalars(clij2, src, dst, clBuffer);
        clij2.release(clBuffer);
        return result;
    }

    public static boolean multiplyImageStackWithScalars(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst, ClearCLBuffer scalar_list) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("src_scalar_list", scalar_list);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match!");
        }
        clij2.execute(MultiplyImageStackWithScalars.class, "multiply_stack_with_scalars_x.cl", "multiply_stack_with_scalars", dst.getDimensions(), dst.getDimensions(), parameters);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Image destination, Array scalars";
    }

    @Override
    public String getDescription() {
        return "Multiplies all pixels value x in a given image X with a constant scalar s from a list of scalars.\n\n<pre>f(x, s) = x * s</pre>";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
