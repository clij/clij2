package net.haesleinhuepf.clij.painting;

import ij.ImageJ;
import ij.gui.WaitForUserDialog;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij2.CLIJ2;
import org.junit.Test;

import static org.junit.Assert.*;

public class DrawLineTest {
    @Test
    public void test2d() {
        CLIJ2 clij2 = CLIJ2.getInstance();
        ClearCLBuffer image = clij2.create(new long[]{100, 100}, NativeTypeEnum.Float);

        DrawLine.drawLine(CLIJ.getInstance(), image, 10f, 20f, 0f, 50f, 50f, 0f, 10f);
        //clij2.op.drawLine(image, 10f, 10f, 0f, 10f, 50f, 0f, 5f);

        //new ImageJ();
        //clij2.show(image, "image");
        //new WaitForUserDialog("helo").show();
        image.close();
    }
    @Test
    public void test3d() {
        CLIJ2 clij2 = CLIJ2.getInstance();
        ClearCLBuffer image = clij2.create(new long[]{100, 100, 100}, NativeTypeEnum.Float);

        DrawLine.drawLine(CLIJ.getInstance(), image, 10f, 20f, 0f, 50f, 50f, 70f, 10f);
        //clij2.op.drawLine(image, 10f, 10f, 0f, 10f, 50f, 0f, 5f);

        //new ImageJ();
        //clij2.show(image, "image");
        //new WaitForUserDialog("helo").show();
        image.close();
    }

}