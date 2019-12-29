package net.haesleinhuepf.clijx.advancedfilters;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import net.haesleinhuepf.clijx.utilities.CLIJUtilities;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_applyVectorField2D")
public class ApplyVectorField2D extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        if (clij.hasImageSupport()) {
            ClearCLBuffer inputBuffer = (ClearCLBuffer)( args[0]);
            ClearCLImage input = getCLIJx().create(inputBuffer.getDimensions(), CLIJUtilities.nativeToChannelType(inputBuffer.getNativeType()));
            getCLIJx().copy(inputBuffer, input);

            applyVectorfield(getCLIJx(),
                    input,
                    (ClearCLBuffer)( args[1]),
                    (ClearCLBuffer)( args[2]),
                    (ClearCLBuffer)(args[3])
            );

            getCLIJx().release(input);
        } else {
            applyVectorfield(getCLIJx(),
                    (ClearCLBuffer)( args[0]),
                    (ClearCLBuffer)( args[1]),
                    (ClearCLBuffer)( args[2]),
                    (ClearCLBuffer)(args[3])
            );
        }
        return true;
    }

    public static boolean applyVectorfield2D(CLIJx clijx, ClearCLImageInterface src, ClearCLImageInterface vectorX, ClearCLImageInterface vectorY, ClearCLImageInterface dst) {
        return applyVectorfield(clijx, src, vectorX, vectorY, dst);
    }


    public static boolean applyVectorfield(CLIJx clijx, ClearCLImageInterface src, ClearCLImageInterface vectorX, ClearCLImageInterface vectorY, ClearCLImageInterface dst) {
        assertDifferent(src, dst);
        assertDifferent(vectorX, dst);
        assertDifferent(vectorY, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("vectorX", vectorX);
        parameters.put("vectorY", vectorY);

        if (src instanceof ClearCLImage) {
            clijx.execute(ApplyVectorField2D.class, "apply_vectorfield_2d_interpolate_x.cl", "apply_vectorfield_2d_interpolate", src.getDimensions(), src.getDimensions(), parameters);
        } else {
            clijx.execute(ApplyVectorField2D.class, "apply_vectorfield_2d_x.cl", "apply_vectorfield_2d", src.getDimensions(), src.getDimensions(), parameters);
        }
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image vectorX, Image vectorY, Image destination";
    }

    @Override
    public String getDescription() {
        return "Deforms an image according to distances provided in the given vector images. " +
                "It is recommended to use 32-bit images for input, output and vector images. ";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }
}
