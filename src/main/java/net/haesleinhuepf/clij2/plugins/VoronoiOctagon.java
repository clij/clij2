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
        return getCLIJ2().voronoiOctagon((ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]));
    }

    public static boolean voronoiOctagon(CLIJ2 clij2, ClearCLBuffer src, ClearCLBuffer dst) {
        //CLIJx.getInstance().stopWatch("");

        ClearCLImage flip = clij2.create(dst.getDimensions(), ImageChannelDataType.Float);

        VoronoiLabeling.voronoiLabeling(clij2, src, flip);
        clij2.detectLabelEdges(flip, dst);
        clij2.release(flip);

        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image input, ByRef Image destination";
    }

    @Override
    public String getDescription() {
        return "Takes a binary image and dilates the regions using a octagon shape until they touch. \n\nThe pixels where " +
                " the regions touched are afterwards returned as binary image which corresponds to the Voronoi diagram.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
