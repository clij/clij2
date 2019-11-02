package net.haesleinhuepf.clijx.advancedfilters;


import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_onlyzeroOverwriteMaximumDiamond")
public class OnlyzeroOverwriteMaximumBox extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image input, Image destination";
    }

    @Override
    public boolean executeCL() {
        ClearCLBuffer input = (ClearCLBuffer) (args[0]);
        ClearCLBuffer output = (ClearCLBuffer) (args[1]);

        ClearCLBuffer flag = clij.create(new long[]{1,1,1}, output.getNativeType());
        boolean result = onlyzeroOverwriteMaximumBox(clij, input, flag, output);
        flag.close();
        return result;
    }


    public static boolean onlyzeroOverwriteMaximumBox(CLIJ clij, ClearCLImageInterface src, ClearCLImageInterface flag, ClearCLImageInterface dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("flag_dst", flag);
        parameters.put("dst", dst);
        //ElapsedTime.measureForceOutput("diamondmin", () -> {
            clij.execute(OnlyzeroOverwriteMaximumBox.class, "boxMorphology" + dst.getDimension() +  "d.cl", "onlyzero_overwrite_maximum_box_image" + dst.getDimension() +  "d", dst.getDimensions(), parameters);
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
