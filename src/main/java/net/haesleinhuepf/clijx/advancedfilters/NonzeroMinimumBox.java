package net.haesleinhuepf.clijx.advancedfilters;


import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLKernel;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_nonzeroMinimumDiamond")
public class NonzeroMinimumBox extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image input, Image destination";
    }

    @Override
    public boolean executeCL() {
        ClearCLBuffer input = (ClearCLBuffer) (args[0]);
        ClearCLBuffer output = (ClearCLBuffer) (args[1]);

        ClearCLBuffer flag = getCLIJx().create(new long[]{1,1,1}, output.getNativeType());
        nonzeroMinimumBox(getCLIJx(), input, flag, output, null).close();
        flag.close();
        return true;
    }

    public static boolean nonzeroMinimumBox(CLIJx clijx, ClearCLImageInterface src, ClearCLBuffer flag, ClearCLImageInterface dst) {
        ClearCLKernel kernel = nonzeroMinimumBox(clijx, src, flag, dst, null);
        kernel.close();
        return true;
    }


    public static ClearCLKernel nonzeroMinimumBox(CLIJx clijx, ClearCLImageInterface src, ClearCLBuffer flag, ClearCLImageInterface dst, ClearCLKernel kernel) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("flag_dst", flag);
        parameters.put("dst", dst);

        ClearCLKernel[] workaround = {kernel};
        //ElapsedTime.measureForceOutput("diamondmin", () -> {
            workaround[0] = clijx.executeSubsequently(NonzeroMinimumBox.class, "diamondMorphology" + dst.getDimension() + "d_x.cl", "minimalistic_nonzero_minimum_box_image" + dst.getDimension() +  "d", dst.getDimensions(), dst.getDimensions(), parameters, workaround[0]);
        //});
        return workaround[0];
    }

    @Override
    public String getDescription() {
        return "Apply a minimum-sphere filter to the input image. The radius is fixed to 1 and pixels with value 0 are ignored.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
