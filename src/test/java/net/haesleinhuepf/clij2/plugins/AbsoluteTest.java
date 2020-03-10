package net.haesleinhuepf.clij2.plugins;

import ij.ImagePlus;
import ij.gui.NewImage;
import ij.process.ImageProcessor;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.test.TestUtilities;
import net.haesleinhuepf.clijx.CLIJx;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AbsoluteTest {
    @Test
    public void absolute3d() {
        CLIJx clijx = CLIJx.getInstance();

        ImagePlus negativeImp =
                NewImage.createImage("",
                        2,
                        2,
                        2,
                        32,
                        NewImage.FILL_BLACK);

        ImageProcessor ip1 = negativeImp.getProcessor();
        ip1.setf(0, 1, -1.0f);

        ClearCLImage input = clijx.convert(negativeImp, ClearCLImage.class);


        assertEquals(-1, clijx.sumPixels(input), 0.0001);

        ClearCLImage abs = clijx.create(input);
        clijx.absolute(input, abs);
        assertEquals(1, clijx.sumPixels(abs), 0.0001);

        clijx.clear();
    }

    @Test
    public void absolute3d_Buffer() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus negativeImp =
                NewImage.createImage("",
                        2,
                        2,
                        2,
                        32,
                        NewImage.FILL_BLACK);

        ImageProcessor ip1 = negativeImp.getProcessor();
        ip1.setf(0, 1, -1.0f);

        ClearCLBuffer input = clijx.convert(negativeImp, ClearCLBuffer.class);
        //converter(negativeImp).getClearCLBuffer();

        assertEquals(-1, clijx.sumPixels(input), 0.0001);

        ClearCLBuffer abs = clijx.create(input);
        clijx.absolute(input, abs);
        assertEquals(1, clijx.sumPixels(abs), 0.0001);

        clijx.clear();
    }

    @Test
    public void compare_absolute3d_Buffer_to_clij1() {
        CLIJx clijx = CLIJx.getInstance();
        ImagePlus negativeImp =
                NewImage.createImage("",
                        2,
                        2,
                        2,
                        32,
                        NewImage.FILL_BLACK);

        ImageProcessor ip1 = negativeImp.getProcessor();
        ip1.setf(0, 1, -1.0f);

        ClearCLBuffer input = clijx.convert(negativeImp, ClearCLBuffer.class);
        //converter(negativeImp).getClearCLBuffer();

        assertEquals(-1, clijx.sumPixels(input), 0.0001);

        ClearCLBuffer abs = clijx.create(input);
        clijx.absolute(input, abs);
        assertEquals(1, clijx.sumPixels(abs), 0.0001);

        ClearCLBuffer abs2 = clijx.create(input);
        clijx.getClij().op().absolute(input, abs2);
        TestUtilities.clBuffersEqual(clijx.getClij(), abs, abs2, 0.0);

        clijx.clear();
    }



}