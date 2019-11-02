package net.haesleinhuepf.clijx.piv;

import ij.ImagePlus;
import ij.gui.NewImage;
import ij.process.ImageProcessor;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.registration.DeformableRegistration2D;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

/**
 * Author: @haesleinhuepf
 * June 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_particleImageVelocimetry")
public class ParticleImageVelocimetry extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();
        boolean result = particleImageVelocimetry(clij, (ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]), (ClearCLBuffer) (args[2]), (ClearCLBuffer) (args[3]), (ClearCLBuffer) (args[4]), asInteger(args[5]), asInteger(args[6]), asInteger(args[7]), asBoolean(args[8]));
        releaseBuffers(args);
        return result;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source1, Image source2, Image destinationDeltaX, Image destinationDeltaY, Image destinationDeltaZ, Number maxDeltaX, Number maxDeltaY, Number maxDeltaZ, Boolean correctLocalShift";
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

    public static boolean particleImageVelocimetry(CLIJ clij, ClearCLBuffer input1, ClearCLBuffer input2, ClearCLBuffer vfX, ClearCLBuffer vfY, ClearCLBuffer vfZ, Integer maxDeltaX, Integer maxDeltaY, Integer maxDeltaZ, boolean correctLocalShift) {
        // prepare cross-correlation analysis
        int meanRangeX = 3;
        int scanRangeX = meanRangeX; // has influence on precision / correctness
        int meanRangeY = 3;
        int scanRangeY = meanRangeY; // has influence on precision / correctness
        int meanRangeZ = 0;
        int scanRangeZ = meanRangeZ; // has influence on precision / correctness

        if (correctLocalShift) {
            ClearCLBuffer copy = clij.create(input2);
            clij.op().copy(input2, copy);

            DeformableRegistration2D.deformableRegistration2D(clij, input1, copy, input2, maxDeltaX, maxDeltaY);
            copy.close();
        }

        ClearCLBuffer meanInput1 = clij.create(input1);
        ClearCLBuffer meanInput2 = clij.create(input2);


        ClearCLBuffer crossCorrCoeff = clij.create(input1);
        ClearCLBuffer crossCorrCoeffStack = clij.create(new long[]{input1.getWidth(), input1.getHeight(), (2 * maxDeltaX + 1) * (2 * maxDeltaY + 1) * (2 * maxDeltaZ + 1)}, input1.getNativeType());

        // analyse shift in X
        Kernels.meanBox(clij, input1, meanInput1, meanRangeX, meanRangeY, meanRangeZ);
        Kernels.meanBox(clij, input2, meanInput2, meanRangeX, meanRangeY, meanRangeZ);
        analyseShift(clij, input1, input2, vfX, vfY, vfZ, maxDeltaX, maxDeltaY, maxDeltaZ, scanRangeX, scanRangeY, scanRangeZ, meanInput1, meanInput2, crossCorrCoeff, crossCorrCoeffStack);


        meanInput1.close();
        meanInput2.close();

        crossCorrCoeff.close();
        crossCorrCoeffStack.close();

        return true;
    }

    private static void analyseShift(CLIJ clij, ClearCLBuffer input1, ClearCLBuffer input2, ClearCLBuffer vfX, ClearCLBuffer vfY, ClearCLBuffer vfZ, int maxDeltaX, int maxDeltaY, int maxDeltaZ, int scanRangeX, int scanRangeY, int scanRangeZ, ClearCLBuffer meanInput1, ClearCLBuffer meanInput2, ClearCLBuffer crossCorrCoeff, ClearCLBuffer crossCorrCoeffStack) {

        int count = 0;
        double[][] coords = new double[(int)crossCorrCoeffStack.getDepth()][3];
        for (int ix = -maxDeltaX; ix <= maxDeltaX; ix++) {
            for (int iy = -maxDeltaY; iy <= maxDeltaY; iy++) {
                for (int iz = -maxDeltaZ; iz <= maxDeltaZ; iz++) {
                    crossCorrelation(clij, input1, meanInput1, input2, meanInput2, crossCorrCoeff, scanRangeX, scanRangeY, scanRangeZ, ix, iy, iz);
                    //System.out.println("ccc " + crossCorrCoeff.getDimension());
                    //System.out.println("cccs " + crossCorrCoeffStack.getDimension());
                    Kernels.copySlice(clij, crossCorrCoeff, crossCorrCoeffStack, count);
                    coords[count][0] = ix;
                    coords[count][1] = iy;
                    coords[count][2] = iz;
                    count++;
                }
            }
        }

        ClearCLBuffer coordMap = doubleArrayToImagePlus(clij, coords);
        ClearCLBuffer argMaxProj = clij.create(input1);

        Kernels.argMaximumZProjection(clij, crossCorrCoeffStack, vfX, argMaxProj);
        indexProjection(clij, argMaxProj, coordMap, vfX, 0, 0, 1, 0, 2);

        Kernels.argMaximumZProjection(clij, crossCorrCoeffStack, vfY, argMaxProj);
        indexProjection(clij, argMaxProj, coordMap, vfY, 0, 1, 1, 0, 2);

        Kernels.argMaximumZProjection(clij, crossCorrCoeffStack, vfZ, argMaxProj);
        indexProjection(clij, argMaxProj, coordMap, vfZ, 0, 2, 1, 0, 2);

        argMaxProj.close();
    }

    private static boolean indexProjection(CLIJ clij, ClearCLBuffer index, ClearCLBuffer indexMap, ClearCLBuffer target, int indexDimension,
                                   int fixed1, int fixedDimension1, int fixed2, int fixedDimension2) {

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("index_src1", index);
        parameters.put("index_map_src1", indexMap);
        parameters.put("dst", target);
        parameters.put("indexDimension", indexDimension);
        parameters.put("fixed1", fixed1);
        parameters.put("fixedDimension1", fixedDimension1);
        parameters.put("fixed2", fixed2);
        parameters.put("fixedDimension2", fixedDimension2);

        return clij.execute(ParticleImageVelocimetry.class, "piv.cl", "index_projection_3d", parameters);
    }

    private static ClearCLBuffer doubleArrayToImagePlus(CLIJ clij, double[][] coords) {
        ImagePlus imp = NewImage.createFloatImage("img", coords.length, coords[0].length, 1, NewImage.FILL_BLACK);
        ImageProcessor ip = imp.getProcessor();
        for (int x = 0; x < coords.length; x++) {
            for (int y = 0; y < coords[0].length; y++) {
                ip.setf(x, y, (float)coords[x][y]);
            }
        }
        return clij.push(imp);
    }

    private static boolean crossCorrelation(CLIJ clij, ClearCLBuffer input1, ClearCLBuffer meanInput1, ClearCLBuffer input2, ClearCLBuffer meanInput2, ClearCLBuffer crossCorrCoeff, int scanRangeX, int scanRangeY, int scanRangeZ, int ix, int iy, int iz) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src1", input1);
        parameters.put("mean_src1", meanInput1);
        parameters.put("src2", input2);
        parameters.put("mean_src2", meanInput2);
        parameters.put("dst", crossCorrCoeff);
        parameters.put("radiusx", scanRangeX);
        parameters.put("radiusy", scanRangeY);
        parameters.put("radiusz", scanRangeZ);
        parameters.put("ix", ix);
        parameters.put("iy", iy);
        parameters.put("iz", iz);

        return clij.execute(ParticleImageVelocimetry.class, "piv.cl", "cross_correlation_3d", parameters);
    }
}
