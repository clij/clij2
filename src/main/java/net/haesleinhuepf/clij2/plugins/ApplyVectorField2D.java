package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.clearcl.enums.ImageChannelDataType;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.CLIJUtilities;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_applyVectorField2D")
public class ApplyVectorField2D extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Image";
    }

    @Override
    public String getOutputType() {
        return "Image";
    }


    @Override
    public boolean executeCL() {
        if (clij.hasImageSupport()) {
            ClearCLBuffer inputBuffer = (ClearCLBuffer)( args[0]);
            ClearCLImage input = getCLIJ2().create(inputBuffer.getDimensions(), ImageChannelDataType.Float);
            getCLIJ2().copy(inputBuffer, input);

            getCLIJ2().applyVectorField(
                    input,
                    (ClearCLBuffer)( args[1]),
                    (ClearCLBuffer)( args[2]),
                    (ClearCLBuffer)(args[3])
            );

            getCLIJ2().release(input);
        } else {
            getCLIJ2().applyVectorField(
                    (ClearCLBuffer)( args[0]),
                    (ClearCLBuffer)( args[1]),
                    (ClearCLBuffer)( args[2]),
                    (ClearCLBuffer)(args[3])
            );
        }
        return true;
    }

    public static boolean applyVectorField2D(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface vectorX, ClearCLImageInterface vectorY, ClearCLImageInterface dst) {
        return applyVectorField(clij2, src, vectorX, vectorY, dst);
    }


    public static boolean applyVectorField(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface vectorX, ClearCLImageInterface vectorY, ClearCLImageInterface dst) {
        assertDifferent(src, dst);
        assertDifferent(vectorX, dst);
        assertDifferent(vectorY, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("vectorX", vectorX);
        parameters.put("vectorY", vectorY);

        if (src instanceof ClearCLImage) {
            clij2.execute(ApplyVectorField2D.class, "apply_vectorfield_2d_interpolate_x.cl", "apply_vectorfield_2d_interpolate", src.getDimensions(), src.getDimensions(), parameters);
        } else {
            clij2.execute(ApplyVectorField2D.class, "apply_vectorfield_2d_x.cl", "apply_vectorfield_2d", src.getDimensions(), src.getDimensions(), parameters);
        }
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image vectorX, Image vectorY, ByRef Image destination";
    }

    @Override
    public String getDescription() {
        return "Deforms an image according to distances provided in the given vector images.\n\n " +
                "It is recommended to use 32-bit images for input, output and vector images. ";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }

    @Override
    public String getCategories() {
        return "Transform";
    }
}
