package net.haesleinhuepf.clij2.plugins;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.WaitForUserDialog;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij2.CLIJ2;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConnectedComponentsLabelingDiamondTest {
    @Test
    public void testCCAD() {
        //new ImageJ();
        ImagePlus imp = IJ.openImage("src/test/resources/miniBlobs.tif");
        //imp.show();

        CLIJ2 clij2 = CLIJ2.getInstance();

        int size = 5;

        ClearCLBuffer miniBlobs = clij2.push(imp);
        ClearCLBuffer input = clij2.create(new long[]{miniBlobs.getWidth() * size, miniBlobs.getHeight() * size, miniBlobs.getDepth() * size}, miniBlobs.getNativeType());
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                for (int z = 0; z < size; z++) {
                    Paste3D.paste(clij2, miniBlobs, input, (int)(x * miniBlobs.getWidth()), (int)(y * miniBlobs.getHeight()), (int)(z * miniBlobs.getDepth()));
                }
            }
        }
        //clij2.show(input, "input");

        ClearCLBuffer thresholded = clij2.create(input);
        ClearCLBuffer output = clij2.create(input.getDimensions(), NativeTypeEnum.Float);

        clij2.threshold(input, thresholded, 7f);
        //clij2.show(thresholded, "thresholded");

        for (int i = 0; i < 3; i++) {
            clij2.connectedComponentsLabelingDiamond( thresholded, output);


            //assertEquals(375.0, clij.op().maximumOfAllPixels(output), 0.1);
        }
        //clij2.show(output, "result");

        //new WaitForUserDialog("wait").show();


        clij2.release(input);
        clij2.release(output);
        clij2.release(thresholded);

        clij2.clear();
    }


}