package net.haesleinhuepf.clij2.plugins;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.NewImage;
import ij.gui.WaitForUserDialog;
import ij.process.ImageProcessor;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij2.CLIJ2;
import org.junit.Test;

import static org.junit.Assert.*;

public class VoronoiOctagonTest {
    @Test
    public void testVoronoiOctagon(){
        ImagePlus imagePlus = NewImage.createFloatImage("title", 10, 10, 1, NewImage.FILL_BLACK);
        ImageProcessor ip = imagePlus.getProcessor();
        ip.setf(1, 2, 1);
        ip.setf(3, 2, 1);
        ip.setf(8, 1, 1);
        ip.setf(1, 6, 1);

        CLIJ2 clij2 = CLIJ2.getInstance();
        ClearCLBuffer bufferIn = clij2.push(imagePlus);
        ClearCLBuffer bufferOut = clij2.create(bufferIn);

        VoronoiOctagon.voronoiOctagon(clij2, bufferIn, bufferOut);

        clij2.print(bufferOut);

        clij2.clear();
    }

    @Test
    public void testVoronoiOctagon3D(){
        ImagePlus imagePlus = IJ.openImage("src/test/resources/miniBlobs.tif");

        CLIJ2 clij2 = CLIJ2.getInstance();
        ClearCLBuffer bufferIn = clij2.push(imagePlus);
        ClearCLBuffer bufferOut = clij2.create(bufferIn);
        ClearCLBuffer temp = clij2.create(bufferIn);

        clij2.thresholdOtsu(bufferIn, temp);

        VoronoiOctagon.voronoiOctagon(clij2, temp, bufferOut);

        clij2.print(bufferOut);

        clij2.clear();
    }

    @Test
    public void testLargeImage(){
        CLIJ2 clij2 = CLIJ2.getInstance();

        ImagePlus input_imp = IJ.openImage("src/test/resources/blobs.tif");

        ClearCLBuffer input = clij2.push(input_imp);

        int factor = 5;
        ClearCLBuffer scaled = clij2.create(new long[]{input.getWidth() * factor, input.getHeight() * factor});
        ClearCLBuffer thresholded = clij2.create(scaled);
        ClearCLBuffer output = clij2.create(scaled);

        clij2.scale2D(input, scaled, factor, factor);
        clij2.thresholdOtsu(scaled, thresholded);

        for (int i = 0; i < 3; i++) {
            long time = System.currentTimeMillis();
            clij2.voronoiOctagon(thresholded, output);
            System.out.println("Voronoi took " + (System.currentTimeMillis() - time) + " msec");
        }
        //clij2.show(output, "output");
        //new WaitForUserDialog("").show();

        clij2.clear();
    }
}