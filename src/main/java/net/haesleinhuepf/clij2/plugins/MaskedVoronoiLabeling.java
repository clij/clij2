package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.clearcl.ClearCLKernel;
import net.haesleinhuepf.clij.clearcl.enums.ImageChannelDataType;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.plugins.ConnectedComponentsLabelingBox;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

import java.nio.FloatBuffer;

/**
 * Author: @haesleinhuepf
 *         March 2020
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_maskedVoronoiLabeling")
public class MaskedVoronoiLabeling extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Binary Image";
    }

    @Override
    public String getOutputType() {
        return "Label Image";
    }


    @Override
    public String getCategories() {
        return "Binary, Label, Filter";
    }

    @Override
    public boolean executeCL() {
        return maskedVoronoiLabeling(getCLIJ2(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), (ClearCLBuffer)(args[2]));
    }

    public static boolean maskedVoronoiLabeling(CLIJ2 clij2, ClearCLBuffer src, ClearCLBuffer mask, ClearCLImageInterface dst) {
        //CLIJx.getInstance().stopWatch("");

        ClearCLBuffer flip = clij2.create(dst.getDimensions(), NativeTypeEnum.Float);
        ClearCLBuffer flop = clij2.create(flip);
        ClearCLBuffer flup = clij2.create(flip);
        clij2.addImageAndScalar(mask, flup, -1);
        //CLIJx.getInstance().stopWatch("alloc");

        ClearCLKernel flipKernel = null;
        ClearCLKernel flopKernel = null;

        //clij2.copy(src, flip);
        ConnectedComponentsLabelingBox.connectedComponentsLabelingBox(clij2, src, flop, false);
        clij2.addImages(flop, flup, flip);
        flup.close();
        //CLIJx.getInstance().stopWatch("cca");

        ClearCLBuffer flag = clij2.create(1,1,1);
        float[] flagBool = new float[1];
        flagBool[0] = 1;

        FloatBuffer buffer = FloatBuffer.wrap(flagBool);

        int i = 0;
        //CLIJx.getInstance().stopWatch("");
        while (flagBool[0] != 0) {
            //CLIJx.getInstance().stopWatch("h " + i);
            //System.out.println(i);

            flagBool[0] = 0;
            flag.readFrom(buffer, true);

            if (i % 2 == 0) {
                flipKernel = clij2.onlyzeroOverwriteMaximumBox(flip, flag, flop, flipKernel);
            } else {
                flopKernel = clij2.onlyzeroOverwriteMaximumDiamond(flop, flag, flip, flopKernel);
            }
            i++;

            flag.writeTo(buffer, true);
            //System.out.println(flagBool[0]);
        }
        //CLIJx.getInstance().stopWatch("h " + i);

        if (i % 2 == 0) {
            clij2.mask(flip, mask, dst);
        } else {
            clij2.mask(flop, mask, dst);
        }
        //CLIJx.getInstance().stopWatch("edges");

        if (flipKernel != null) {
            flipKernel.close();
        }
        if (flopKernel != null) {
            flopKernel.close();
        }
        clij2.release(flip);
        clij2.release(flop);
        clij2.release(flag);

        return true;
    }
    
    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        return clij.create(input.getDimensions(), NativeTypeEnum.Float);
    }

    @Override
    public String getParameterHelpText() {
        return "Image input, Image mask, ByRef Image destination";
    }

    @Override
    public String getDescription() {
        return "Takes a binary image, labels connected components and dilates the regions using a octagon shape until they touch and only inside another binary mask image.\n\nThe resulting label map is written to the output.\n\n" +
                "Hint: Process isotropic images only.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
