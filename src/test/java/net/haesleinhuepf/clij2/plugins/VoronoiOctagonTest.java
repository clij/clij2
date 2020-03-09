package net.haesleinhuepf.clij2.plugins;

import ij.ImagePlus;
import ij.gui.NewImage;
import ij.process.ImageProcessor;
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
    }
}