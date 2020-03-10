package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.plugins.DrawSphere;
import net.haesleinhuepf.clijx.CLIJx;
import org.junit.Test;

public class DrawSphereTest {
    @Test
    public void test() {
        CLIJx clijx = CLIJx.getInstance();
        ClearCLBuffer image = clijx.create(new long[]{100, 100}, NativeTypeEnum.Float);

        DrawSphere.drawSphere(CLIJ2.getInstance(), image, 50f, 50f, 10f, 20f);

        //new ImageJ();
        //clijx.show(image, "image");
        //new WaitForUserDialog("helo").show();

        image.close();
    }

}