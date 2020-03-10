package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij2.plugins.DrawLine;
import net.haesleinhuepf.clijx.CLIJx;
import org.junit.Test;

public class DrawLineTest {
    @Test
    public void test2d() {
        CLIJx clijx = CLIJx.getInstance();
        ClearCLBuffer image = clijx.create(new long[]{100, 100}, NativeTypeEnum.Float);

        DrawLine.drawLine(clijx, image, 10f, 20f, 0f, 50f, 50f, 0f, 10f);
        //clijx.op.drawLine(image, 10f, 10f, 0f, 10f, 50f, 0f, 5f);

        //new ImageJ();
        //clijx.show(image, "image");
        //new WaitForUserDialog("helo").show();
        image.close();
    }
    @Test
    public void test3d() {
        CLIJx clijx = CLIJx.getInstance();
        ClearCLBuffer image = clijx.create(new long[]{100, 100, 100}, NativeTypeEnum.Float);

        DrawLine.drawLine(clijx, image, 10f, 20f, 0f, 50f, 50f, 70f, 10f);
        //clijx.op.drawLine(image, 10f, 10f, 0f, 10f, 50f, 0f, 5f);

        //new ImageJ();
        //clijx.show(image, "image");
        //new WaitForUserDialog("helo").show();
        image.close();
    }

}