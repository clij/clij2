package net.haesleinhuepf.clij2.plugins;

import ij.ImageJ;
import ij.gui.WaitForUserDialog;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.util.ElapsedTime;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clijx.plugins.MatrixEqual;

import static org.junit.Assert.*;

public class MultiplyMatrixTest {

    public static void main(String... args) {

        for (int i = 0; i < 3; i++) {
            CLIJ2 clij2 = CLIJ2.getInstance();

            int size1 = 2000;
            int size2 = 2000;

            ClearCLBuffer mat1 = clij2.create(new long[]{size1, size2});
            ClearCLBuffer mat2 = clij2.create(new long[]{size2, size1});
            ClearCLBuffer matR = clij2.create(new long[]{size2, size2});
            ClearCLBuffer matRfx = clij2.create(new long[]{size2, size2});
            ClearCLBuffer matRfy = clij2.create(new long[]{size2, size2});

            clij2.drawBox(mat1, 10, 10, 25, 35);
            clij2.drawBox(mat2, 5, 5, 15, 35);

            ElapsedTime.measureForceOutput("matR", () -> {
                clij2.multiplyMatrix(mat1, mat2, matR);

            });
            ElapsedTime.measureForceOutput("matRfx", () -> {
                MultiplyMatrix.multiplyMatrix_fast_x(clij2, mat1, mat2, matRfx);
            });
            ElapsedTime.measureForceOutput("matRfy", () -> {
                MultiplyMatrix.multiplyMatrix_fast_x(clij2, mat1, mat2, matRfy);
            });
            System.out.println("Equal R Rfx: " + MatrixEqual.matrixEqual(clij2.getCLIJ(), matR, matRfx, 0.0f));
            System.out.println("Equal R Rfy: " + MatrixEqual.matrixEqual(clij2.getCLIJ(), matR, matRfy, 0.0f));

            new ImageJ();

            clij2.show(matR, "matR");
            clij2.show(matRfx, "matRfx");
            clij2.show(matRfy, "matRfy");

            // new WaitForUserDialog("").show();

            clij2.clear();
            //break;
        }
    }
}