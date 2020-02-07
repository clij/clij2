package net.haesleinhuepf.clij2.legacyplugins;

import ij.IJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.plugins.AffineTransform2D;
import net.haesleinhuepf.clij2.plugins.AffineTransform3D;
import net.haesleinhuepf.clij2.plugins.Scale2D;
import net.haesleinhuepf.clij2.plugins.Scale3D;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_scale")
public class Scale extends Scale3D implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    private static boolean notifiedDeprecated = false;
    @Override
    public boolean executeCL() {
        ClearCLImageInterface input = (ClearCLImageInterface) args[0];

        AbstractCLIJ2Plugin plugin = null;
        if (input.getDimension() == 2) {
            plugin = new Scale2D();
        } else {
            plugin = new Scale3D();
        }
        plugin.setClij(clij);
        plugin.setArgs(args);
        if (plugin instanceof CLIJOpenCLProcessor) {
            return ((CLIJOpenCLProcessor) plugin).executeCL();
        }
        return false;
    }

    @Deprecated
    public static boolean scale(CLIJ2 clij2, ClearCLBuffer input, ClearCLBuffer output, Float factorX, Float factorY, Float factorZ) {
        return scale3D(clij2, input, output, factorX, factorY, factorZ);
    }
}
