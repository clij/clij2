package net.haesleinhuepf.clij2.plugins;

import ij.ImageJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.plugins.AverageAngleBetweenAdjacentTriangles;
import net.imglib2.img.array.ArrayImgs;

public class averageAngleBetweenAdjacentTrianglesTest {
    public static void main(String[] args) {
        CLIJx clijx = CLIJx.getInstance();

        ClearCLBuffer pointlist = clijx.push(ArrayImgs.floats(new float[]{
                1, 1, 2, 1,
                1, 1, 1, 2,
                1, 3, 2, 2
                }, new long[]{4, 3}
        ));

        ClearCLBuffer touchmatrix = clijx.push(ArrayImgs.unsignedBytes(new byte[]{
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 1, 0, 0, 0,
                0, 1, 1, 0, 0,
                0, 1, 1, 0, 0
        }, new long[]{5,5}));

        ClearCLBuffer averageAngle = clijx.create(new long[]{4, 1, 1});

        AverageAngleBetweenAdjacentTriangles.averageAngleBetweenAdjacentTriangles(clijx, pointlist, touchmatrix, averageAngle);

        new ImageJ();
        clijx.show(averageAngle, "average angle");
    }
}