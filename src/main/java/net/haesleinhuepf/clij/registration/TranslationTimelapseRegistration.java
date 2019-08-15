package net.haesleinhuepf.clij.registration;


import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_translationTimelapseRegistration")
public class TranslationTimelapseRegistration extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image input, Image output";
    }

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();
        boolean result = translationTimelapseRegistration(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]));
        releaseBuffers(args);
        return result;
    }

    public static boolean translationTimelapseRegistration(CLIJ clij, ClearCLBuffer input, ClearCLBuffer destination) {
        ClearCLBuffer slice1 = clij.create(new long[] {input.getWidth(), input.getHeight()}, input.getNativeType());

        ClearCLBuffer registered = clij.create(slice1);
        double[] center = null;
        for (int t = 0; t < input.getDepth(); t++) {
            clij.op().copySlice(input, slice1, t);

            if (t == 0) {
                center = TranslationRegistration.centerOfMass(clij, slice1);
                clij.op().copySlice(slice1, destination, t);
            } else {
                TranslationRegistration.translationRegistration(clij, slice1, registered, center);
                clij.op().copySlice(registered, destination, t);
            }
        }
        slice1.close();
        registered.close();
        return true;
    }

    @Override
    public String getDescription() {
        return "Applies 2D translation registration to every pair of t, t+1 slices of a 2D+t image stack.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D+t";
    }
}
