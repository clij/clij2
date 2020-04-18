package net.haesleinhuepf.clij2.plugins;


import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLKernel;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_onlyzeroOverwriteMaximumBox")
public class OnlyzeroOverwriteMaximumBox extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image input, ByRef Image destination";
    }

    @Override
    public boolean executeCL() {
        ClearCLBuffer input = (ClearCLBuffer) (args[0]);
        ClearCLBuffer output = (ClearCLBuffer) (args[1]);

        ClearCLBuffer flag = clij.create(new long[]{1,1,1}, output.getNativeType());
        boolean result = onlyzeroOverwriteMaximumBox(getCLIJ2(), input, flag, output);
        flag.close();
        return result;
    }

    public static ClearCLKernel onlyzeroOverwriteMaximumBox(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface flag, ClearCLImageInterface dst, ClearCLKernel kernel) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("flag_dst", flag);
        parameters.put("dst", dst);
        //ElapsedTime.measureForceOutput("diamondmin", () -> {
        kernel = clij2.executeSubsequently(OnlyzeroOverwriteMaximumBox.class, "onlyzero_overwrite_maximum_box_" + dst.getDimension() +  "d_x.cl", "onlyzero_overwrite_maximum_box_" + dst.getDimension() +  "d", dst.getDimensions(), dst.getDimensions(), parameters, kernel);
        //});
        return kernel;
    }

    public static boolean onlyzeroOverwriteMaximumBox(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface flag, ClearCLImageInterface dst) {
        ClearCLKernel kernel = onlyzeroOverwriteMaximumBox(clij2, src, flag, dst, null);
        kernel.close();
        return true;
    }

    @Override
    public String getDescription() {
        return "Apply a local maximum filter to an image which only overwrites pixels with value 0.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
