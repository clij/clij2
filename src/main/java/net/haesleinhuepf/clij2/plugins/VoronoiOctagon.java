package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.clearcl.ClearCLKernel;
import net.haesleinhuepf.clij.clearcl.enums.ImageChannelDataType;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

import java.nio.FloatBuffer;

/**
 * Author: @haesleinhuepf
 *         March 2020
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_voronoiOctagon")
public class VoronoiOctagon extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        return voronoiOctagon(getCLIJ2(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]));
    }

    public static boolean voronoiOctagon(CLIJ2 clij2, ClearCLBuffer src, ClearCLBuffer dst) {
        //CLIJx.getInstance().stopWatch("");

        ClearCLImage flip = clij2.create(dst.getDimensions(), ImageChannelDataType.Float);
        ClearCLImage flop = clij2.create(flip);
        //CLIJx.getInstance().stopWatch("alloc");

        ClearCLKernel flipKernel = null;
        ClearCLKernel flopKernel = null;

        //clij2.copy(src, flip);
        ConnectedComponentsLabelingBox.connectedComponentsLabelingBox(clij2, src, flip, false);
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
            clij2.detectLabelEdges(flip, dst);
        } else {
            clij2.detectLabelEdges(flop, dst);
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
    public String getParameterHelpText() {
        return "Image input, ByRef Image destination";
    }

    @Override
    public String getDescription() {
        return "Takes a binary image and dilates the regions using a octagon shape until they touch. The pixels where " +
                " the regions touched are afterwards returned as binary image which corresponds to the Voronoi diagram.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
