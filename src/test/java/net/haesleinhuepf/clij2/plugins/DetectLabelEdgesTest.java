package net.haesleinhuepf.clij2.plugins;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij2.plugins.DetectLabelEdges;
import net.haesleinhuepf.clijx.CLIJx;
import org.junit.Test;

import static net.haesleinhuepf.clij2.plugins.ConnectedComponentsLabeling.connectedComponentsLabeling;

public class DetectLabelEdgesTest {
    @Test
    public void test() {
        new ImageJ();
        ImagePlus imp = IJ.openImage("src/test/resources/miniBlobs.tif");
        imp.show();

        CLIJ.debug = true;


        CLIJx clijx = CLIJx.getInstance();
        CLIJ clij = clijx.getClij();

        ClearCLBuffer input = clij.push(imp);
        ClearCLBuffer temp = clij.create(input.getDimensions(), NativeTypeEnum.Float);
        ClearCLBuffer output = clij.createCLBuffer(input.getDimensions(), NativeTypeEnum.Float);

        clij.op().threshold(input, temp, 7f);
        clij.show(temp, "thresholded");

        connectedComponentsLabeling(clijx, temp, output);

        DetectLabelEdges.detectLabelEdges(clijx, output, temp);

        clij.show(temp, "outline");

        //new WaitForUserDialog("wait").show();
        input.close();
        output.close();
        temp.close();
    }
}