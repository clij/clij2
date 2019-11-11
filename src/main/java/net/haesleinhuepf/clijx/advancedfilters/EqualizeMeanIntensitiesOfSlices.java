package net.haesleinhuepf.clijx.advancedfilters;


import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_equalizeMeanIntensitiesOfSlices")
public class EqualizeMeanIntensitiesOfSlices extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image input, Image destination, Number referenceSlice";
    }

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();
        boolean result = equalizeMeanIntensitiesOfSlices(clij, (ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]), asInteger(args[2]));
        releaseBuffers(args);
        return result;
    }

    public static boolean equalizeMeanIntensitiesOfSlices(CLIJ clij, ClearCLBuffer input, ClearCLBuffer output, Integer referenceSlice) {

        double[] intensities = clij.op().sumPixelsSliceBySlice(input);
        float[] factors = new float[intensities.length];
        for (int i = 0; i < factors.length; i++ ) {
            factors[i] = (float)(intensities[referenceSlice] / intensities[i]);
        }
        clij.op().multiplySliceBySliceWithScalars(input, output, factors);
        return true;
    }

    @Override
    public String getDescription() {
        return "Determines correction factors for each slice so that the average intensity in all slices can be made the same and applies the factors.\n" +
                "This functionality is similar to the 'Simple Ratio Bleaching Correction' in Fiji.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }
}
