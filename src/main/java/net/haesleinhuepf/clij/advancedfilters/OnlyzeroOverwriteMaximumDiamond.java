package net.haesleinhuepf.clij.advancedfilters;


import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLKernel;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_onlyzeroOverwriteMaximumDiamond")
public class OnlyzeroOverwriteMaximumDiamond extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image input, Image destination";
    }

    @Override
    public boolean executeCL() {
        ClearCLBuffer input = (ClearCLBuffer) (args[0]);
        ClearCLBuffer output = (ClearCLBuffer) (args[1]);

        ClearCLBuffer flag = clij.create(new long[]{1,1,1}, output.getNativeType());
        boolean result = onlyzeroOverwriteMaximumDiamond(clij, input, flag, output);
        flag.close();
        return result;
    }


    public static boolean onlyzeroOverwriteMaximumDiamond(CLIJ clij, ClearCLImageInterface src, ClearCLImageInterface flag, ClearCLImageInterface dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("flag_dst", flag);
        parameters.put("dst", dst);
        //ElapsedTime.measureForceOutput("diamondmin", () -> {
            clij.execute(OnlyzeroOverwriteMaximumDiamond.class, "diamondMorphology" + dst.getDimension() +  "d.cl", "onlyzero_overwrite_maximum_diamond_image" + dst.getDimension() +  "d", dst.getDimensions(), parameters);
        //});
        return true;
    }

    @Override
    public String getDescription() {
        return "TODO";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
