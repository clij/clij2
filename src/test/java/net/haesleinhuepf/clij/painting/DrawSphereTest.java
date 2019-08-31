package net.haesleinhuepf.clij.painting;

import ij.ImageJ;
import ij.gui.WaitForUserDialog;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij2.CLIJ2;
import org.junit.Test;

public class DrawSphereTest {
    @Test
    public void test() {
        CLIJ2 clij2 = CLIJ2.getInstance();
        ClearCLBuffer image = clij2.create(new long[]{100, 100}, NativeTypeEnum.Float);

        DrawSphere.drawSphere(CLIJ.getInstance(), image, 50f, 50f, 10f, 20f);

        //new ImageJ();
        //clij2.show(image, "image");
        //new WaitForUserDialog("helo").show();

        image.close();
    }

}