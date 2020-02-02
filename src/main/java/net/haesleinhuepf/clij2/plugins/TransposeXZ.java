package net.haesleinhuepf.clij2.plugins;


import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_transposeXZ")
public class TransposeXZ extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image input, Image destination";
    }

    @Override
    public boolean executeCL() {
        return transposeXZ(getCLIJ2(), (ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]));
    }

    public static boolean transposeXZ(CLIJ2 clij2, ClearCLBuffer input, ClearCLBuffer output) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("src", input);
        parameters.put("dst", output);
        clij2.execute(TransposeXY.class, "transpose_xz_" + input.getDimension() + "d_x.cl", "transpose_xz_" + input.getDimension() + "d", output.getDimensions(), output.getDimensions(), parameters);
        return true;
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        return clij.create(new long[]{input.getDepth(), input.getHeight(), input.getWidth()}, input.getNativeType());
    }

    @Override
    public String getDescription() {
        return "Transpose X and Z axes of an image.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }
}
