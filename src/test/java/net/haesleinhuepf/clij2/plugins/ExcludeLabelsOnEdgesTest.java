package net.haesleinhuepf.clij2.plugins;

import ij.IJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij2.plugins.ExcludeLabelsOnEdges;
import net.haesleinhuepf.clijx.CLIJx;
import org.junit.Test;

import static net.haesleinhuepf.clij2.plugins.ConnectedComponentsLabeling.connectedComponentsLabeling;
import static org.junit.Assert.*;

public class ExcludeLabelsOnEdgesTest {

    @Test
    public void test() {
        ImagePlus imp = IJ.openImage("src/test/resources/blobs.tif");


        CLIJx clijx = CLIJx.getInstance();

        ClearCLBuffer input = clijx.push(imp);
        ClearCLBuffer thresholded = clijx.create(input.getDimensions(), NativeTypeEnum.Float);
        ClearCLBuffer connectedComponents = clijx.create(input.getDimensions(), NativeTypeEnum.Float);
        ClearCLBuffer withoutEdges = clijx.create(input.getDimensions(), NativeTypeEnum.Float);

        clijx.threshold(input, thresholded, 127f);
        clijx.show(thresholded, "thresholded");
        connectedComponentsLabeling(clijx, thresholded, connectedComponents);

        ExcludeLabelsOnEdges.excludeLabelsOnEdges(clijx, connectedComponents, withoutEdges);


        //new ImageJ();
        //clij.show(withoutEdges, "w");
        //new WaitForUserDialog("dd").show();

        assertEquals(64.0, clijx.maximumOfAllPixels(connectedComponents), 0.1);
        assertEquals(46.0, clijx.maximumOfAllPixels(withoutEdges), 0.1);


        //clij.show(output, "result");

        input.close();
        connectedComponents.close();
        thresholded.close();

    }

}