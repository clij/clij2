package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;
import static net.haesleinhuepf.clij.utilities.CLIJUtilities.radiusToKernelSize;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_varianceBox")
public class VarianceBox extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Image";
    }

    @Override
    public String getOutputType() {
        return "Image";
    }

    @Override
    public String getCategories() {
        return "Filter";
    }

    @Override
    public Object[] getDefaultValues() {
        return new Object[]{null, null, 10, 10, 10};
    }

    @Override
    public boolean executeCL() {
        int radiusX = asInteger(args[2]);
        int radiusY = asInteger(args[3]);
        int radiusZ = asInteger(args[4]);

        return varianceBox(getCLIJ2(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), radiusX, radiusY, radiusZ);
    }

    public static boolean varianceBox(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst, Integer radiusX, Integer radiusY, Integer radiusZ) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", radiusToKernelSize(radiusX));
        parameters.put("Ny", radiusToKernelSize(radiusY));
        if (dst.getDimension() == 3) {
            parameters.put("Nz", radiusToKernelSize(radiusZ));
        }

        clij2.execute(VarianceSphere.class, "variance_box_" + dst.getDimension() + "d_x.cl", "variance_box_" + dst.getDimension() + "d", dst.getDimensions(), dst.getDimensions(), parameters);
        return true;
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        return getCLIJ2().create(input.getDimensions(), NativeTypeEnum.Float);
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Image destination, Number radius_x, Number radius_y, Number radius_z";
    }

    @Override
    public String getDescription() {
        return "Computes the local variance of a pixels box neighborhood. \n\nThe box size is specified by \n" +
                "its half-width, half-height and half-depth (radius). If 2D images are given, radius_z will be ignored. ";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
