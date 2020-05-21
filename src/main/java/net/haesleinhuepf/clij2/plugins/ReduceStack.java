package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 *         May 2020
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_reduceStack")
public class ReduceStack extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {


    @Override
    protected Object[] getDefaultValues() {
        return new Object[]{null, null, 2, 0};
    }

    @Override
    public boolean executeCL() {
        return getCLIJ2().reduceStack( (ClearCLImageInterface)( args[0]), (ClearCLImageInterface)(args[1]), asInteger(args[2]), asInteger(args[3]));
    }

    public static boolean reduceStack(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst, Integer factor, Integer offset) {
        ClearCLBuffer temp = clij2.create(new long[]{src.getWidth(), src.getHeight()}, src.getNativeType());
        int j = 0;
        for (int i = 0; i < src.getDepth(); i += factor) {
            clij2.copySlice(src, temp, i + offset);
            clij2.copySlice(temp, dst, j);
            j++;
        }
        return true;
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        int factor = asInteger(args[2]);
        return getCLIJ2().create(new long[]{input.getWidth(), input.getHeight(), input.getDepth() / factor}, input.getNativeType());
    }

    @Override
    public String getParameterHelpText() {
        return "Image input, ByRef Image destination, Number reductionFactor, Number offset";
    }

    @Override
    public String getDescription() {
        return "Reduces the number of slices in a stack by a given factor.\n" +
                "With the offset you have control which slices stay: \n" +
                "* With factor 3 and offset 0, slices 0, 3, 6,... are kept. " +
                "* With factor 4 and offset 1, slices 1, 5, 9,... are kept.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
