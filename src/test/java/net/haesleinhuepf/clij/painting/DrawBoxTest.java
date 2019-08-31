package net.haesleinhuepf.clij.painting;

import ij.ImageJ;
import ij.gui.WaitForUserDialog;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij2.CLIJ2;
import org.junit.Test;

public class DrawBoxTest {
    @Test
    public void test() {
        CLIJ2 clij2 = CLIJ2.getInstance();
        ClearCLBuffer image = clij2.create(new long[]{100, 100, 1}, NativeTypeEnum.Float);

        DrawBox.drawBox(CLIJ.getInstance(), image, 10f, 10f, 0f, 20f, 50f, 0f);
        //clij2.op.drawLine(image, 10f, 10f, 0f, 10f, 50f, 0f, 5f);

        //new ImageJ();
        //clij2.show(image, "image");
        //new WaitForUserDialog("helo").show();
        image.close();
    }

}