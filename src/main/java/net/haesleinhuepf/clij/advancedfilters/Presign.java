package net.haesleinhuepf.clij.advancedfilters;


import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_presign")
public class Presign extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image input, Image destination";
    }

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();
        boolean result = presign(clij, (ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]));
        releaseBuffers(args);
        return result;
    }

    public static boolean presign(CLIJ clij, ClearCLBuffer input, ClearCLBuffer output) {

        ClearCLBuffer temp1 = clij.create(input);

        clij.op().absolute(input, temp1);
        clij.op().divideImages(input, temp1, output);

        temp1.close();
        return true;
    }

    @Override
    public String getDescription() {
        return "Determines the extrema of pixel values: f(x) = x / abs(x).";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
