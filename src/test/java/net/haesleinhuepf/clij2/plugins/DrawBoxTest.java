package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij2.plugins.DrawBox;
import net.haesleinhuepf.clijx.CLIJx;
import org.junit.Test;

public class DrawBoxTest {
    @Test
    public void test() {
        CLIJx clijx = CLIJx.getInstance();
        ClearCLBuffer image = clijx.create(new long[]{100, 100, 1}, NativeTypeEnum.Float);

        DrawBox.drawBox(clijx, image, 10f, 10f, 0f, 20f, 50f, 0f);
        //clijx.op.drawLine(image, 10f, 10f, 0f, 10f, 50f, 0f, 5f);

        //new ImageJ();
        //clijx.show(image, "image");
        //new WaitForUserDialog("helo").show();
        image.close();
    }

}