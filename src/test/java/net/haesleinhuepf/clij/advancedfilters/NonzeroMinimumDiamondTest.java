package net.haesleinhuepf.clij.advancedfilters;

import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.NewImage;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.test.TestUtilities;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class NonzeroMinimumDiamondTest {
    @Test
    public void testMinimum3DDiamond() {
        new ImageJ();
        ImagePlus imp = NewImage.createFloatImage("bb", 10, 10, 3, NewImage.FILL_BLACK);
        imp.setRoi(2,2, 3,3);
        for (int z = 0; z < 3; z++) {
            imp.setSlice(z+1);
            imp.getProcessor().add(1);
        }

        imp.show();

        CLIJ clij = CLIJ.getInstance();
        ClearCLBuffer input = clij.push(imp);
        ClearCLBuffer output = clij.create(input);
        ClearCLBuffer outputRef = clij.create(input);
        ClearCLBuffer flag = clij.create(new long[]{1,1,1}, output.getNativeType());

        NonzeroMinimum3DDiamond.nonzeroMinimum3DDiamond(clij, input, flag, output);
        clij.op().minimumSphere(input, outputRef, 3, 3, 3);

        //clij.show(output, "output");
        //clij.show(output, "outputRef");
        assertTrue(TestUtilities.clBuffersEqual(clij, output, input, 0));
        //new WaitForUserDialog("44").show();

        input.close();
        output.close();
        outputRef.close();
        flag.close();
    }
}
