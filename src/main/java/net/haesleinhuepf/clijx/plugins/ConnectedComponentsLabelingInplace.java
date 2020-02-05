package net.haesleinhuepf.clijx.plugins;

import ij.ImagePlus;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.clearcl.ClearCLKernel;
import net.haesleinhuepf.clij.clearcl.enums.ImageChannelDataType;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.plugins.CloseIndexGapsInLabelMap;
import net.haesleinhuepf.clij2.plugins.NonzeroMinimumBox;
import net.haesleinhuepf.clij2.plugins.SetNonZeroPixelsToPixelIndex;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

import java.nio.ByteBuffer;

import static net.haesleinhuepf.clij2.plugins.ConnectedComponentsLabeling.*;

/**
 * ConnectedComponentsLabeling
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 06 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_connectedComponentsLabelingInplace")
public class ConnectedComponentsLabelingInplace extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();
        ClearCLBuffer output = (ClearCLBuffer) args[0];

        boolean result = connectedComponentsLabelingInplace(getCLIJx(), output);
        releaseBuffers(args);
        return result;
    }

    public static boolean connectedComponentsLabelingInplace(CLIJx clijx, ClearCLBuffer output) {
        ClearCLImage temp1 = clijx.create(output.getDimensions(), ImageChannelDataType.Float);
        ClearCLImage temp2 = clijx.create(output.getDimensions(), ImageChannelDataType.Float);
        //ClearCLImage temp3 = clijx.create(output.getDimensions(), ImageChannelDataType.Float);

        //ClearCLBuffer temp1 = clijx.create(output);
        //ClearCLBuffer temp2 = clijx.create(output);
        ClearCLBuffer temp3 = clijx.create(output);

        ClearCLBuffer flag = clijx.create(new long[]{1,1,1}, NativeTypeEnum.Byte);
        ByteBuffer aByteBufferWithAZero = ByteBuffer.allocate(1);
        aByteBufferWithAZero.put((byte)0);
        flag.readFrom(aByteBufferWithAZero, true);

        SetNonZeroPixelsToPixelIndex.setNonZeroPixelsToPixelIndex(clijx, output, temp1);

        clijx.set(temp2, 0f);


        final int[] iterationCount = {0};
        int flagValue = 1;

        ClearCLKernel flipkernel = null;
        ClearCLKernel flopkernel = null;

        while (flagValue > 0) {
            if (iterationCount[0] % 2 == 0) {
                //NonzeroMinimumBox.nonzeroMinimumBox(clijx, temp1, flag, temp2, null).close();
                if (flipkernel == null) {
                    flipkernel = NonzeroMinimumBox.nonzeroMinimumBox(clijx, temp1, flag, temp2, flipkernel);
                } else {
                    flipkernel.run(true);
                }
            } else {
                //NonzeroMinimumBox.nonzeroMinimumBox(clijx, temp2, flag, temp1, null).close();

                if (flopkernel == null) {
                    flopkernel = NonzeroMinimumBox.nonzeroMinimumBox(clijx, temp2, flag, temp1, flopkernel);
                } else {
                    flopkernel.run(true);
                }

            }

            ImagePlus flagImp = clijx.pull(flag);
            flagValue = flagImp.getProcessor().get(0,0);
            flag.readFrom(aByteBufferWithAZero, true);
            iterationCount[0] = iterationCount[0] + 1;
        }

        if (iterationCount[0] % 2 == 0) {
            clijx.copy(temp1, temp3);
        } else {
            clijx.copy(temp1, temp3);
        }
        if (flipkernel != null) {
            flipkernel.close();
        }
        if (flopkernel != null) {
            flopkernel.close();
        }


        CloseIndexGapsInLabelMap.closeIndexGapsInLabelMap(clijx, temp3, output);

        clijx.release(temp1);
        clijx.release(temp2);
        clijx.release(temp3);
        clijx.release(flag);

        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image binary_source_labeling_destination";
    }

    @Override
    public String getDescription() {
        return "Performs connected components analysis to a binary image and generates a label map.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        return clij.create(input.getDimensions(), NativeTypeEnum.Float);
    }
}
