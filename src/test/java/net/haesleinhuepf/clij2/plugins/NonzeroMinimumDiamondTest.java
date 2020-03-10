package net.haesleinhuepf.clij2.plugins;

import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.NewImage;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.test.TestUtilities;
import net.haesleinhuepf.clij2.plugins.NonzeroMinimumDiamond;
import net.haesleinhuepf.clijx.CLIJx;
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

        CLIJx clijx = CLIJx.getInstance();
        ClearCLBuffer input = clijx.push(imp);
        ClearCLBuffer output = clijx.create(input);
        ClearCLBuffer outputRef = clijx.create(input);
        ClearCLBuffer flag = clijx.create(new long[]{1,1,1}, output.getNativeType());

        NonzeroMinimumDiamond.nonzeroMinimumDiamond(clijx, input, flag, output);
        clijx.minimum3DSphere(input, outputRef, 3, 3, 3);

        //clij.show(output, "output");
        //clij.show(output, "outputRef");
        assertTrue(TestUtilities.clBuffersEqual(clijx.getCLIJ(), output, input, 0));
        //new WaitForUserDialog("44").show();

        input.close();
        output.close();
        outputRef.close();
        flag.close();
    }
}
