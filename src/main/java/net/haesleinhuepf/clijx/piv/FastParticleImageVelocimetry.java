package net.haesleinhuepf.clijx.piv;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clijx.advancedfilters.CrossCorrelation;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_fastParticleImageVelocimetry")
public class FastParticleImageVelocimetry extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        if (containsCLImageArguments()) {
            return particleImageVelocimetry2D(clij, (ClearCLImage)( args[0]), (ClearCLImage)(args[1]), (ClearCLImage)(args[2]), (ClearCLImage)(args[3]), asInteger(args[4]));
        } else {
            Object[] args = openCLBufferArgs();
            boolean result = particleImageVelocimetry2D(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), (ClearCLBuffer)(args[2]), (ClearCLBuffer)(args[3]), asInteger(args[4]));
            releaseBuffers(args);
            return result;
        }
    }

    @Override
    public String getParameterHelpText() {
        return "Image source1, Image source2, Image destinationDeltaX, Image destinationDeltaY, Number maxDelta";
    }

    @Override
    public String getDescription() {
        return "For every pixel in source image 1, determine the pixel with the most similar intensity in \n" +
                " the local neighborhood with a given radius in source image 2. Write the distance in \n" +
                "X and Y in the two corresponding destination images.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }

    public boolean particleImageVelocimetry2D( ClearCLBuffer input1,  ClearCLBuffer input2,  ClearCLBuffer vfX,  ClearCLBuffer vfY,  Integer maxDelta  ) {
        return particleImageVelocimetry2D(clij, input1, input2, vfX, vfY, maxDelta);
    }

    public boolean particleImageVelocimetry2D( ClearCLImage input1,  ClearCLImage input2,  ClearCLImage vfX,  ClearCLImage vfY,  Integer maxDelta  ) {
        return particleImageVelocimetry2D(clij, input1, input2, vfX, vfY, maxDelta);
    }

    public static boolean particleImageVelocimetry2D(CLIJ clij, ClearCLBuffer input1, ClearCLBuffer input2, ClearCLBuffer vfX, ClearCLBuffer vfY, Integer maxDelta ) {
        // prepare cross-correlation analysis
        int meanRange = maxDelta + 1;
        int scanRange = 5; // has influence on precision / correctness

        ClearCLBuffer meanInput1 = clij.create(input1);
        ClearCLBuffer meanInput2 = clij.create(input2);


        ClearCLBuffer crossCorrCoeff = clij.create(input1);
        ClearCLBuffer crossCorrCoeffStack = clij.create(new long[] {input1.getWidth(), input1.getHeight(), 2 * maxDelta + 1}, input1.getNativeType());

        // analyse shift in X
        Kernels.meanBox(clij, input1, meanInput1, meanRange, 0, 0);
        Kernels.meanBox(clij, input2, meanInput2, meanRange, 0, 0);
        analyseShift(clij, input1, input2, vfX, maxDelta, scanRange, meanInput1, meanInput2, crossCorrCoeff, crossCorrCoeffStack, 0);

        Kernels.meanBox(clij, input1, meanInput1, 0, meanRange, 0);
        Kernels.meanBox(clij, input2, meanInput2, 0, meanRange, 0);
        analyseShift(clij, input1, input2, vfY, maxDelta, scanRange, meanInput1, meanInput2, crossCorrCoeff, crossCorrCoeffStack, 1);

        meanInput1.close();
        meanInput2.close();

        crossCorrCoeff.close();
        crossCorrCoeffStack.close();

        return true;
    }

    public static boolean particleImageVelocimetry2D(CLIJ clij, ClearCLImage input1, ClearCLImage input2, ClearCLImage vfX, ClearCLImage vfY, Integer maxDelta ) {
        // prepare cross-correlation analysis
        int meanRange = maxDelta + 1;
        int scanRange = 1; // has influence on precision / correctness

        ClearCLImage meanInput1 = clij.create(input1);
        ClearCLImage meanInput2 = clij.create(input2);


        ClearCLImage crossCorrCoeff = clij.create(input1);
        ClearCLImage crossCorrCoeffStack = clij.create(new long[] {input1.getWidth(), input1.getHeight(), 2 * maxDelta + 1}, input1.getChannelDataType());

        // analyse shift in X
        Kernels.meanBox(clij, input1, meanInput1, meanRange, 0, 0);
        Kernels.meanBox(clij, input2, meanInput2, meanRange, 0, 0);
        analyseShift(clij, input1, input2, vfX, maxDelta, scanRange, meanInput1, meanInput2, crossCorrCoeff, crossCorrCoeffStack, 0);

        Kernels.meanBox(clij, input1, meanInput1, 0, meanRange, 0);
        Kernels.meanBox(clij, input2, meanInput2, 0, meanRange, 0);
        analyseShift(clij, input1, input2, vfY, maxDelta, scanRange, meanInput1, meanInput2, crossCorrCoeff, crossCorrCoeffStack, 1);

        meanInput1.close();
        meanInput2.close();

        crossCorrCoeff.close();
        crossCorrCoeffStack.close();

        return true;
    }

    private static void analyseShift(CLIJ clij, ClearCLBuffer input1, ClearCLBuffer input2, ClearCLBuffer vf, int maxDelta, int scanRange, ClearCLBuffer meanInput1, ClearCLBuffer meanInput2, ClearCLBuffer crossCorrCoeff, ClearCLBuffer crossCorrCoeffStack, int dimension) {
        for (int i = -maxDelta; i <=maxDelta; i++) {
            CrossCorrelation.crossCorrelation(clij, input1, meanInput1, input2, meanInput2, crossCorrCoeff, scanRange, i, dimension);
            Kernels.copySlice(clij, crossCorrCoeff, crossCorrCoeffStack, i + maxDelta);
        }

        ClearCLBuffer argMaxProj = clij.create(input1);

        Kernels.argMaximumZProjection(clij, crossCorrCoeffStack, vf, argMaxProj);
        Kernels.addImageAndScalar(clij, argMaxProj, vf, new Float(-maxDelta));
        argMaxProj.close();
    }

    private static void analyseShift(CLIJ clij, ClearCLImage input1, ClearCLImage input2, ClearCLImage vf, int maxDelta, int scanRange, ClearCLImage meanInput1, ClearCLImage meanInput2, ClearCLImage crossCorrCoeff, ClearCLImage crossCorrCoeffStack, int dimension) {
        for (int i = -maxDelta; i <=maxDelta; i++) {
            CrossCorrelation.crossCorrelation(clij, input1, meanInput1, input2, meanInput2, crossCorrCoeff, scanRange, i, dimension);
            Kernels.copySlice(clij, crossCorrCoeff, crossCorrCoeffStack, i + maxDelta);
        }

        ClearCLImage argMaxProj = clij.create(input1);

        Kernels.argMaximumZProjection(clij, crossCorrCoeffStack, vf, argMaxProj);
        Kernels.addImageAndScalar(clij, argMaxProj, vf, new Float(-maxDelta));
        argMaxProj.close();
    }

}
