package net.haesleinhuepf.clij.advancedfilters;


import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_nonzeroMinimum3DDiamond")
public class NonzeroMinimum3DDiamond extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image input, Image destination";
    }

    @Override
    public boolean executeCL() {
        ClearCLBuffer input = (ClearCLBuffer) (args[0]);
        ClearCLBuffer output = (ClearCLBuffer) (args[1]);

        ClearCLBuffer flag = clij.create(new long[]{1,1,1}, output.getNativeType());
        boolean result = nonzeroMinimum3DDiamond(clij, input, flag, output);
        flag.close();
        return result;
    }


    public static boolean nonzeroMinimum3DDiamond(CLIJ clij, ClearCLBuffer src, ClearCLBuffer flag, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("flag_dst", flag);
        parameters.put("dst", dst);
        return clij.execute(ConnectedComponentsLabeling.class, "diamondMorphology.cl", "minimalistic_nonzero_minimum_diamond_image3d", dst.getDimensions(), parameters);
    }


    @Override
    public String getDescription() {
        return "Apply a minimum-sphere filter to the input image. The radius is fixed to 1.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }
}
