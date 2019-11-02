package net.haesleinhuepf.clijx.advancedfilters;


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

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_nonzeroMaximumDiamond")
public class NonzeroMaximumDiamond extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image input, Image destination";
    }

    @Override
    public boolean executeCL() {
        ClearCLBuffer input = (ClearCLBuffer) (args[0]);
        ClearCLBuffer output = (ClearCLBuffer) (args[1]);

        ClearCLBuffer flag = clij.create(new long[]{1,1,1}, output.getNativeType());
        boolean result = nonzeroMaximumDiamond(clij, input, flag, output);
        flag.close();
        return result;
    }


    public static boolean nonzeroMaximumDiamond(CLIJ clij, ClearCLImageInterface src, ClearCLImageInterface flag, ClearCLImageInterface dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("flag_dst", flag);
        parameters.put("dst", dst);
        //ElapsedTime.measureForceOutput("diamondmin", () -> {
            clij.execute(NonzeroMaximumDiamond.class, "diamondMorphology" + dst.getDimension() +  "d.cl", "minimalistic_nonzero_maximum_diamond_image" + dst.getDimension() +  "d", dst.getDimensions(), parameters);
        //});
        return true;
    }

    public static ClearCLKernel nonzeroMaximumDiamond(CLIJx clijx, ClearCLImageInterface src, ClearCLBuffer flag, ClearCLImageInterface dst, ClearCLKernel kernel) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("flag_dst", flag);
        parameters.put("dst", dst);

        ClearCLKernel[] workaround = {kernel};
        //ElapsedTime.measureForceOutput("diamondmin", () -> {
            workaround[0] = clijx.executeSubsequently(NonzeroMaximumDiamond.class, "diamondMorphology" + dst.getDimension() + "d_x.cl", "minimalistic_nonzero_maximum_diamond_image" + dst.getDimension() +  "d", dst.getDimensions(), dst.getDimensions(), parameters, workaround[0]);
        //});
        return workaround[0];
    }

    @Override
    public String getDescription() {
        return "Apply a maximum-sphere filter to the input image. The radius is fixed to 1 and pixels with value 0 are ignored.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
