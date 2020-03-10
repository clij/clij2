package net.haesleinhuepf.clijx.plugins;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.WaitForUserDialog;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.test.TestUtilities;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clijx.plugins.Skeletonize;
import net.imglib2.img.array.ArrayImgs;
import org.junit.Test;

public class SkeletonizeTest {
    @Test
    public void testSkeletonize2D() {
        CLIJ2 clij2 = CLIJ2.getInstance();

        ClearCLBuffer binary_image = clij2.push(ArrayImgs.floats(new float[]{
                        1, 1, 1, 0, 0, 0, 0, 0, 0, 0,
                        1, 1, 1, 1, 1, 1, 0, 0, 0, 0,
                        1, 1, 1, 1, 1, 1, 1, 0, 0, 0,
                        0, 0, 0, 1, 1, 1, 1, 0, 0, 0,
                        0, 0, 0, 0, 1, 1, 1, 0, 0, 0,

                        0, 0, 0, 0, 1, 1, 1, 1, 0, 0,
                        0, 0, 0, 0, 1, 1, 1, 1, 1, 0,
                        0, 0, 0, 0, 1, 1, 1, 1, 1, 0,
                        0, 0, 0, 0, 1, 1, 1, 1, 1, 0,
                        0, 0, 0, 0, 1, 1, 1, 1, 1, 0,
                }, new long[]{10, 10}
        ));

        ClearCLBuffer refereanceResult = clij2.push(ArrayImgs.floats(new float[]{
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 1, 1, 1, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 1, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 1, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 1, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 1, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 1, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                }, new long[]{10, 10}
        ));

        ClearCLBuffer skeleton = clij2.create(binary_image);

        Skeletonize.skeletonize(clij2, binary_image, skeleton);

        System.out.println("Result: ");
        clij2.print(skeleton);

        TestUtilities.clBuffersEqual(clij2.getCLIJ(), skeleton, refereanceResult, 0);

        clij2.clear();
    }

    @Test
    public void testSkeletonize3D() {
        CLIJ2 clij2 = CLIJ2.getInstance();

        ImagePlus imp = IJ.openImage("src/test/resources/skeleton_3d_test.tif");

        ClearCLBuffer binary_image = clij2.push(imp);

        ClearCLBuffer skeleton = clij2.create(binary_image);

        Skeletonize.skeletonize(clij2, binary_image, skeleton);

        System.out.println("Result: ");
        //clij2.print(skeleton);

        new ImageJ();
        clij2.show(skeleton, "skel");
        new WaitForUserDialog("").show();

        clij2.clear();
    }



}