package net.haesleinhuepf.clij.advancedfilters;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.WaitForUserDialog;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clijx.CLIJx;
import org.junit.Test;

import static net.haesleinhuepf.clij.advancedfilters.ConnectedComponentsLabeling.connectedComponentsLabeling;
import static org.junit.Assert.*;

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