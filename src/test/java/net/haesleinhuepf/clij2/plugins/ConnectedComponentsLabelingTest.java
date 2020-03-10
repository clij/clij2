package net.haesleinhuepf.clij2.plugins;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.NewImage;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij2.CLIJ2;
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

        //imp.show();


        CLIJ2 clij2 = CLIJ2.getInstance();

        ClearCLBuffer input = clij2.push(imp);
        ClearCLBuffer output = clij2.create(input.getDimensions(), NativeTypeEnum.Float);

        clij2.connectedComponentsLabelingDiamond(input, output);

        assertEquals(clij2.maximumOfAllPixels(output), 2.0, 0.1);
        //clij.show(output, "result");

        clij2.release(input);
        clij2.release(output);

        clij2.clear();

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

        //imp.show();

        CLIJ2 clij2 = CLIJx.getInstance();

        ClearCLBuffer input = clij2.push(imp);
        ClearCLBuffer thresholded = clij2.create(input.getDimensions(), NativeTypeEnum.Float);
        ClearCLBuffer output = clij2.create(input.getDimensions(), NativeTypeEnum.Float);

        clij2.threshold(input, thresholded, 127f);
        //clij2.show(thresholded, "thresholded");
        clij2.connectedComponentsLabelingDiamond(thresholded, output);

        assertEquals(64.0, clij2.maximumOfAllPixels(output), 0.1);
        //clij.show(output, "result");

        clij2.release(input);
        clij2.release(output);
        clij2.release(thresholded);

        clij2.clear();

    }

    @Test
    public void testMiniBlobs() {
        ///new ImageJ();
        ImagePlus imp = IJ.openImage("src/test/resources/miniBlobs.tif");
        //imp.show();


        CLIJ2 clij2 = CLIJ2.getInstance();

        ClearCLBuffer input = clij2.push(imp);
        ClearCLBuffer thresholded = clij2.create(input.getDimensions(), NativeTypeEnum.Float);
        ClearCLBuffer output = clij2.create(input.getDimensions(), NativeTypeEnum.Float);

        clij2.threshold(input, thresholded, 7f);
        clij2.show(thresholded, "thresholded");
        clij2.connectedComponentsLabelingDiamond( thresholded, output);

        //clij2.show(output, "result");

        //new WaitForUserDialog("wait").show();

        assertEquals(3.0, clij2.maximumOfAllPixels(output), 0.1);
        clij2.release(input);
        clij2.release(output);
        clij2.release(thresholded);
    }

    @Test
    public void testManyMiniBlobs() {
        //new ImageJ();
        ImagePlus imp = IJ.openImage("src/test/resources/miniBlobs.tif");
        //imp.show();

        CLIJ2 clij2 = CLIJ2.getInstance();
        //CLIJ clij = clijx.getClij();

        ClearCLBuffer miniBlobs = clij2.push(imp);
        ClearCLBuffer input = clij2.create(new long[]{miniBlobs.getWidth() * 5, miniBlobs.getHeight() * 5, miniBlobs.getDepth() * 5}, miniBlobs.getNativeType());
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                for (int z = 0; z < 5; z++) {
                    Paste3D.paste(clij2, miniBlobs, input, (int)(x * miniBlobs.getWidth()), (int)(y * miniBlobs.getHeight()), (int)(z * miniBlobs.getDepth()));
                }
            }
        }
        //clijx.show(input, "input");

        ClearCLBuffer thresholded = clij2.create(input);
        ClearCLBuffer output = clij2.create(input.getDimensions(), NativeTypeEnum.Float);

        clij2.threshold(input, thresholded, 7f);
        clij2.show(thresholded, "thresholded");
        clij2.connectedComponentsLabelingDiamond( thresholded, output);

        clij2.show(output, "result");

        //new WaitForUserDialog("wait").show();

        assertEquals(375.0, clij2.maximumOfAllPixels(output), 0.1);

        clij2.release(input);
        clij2.release(output);
        clij2.release(thresholded);

        clij2.clear();
    }

}