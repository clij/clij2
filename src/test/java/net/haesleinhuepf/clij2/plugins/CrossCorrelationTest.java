package net.haesleinhuepf.clij2.plugins;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.NewImage;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clijx.plugins.CrossCorrelation;
import net.imglib2.realtransform.AffineTransform3D;
import org.junit.Test;

/**
 * CrossCorrelationTest
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 03 2019
 */
public class CrossCorrelationTest {
    public static void main(String ... args) {
        new ImageJ();
        ImagePlus imp = IJ.openImage("src/main/resources/blobs.tif");
        IJ.run(imp, "32-bit", "");

        ImagePlus vfXImp = NewImage.createFloatImage("vf", imp.getWidth(), imp.getHeight(), 1, NewImage.FILL_BLACK);
        for (int x = 100; x < 120; x++) {
            for (int y = 100; y < 120; y++) {
                vfXImp.getProcessor().setf(x, y, 3);
            }
        }
        CLIJ clij = CLIJ.getInstance();

        ClearCLBuffer input = clij.push(imp);
        ClearCLBuffer vfYBuffer = clij.push(vfXImp);
        ClearCLBuffer vfYBlurred = clij.create(vfYBuffer);
        ClearCLBuffer vfX = clij.create(input);
        ClearCLBuffer vfY = clij.create(input);
        ClearCLBuffer shifted = clij.create(input);

        Kernels.blur(clij, vfYBuffer, vfYBlurred, 5f, 5f, 5f );

        Kernels.applyVectorfield(clij, input, vfX, vfYBlurred, shifted);


        //Kernels.particleImageVelocimetry2D(clij, input, shifted, vfX, vfY, 5);

        clij.show(vfX, "vfX");
        clij.show(vfY, "vfY");

        input.close();
        vfYBuffer.close();
        vfYBlurred.close();
        vfX.close();
        vfY.close();
        shifted.close();

        IJ.exit();
        clij.dispose();
    }

    @Test
    public void testLocalShift() {
        new ImageJ();
        ImagePlus imp = IJ.openImage("src/test/resources/blobs.tif");
        IJ.run(imp, "32-bit", "");

        ImagePlus vfXImp = NewImage.createFloatImage("vf", imp.getWidth(), imp.getHeight(), 1, NewImage.FILL_BLACK);
        for (int x = 100; x < 120; x++) {
            for (int y = 100; y < 120; y++) {
                vfXImp.getProcessor().setf(x, y, 3);
            }
        }
        CLIJ clij = CLIJ.getInstance();

        ClearCLBuffer input = clij.push(imp);
        ClearCLBuffer vfYBuffer = clij.push(vfXImp);
        ClearCLBuffer vfYBlurred = clij.create(vfYBuffer);
        ClearCLBuffer vfX = clij.create(input);
        ClearCLBuffer shifted = clij.create(input);

        Kernels.blur(clij, vfYBuffer, vfYBlurred, 5f, 5f, 5f );

        Kernels.applyVectorfield(clij, input, vfX, vfYBlurred, shifted);

        // prepare cross-correlation analysis
        int meanRange = 5;
        int scanRange = 1;
        int maxDelta = 3;

        ClearCLBuffer meanInput = clij.create(input);
        ClearCLBuffer meanShifted = clij.create(shifted);

        Kernels.meanBox(clij, input, meanInput, 0, meanRange, 0);
        Kernels.meanBox(clij, shifted, meanShifted, 0, meanRange, 0);

        ClearCLBuffer crossCorrCoeff = clij.create(shifted);
        ClearCLBuffer crossCorrCoeffStack = clij.create(new long[] {shifted.getWidth(), shifted.getHeight(), 2 * maxDelta + 1}, shifted.getNativeType());


        for (int i = -3; i <=3; i++) {
            CrossCorrelation.crossCorrelation(clij, input, meanInput, shifted, meanShifted, crossCorrCoeff, scanRange, i, 1);
            Kernels.copySlice(clij, crossCorrCoeff, crossCorrCoeffStack, i + maxDelta);
        }

        ClearCLBuffer maxProj = clij.create(input);
        ClearCLBuffer argMaxProj = clij.create(input);

        Kernels.argMaximumZProjection(clij, crossCorrCoeffStack, maxProj, argMaxProj);

        clij.show(argMaxProj, "argMaxProj");

        //new WaitForUserDialog("wait").show();


        input.close();
        vfYBuffer.close();
        vfYBlurred.close();
        vfX.close();
        shifted.close();
        maxProj.close();
        argMaxProj.close();

        IJ.exit();
        clij.dispose();
    }

    @Test
    public void testGlobalShift() {
        // https://www2.cscamm.umd.edu/programs/trb10/presentations/PIV.pdf
        new ImageJ();
        ImagePlus imp = IJ.openImage("src/test/resources/blobs.tif");
        IJ.run(imp, "32-bit", "");

        CLIJ clij = CLIJ.getInstance();

        ClearCLBuffer input = clij.push(imp);

        // shift
        ClearCLBuffer shifted = clij.create(input);
        AffineTransform3D at = new AffineTransform3D();
        at.translate(0, 2, 0);

        Kernels.affineTransform3D(clij, input, shifted, at);

        // prepare cross-correlation analysis
        int meanRange = 5;
        int scanRange = 1;
        int maxDelta = 3;

        ClearCLBuffer meanInput = clij.create(input);
        ClearCLBuffer meanShifted = clij.create(shifted);

        Kernels.meanBox(clij, input, meanInput, 0, meanRange, 0);
        Kernels.meanBox(clij, shifted, meanShifted, 0, meanRange, 0);

        ClearCLBuffer crossCorrCoeff = clij.create(shifted);
        ClearCLBuffer crossCorrCoeffStack = clij.create(new long[] {shifted.getWidth(), shifted.getHeight(), 2 * maxDelta + 1}, shifted.getNativeType());


        for (int i = -3; i <=3; i++) {
            CrossCorrelation.crossCorrelation(clij, input, meanInput, shifted, meanShifted, crossCorrCoeff, scanRange, i, 1);
            Kernels.copySlice(clij, crossCorrCoeff, crossCorrCoeffStack, i + maxDelta);
        }

        ClearCLBuffer maxProj = clij.create(input);
        ClearCLBuffer argMaxProj = clij.create(input);

        Kernels.argMaximumZProjection(clij, crossCorrCoeffStack, maxProj, argMaxProj);

        clij.show(argMaxProj, "argMaxProj");


        input.close();
        maxProj.close();
        argMaxProj.close();
        shifted.close();

        IJ.exit();
        clij.dispose();

    }
}
