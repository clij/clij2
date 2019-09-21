package net.haesleinhuepf.clij.advancedfilters;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.WaitForUserDialog;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import org.junit.Test;

import static net.haesleinhuepf.clij.advancedfilters.ConnectedComponentsLabeling.connectedComponentsLabeling;
import static org.junit.Assert.*;

public class ExcludeLabelsOnEdgesTest {

    @Test
    public void test() {
        ImagePlus imp = IJ.openImage("src/test/resources/blobs.tif");

        CLIJ clij = CLIJ.getInstance();

        ClearCLBuffer input = clij.push(imp);
        ClearCLBuffer thresholded = clij.create(input.getDimensions(), NativeTypeEnum.Float);
        ClearCLBuffer connectedComponents = clij.createCLBuffer(input.getDimensions(), NativeTypeEnum.Float);
        ClearCLBuffer withoutEdges = clij.createCLBuffer(input.getDimensions(), NativeTypeEnum.Float);

        clij.op().threshold(input, thresholded, 127f);
        clij.show(thresholded, "thresholded");
        connectedComponentsLabeling(clij, thresholded, connectedComponents);

        ExcludeLabelsOnEdges.excludeLabelsOnEdges(clij, connectedComponents, withoutEdges);


        //new ImageJ();
        //clij.show(withoutEdges, "w");
        //new WaitForUserDialog("dd").show();

        assertEquals(64.0, clij.op().maximumOfAllPixels(connectedComponents), 0.1);
        assertEquals(46.0, clij.op().maximumOfAllPixels(withoutEdges), 0.1);


        //clij.show(output, "result");

        input.close();
        connectedComponents.close();
        thresholded.close();

    }

}