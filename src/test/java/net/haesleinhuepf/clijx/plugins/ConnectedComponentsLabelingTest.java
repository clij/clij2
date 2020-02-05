package net.haesleinhuepf.clijx.plugins;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.NewImage;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij2.plugins.Paste3D;
import net.haesleinhuepf.clijx.CLIJx;
import org.junit.Test;

import static net.haesleinhuepf.clij2.plugins.ConnectedComponentsLabeling.connectedComponentsLabeling;
import static org.junit.Assert.assertEquals;

/**
 * ConnectedComponentsLabelingTest
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 06 2019
 */
public class ConnectedComponentsLabelingTest {
    @Test
    public void testLabeling() {
        //new ImageJ();

        ImagePlus imp = NewImage.createFloatImage("img", 100, 100, 1, NewImage.FILL_BLACK);

        imp.setRoi(10, 10, 10, 10);
        IJ.run(imp, "Add...", "value=1");
        imp.setRoi(10, 30, 10, 10);
        IJ.run(imp, "Add...", "value=1");

        imp.show();


        CLIJx clijx = CLIJx.getInstance();
        CLIJ clij = clijx.getClij();

        ClearCLBuffer input = clij.push(imp);
        ClearCLBuffer output = clij.createCLBuffer(input.getDimensions(), NativeTypeEnum.Float);

        connectedComponentsLabeling(clijx, input, output);

        assertEquals(clij.op().maximumOfAllPixels(output), 2.0, 0.1);
        //clij.show(output, "result");

        input.close();
        output.close();



    }

    @Test
    public void testBlobsLabeling() {
        //new ImageJ();

        ImagePlus imp = IJ.openImage("src/test/resources/blobs.tif");
        //NewImage.createFloatImage("img", 100, 100, 1, NewImage.FILL_BLACK);

        imp.setRoi(10, 10, 10, 10);
        IJ.run(imp, "Add...", "value=1");
        imp.setRoi(10, 30, 10, 10);
        IJ.run(imp, "Add...", "value=1");

        imp.show();

        CLIJx clijx = CLIJx.getInstance();
        CLIJ clij = clijx.getClij();

        ClearCLBuffer input = clij.push(imp);
        ClearCLBuffer thresholded = clij.create(input.getDimensions(), NativeTypeEnum.Float);
        ClearCLBuffer output = clij.createCLBuffer(input.getDimensions(), NativeTypeEnum.Float);

        clij.op().threshold(input, thresholded, 127f);
        clij.show(thresholded, "thresholded");
        connectedComponentsLabeling(clijx, thresholded, output);

        assertEquals(64.0, clij.op().maximumOfAllPixels(output), 0.1);
        //clij.show(output, "result");

        input.close();
        output.close();
        thresholded.close();



    }

    @Test
    public void testMiniBlobs() {
        new ImageJ();
        ImagePlus imp = IJ.openImage("src/test/resources/miniBlobs.tif");
        imp.show();


        CLIJx clijx = CLIJx.getInstance();
        CLIJ clij = clijx.getClij();

        ClearCLBuffer input = clij.push(imp);
        ClearCLBuffer thresholded = clij.create(input.getDimensions(), NativeTypeEnum.Float);
        ClearCLBuffer output = clij.createCLBuffer(input.getDimensions(), NativeTypeEnum.Float);

        clij.op().threshold(input, thresholded, 7f);
        clij.show(thresholded, "thresholded");
        connectedComponentsLabeling(clijx, thresholded, output);

        clij.show(output, "result");

        //new WaitForUserDialog("wait").show();

        assertEquals(3.0, clij.op().maximumOfAllPixels(output), 0.1);
        input.close();
        output.close();
        thresholded.close();
    }

    @Test
    public void testManyMiniBlobs() {
        new ImageJ();
        ImagePlus imp = IJ.openImage("src/test/resources/miniBlobs.tif");
        imp.show();

        CLIJx clijx = CLIJx.getInstance();
        //CLIJ clij = clijx.getClij();

        ClearCLBuffer miniBlobs = clijx.push(imp);
        ClearCLBuffer input = clijx.create(new long[]{miniBlobs.getWidth() * 5, miniBlobs.getHeight() * 5, miniBlobs.getDepth() * 5}, miniBlobs.getNativeType());
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                for (int z = 0; z < 5; z++) {
                    Paste3D.paste(clijx, miniBlobs, input, (int)(x * miniBlobs.getWidth()), (int)(y * miniBlobs.getHeight()), (int)(z * miniBlobs.getDepth()));
                }
            }
        }
        clijx.show(input, "input");

        ClearCLBuffer thresholded = clijx.create(input);
        ClearCLBuffer output = clijx.create(input.getDimensions(), NativeTypeEnum.Float);

        clijx.threshold(input, thresholded, 7f);
        clijx.show(thresholded, "thresholded");
        connectedComponentsLabeling(clijx, thresholded, output);

        clijx.show(output, "result");

        //new WaitForUserDialog("wait").show();

        assertEquals(375.0, clijx.maximumOfAllPixels(output), 0.1);

        input.close();
        output.close();
        thresholded.close();


    }

}