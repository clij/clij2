package net.haesleinhuepf.clijx.plugins;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clijx.CLIJx;
import org.junit.Test;

public class WatershedTest {
    @Test
    public void testBlobs2D() {
        new ImageJ();
        ImagePlus imp = IJ.openImage("src/test/resources/blobs.tif");
        IJ.run(imp,"32-bit", "");
        imp.show();

        CLIJx clijx = CLIJx.getInstance();

        ClearCLBuffer input = clijx.push(imp);
        ClearCLBuffer thresholded = clijx.create(input);
        ClearCLBuffer watershed = clijx.create(input);

        clijx.threshold(input, thresholded, 127);

        Watershed.watershed(clijx, thresholded, watershed);

        clijx.show(watershed, "watershed");
        //new WaitForUserDialog("wait").show();

        watershed.close();
        thresholded.close();
        input.close();

    }

    @Test
    public void testBlobs3D() {
        new ImageJ();
        ImagePlus imp = IJ.openImage("src/test/resources/miniBlobs.tif");
        IJ.run(imp,"32-bit", "");
        imp.show();

        CLIJx clijx = CLIJx.getInstance();

        ClearCLBuffer input = clijx.push(imp);
        ClearCLBuffer thresholded = clijx.create(input);
        ClearCLBuffer watershed = clijx.create(input);

        clijx.threshold(input, thresholded, 1);

        Watershed.watershed(clijx, thresholded, watershed);

        //clijx.show(watershed, "watershed");
        //new WaitForUserDialog("wait").show();

        watershed.close();
        thresholded.close();
        input.close();

    }



}