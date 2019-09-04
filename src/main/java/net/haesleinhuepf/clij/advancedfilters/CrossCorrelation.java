package net.haesleinhuepf.clij.advancedfilters;


import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_crossCorrelation")
public class CrossCorrelation extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image input1, Image meanInput1, Image input2, Image meanInput2, Image destination, Number radius, Number deltaPos, Number dimension";
    }

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();
        boolean result = crossCorrelation(clij,
                (ClearCLBuffer) (args[0]),
                (ClearCLBuffer) (args[1]),
                (ClearCLBuffer) (args[2]),
                (ClearCLBuffer) (args[3]),
                (ClearCLBuffer) (args[4]),
                asInteger(args[5]),
                asInteger(args[6]),
                asInteger(args[7]));
        releaseBuffers(args);
        return result;
    }

    public static boolean crossCorrelation(CLIJ clij, ClearCLBuffer src1, ClearCLBuffer meanSrc1, ClearCLBuffer src2, ClearCLBuffer meanSrc2, ClearCLBuffer dst, int radius, int deltaPos, int dimension) {
        assertDifferent(src1, dst);
        assertDifferent(src2, dst);
        assertDifferent(meanSrc1, dst);
        assertDifferent(meanSrc2, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src1", src1);
        parameters.put("mean_src1", meanSrc1);
        parameters.put("src2", src2);
        parameters.put("mean_src2", meanSrc2);
        parameters.put("dst", dst);
        parameters.put("radius", radius);
        parameters.put("i", deltaPos);
        parameters.put("dimension", dimension);
        return clij.execute(CrossCorrelation.class, "cross_correlation.cl", "cross_correlation_3d", parameters);
    }

    public static boolean crossCorrelation(CLIJ clij, ClearCLImage src1, ClearCLImage meanSrc1, ClearCLImage src2, ClearCLImage meanSrc2, ClearCLImage dst, int radius, int deltaPos, int dimension) {
        assertDifferent(src1, dst);
        assertDifferent(src2, dst);
        assertDifferent(meanSrc1, dst);
        assertDifferent(meanSrc2, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src1", src1);
        parameters.put("mean_src1", meanSrc1);
        parameters.put("src2", src2);
        parameters.put("mean_src2", meanSrc2);
        parameters.put("dst", dst);
        parameters.put("radius", radius);
        parameters.put("i", deltaPos);
        parameters.put("dimension", dimension);
        return clij.execute(CrossCorrelation.class, "cross_correlation.cl", "cross_correlation_3d", parameters);
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
