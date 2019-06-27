package net.haesleinhuepf.clij.advancedfilters;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.NewImage;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import org.junit.Test;

import static net.haesleinhuepf.clij.advancedfilters.ConnectedComponentsLabeling.connectedComponentsLabeling;
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

        CLIJ clij = CLIJ.getInstance();

        ClearCLBuffer input = clij.push(imp);
        ClearCLBuffer output = clij.createCLBuffer(input.getDimensions(), NativeTypeEnum.Float);

        connectedComponentsLabeling(clij, input, output);

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

        CLIJ clij = CLIJ.getInstance();

        ClearCLBuffer input = clij.push(imp);
        ClearCLBuffer thresholded = clij.create(input);
        ClearCLBuffer output = clij.createCLBuffer(input.getDimensions(), NativeTypeEnum.Float);

        clij.op().threshold(input, thresholded, 127f);
        clij.show(thresholded, "thresholded");
        connectedComponentsLabeling(clij, thresholded, output);

        assertEquals(64.0, clij.op().maximumOfAllPixels(output), 0.1);
        //clij.show(output, "result");

        input.close();
        output.close();
        thresholded.close();



    }

}
