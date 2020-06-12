package net.haesleinhuepf.clij2.plugins;


import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_equalizeMeanIntensitiesOfSlices")
public class EqualizeMeanIntensitiesOfSlices extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized {
    @Override
    public String getCategories() {
        return "Math";
    }


    @Override
    protected Object[] getDefaultValues() {
        return new Object[]{null, null, 0};
    }

    @Override
    public String getParameterHelpText() {
        return "Image input, ByRef Image destination, Number referenceSlice";
    }

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();
        boolean result = getCLIJ2().equalizeMeanIntensitiesOfSlices((ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]), asInteger(args[2]));
        releaseBuffers(args);
        return result;
    }

    public static boolean equalizeMeanIntensitiesOfSlices(CLIJ2 clij2, ClearCLBuffer input, ClearCLBuffer output, Integer referenceSlice) {
        double[] intensities = clij2.sumImageSliceBySlice(input);
        float[] factors = new float[intensities.length];
        for (int i = 0; i < factors.length; i++ ) {
            factors[i] = (float)(intensities[referenceSlice] / intensities[i]);
        }
        clij2.multiplyImageStackWithScalars(input, output, factors);
        return true;
    }

    @Override
    public String getDescription() {
        return "Determines correction factors for each z-slice so that the average intensity in all slices can be made " +
                "the same and multiplies these factors with the slices. \n\n" +
                "This functionality is similar to the 'Simple Ratio Bleaching Correction' in Fiji.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }
}
