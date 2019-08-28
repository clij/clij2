package net.haesleinhuepf.clij.advancedfilters;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.WaitForUserDialog;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.advancedmath.Equal;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.matrix.MatrixEqual;
import org.junit.Test;

import static org.junit.Assert.*;

public class GaussianBlurTest {
    @Test
    public void blurBuffers() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus blobs = IJ.openImage("src/test/resources/blobs.tif");

        ClearCLBuffer input = clij.push(blobs);
        ClearCLBuffer output = clij.create(input);
        ClearCLBuffer reference = clij.create(input);
        //System.out.println("height before: " + output.getHeight());

        for (int i = 0; i < 10; i++) {
            float sigmaX = i;
            float sigmaY = i;
            float sigmaZ = 0;

            long timeBefore = System.currentTimeMillis();
            GaussianBlur.gaussianBlur(clij, input, output, sigmaX, sigmaY, sigmaZ);
            System.out.println("New blur took: " + (System.currentTimeMillis() - timeBefore) + " ms");

            timeBefore = System.currentTimeMillis();
            clij.op().blur(input, reference, sigmaX, sigmaY, sigmaZ);
            System.out.println("Old blur took: " + (System.currentTimeMillis() - timeBefore) + " ms");

            assertTrue(MatrixEqual.matrixEqual(clij, output, reference, 0f));
        }
        //System.out.println("height after: " + output.getHeight());
        //new ImageJ();
        //clij.show(output, "output");
        //new WaitForUserDialog("dlg").show();

        input.close();
        output.close();
    }

    @Test
    public void blurImages() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus blobs = IJ.openImage("src/test/resources/blobs.tif");

        ClearCLImage input = clij.convert(blobs, ClearCLImage.class);
        ClearCLImage output = clij.create(input);
        ClearCLImage reference = clij.create(input);
        //System.out.println("height before: " + output.getHeight());

        for (int i = 0; i < 10; i++) {
            float sigmaX = i;
            float sigmaY = i;
            float sigmaZ = 0;

            long timeBefore = System.currentTimeMillis();
            GaussianBlur.gaussianBlur(clij, input, output, sigmaX, sigmaY, sigmaZ);
            System.out.println("New blur took: " + (System.currentTimeMillis() - timeBefore) + " ms");

            timeBefore = System.currentTimeMillis();
            clij.op().blur(input, reference, sigmaX, sigmaY, sigmaZ);
            System.out.println("Old blur took: " + (System.currentTimeMillis() - timeBefore) + " ms");

            //assertTrue(MatrixEqual.matrixEqual(clij, output, reference, 0f));
        }
        //System.out.println("height after: " + output.getHeight());
        //new ImageJ();
        //clij.show(output, "output");
        //new WaitForUserDialog("dlg").show();

        input.close();
        output.close();
    }
}