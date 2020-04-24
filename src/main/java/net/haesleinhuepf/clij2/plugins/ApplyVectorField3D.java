package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.CLIJUtilities;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_applyVectorField3D")
public class ApplyVectorField3D extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        if (clij.hasImageSupport()) {
            ClearCLBuffer inputBuffer = (ClearCLBuffer)( args[0]);
            ClearCLImage input = getCLIJ2().create(inputBuffer.getDimensions(), CLIJUtilities.nativeToChannelType(inputBuffer.getNativeType()));
            getCLIJ2().copy(inputBuffer, input);

            getCLIJ2().applyVectorField(
                    input,
                    (ClearCLBuffer)( args[1]),
                    (ClearCLBuffer)( args[2]),
                    (ClearCLBuffer)(args[3]),
                    (ClearCLBuffer)(args[4])
            );

            getCLIJ2().release(input);
        } else {
            getCLIJ2().applyVectorField(
                    (ClearCLBuffer)( args[0]),
                    (ClearCLBuffer)( args[1]),
                    (ClearCLBuffer)( args[2]),
                    (ClearCLBuffer)( args[3]),
                    (ClearCLBuffer)(args[4])

            );
        }
        return true;
    }


    public static boolean applyVectorField3D(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface vectorX, ClearCLImageInterface vectorY, ClearCLImageInterface vectorZ, ClearCLImageInterface dst) {
        return applyVectorField(clij2, src, vectorX, vectorY, vectorZ, dst);
    }

    public static boolean applyVectorField(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface vectorX, ClearCLImageInterface vectorY, ClearCLImageInterface vectorZ, ClearCLImageInterface dst) {
        assertDifferent(src, dst);
        assertDifferent(vectorX, dst);
        assertDifferent(vectorY, dst);
        assertDifferent(vectorZ, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("vectorX", vectorX);
        parameters.put("vectorY", vectorY);
        parameters.put("vectorZ", vectorZ);

        if (src instanceof ClearCLImage) {
            clij2.execute(ApplyVectorField3D.class, "apply_vectorfield_3d_interpolate_x.cl", "apply_vectorfield_3d_interpolate", src.getDimensions(), src.getDimensions(), parameters);
        } else {
            clij2.execute(ApplyVectorField3D.class, "apply_vectorfield_3d_x.cl", "apply_vectorfield_3d", src.getDimensions(), src.getDimensions(), parameters);
        }
        return true;
    }


    @Override
    public String getParameterHelpText() {
        return "Image source, Image vectorX, Image vectorY, Image vectorZ, ByRef Image destination";
    }

    @Override
    public String getDescription() {
        return "Deforms an image stack according to distances provided in the given vector image stacks.\n\n" +
                "It is recommended to use 32-bit image stacks for input, output and vector image stacks. ";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }
}
