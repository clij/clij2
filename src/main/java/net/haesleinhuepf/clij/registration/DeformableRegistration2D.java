package net.haesleinhuepf.clij.registration;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij.piv.ParticleImageVelocimetry;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_deformableRegistration2D")
public class DeformableRegistration2D extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image input1, Image input2, Image destination, Number maxDeltaX, Number maxDeltaY";
    }

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();
        boolean result = deformableRegister(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), (ClearCLBuffer)(args[2]), asInteger(args[3]), asInteger(args[4]));
        releaseBuffers(args);
        return result;
    }

    public static boolean deformableRegister(CLIJ clij, ClearCLBuffer input1, ClearCLBuffer input2, ClearCLBuffer output, int maxDeltaX, int maxDeltaY) {
        ClearCLBuffer vectorfieldX = clij.create(input1.getDimensions(), NativeTypeEnum.Float);
        ClearCLBuffer vectorfieldY = clij.create(vectorfieldX);
        ClearCLBuffer tempX = clij.create(vectorfieldX);
        ClearCLBuffer tempY = clij.create(vectorfieldX);

        ParticleImageVelocimetry.particleImageVelocimetry(clij, input1, input2, vectorfieldX, vectorfieldY, tempX, maxDeltaX, maxDeltaY, 0, false);

        clij.op().blur(vectorfieldX, tempX, (float)maxDeltaX, (float)maxDeltaY);
        clij.op().blur(vectorfieldY, tempY, (float)maxDeltaX, (float)maxDeltaY);

        clij.op().applyVectorfield(input2, tempX, tempY, output);

        vectorfieldX.close();
        vectorfieldY.close();
        tempX.close();
        tempY.close();

        return true;
    }

    @Override
    public String getDescription() {
        return "Applies particle image velocimetry to two images and registers them afterwards by warping input image 2 with a smoothed vector field.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D+t";
    }
}
