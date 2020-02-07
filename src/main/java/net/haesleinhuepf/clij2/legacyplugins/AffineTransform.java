package net.haesleinhuepf.clij2.legacyplugins;

import ij.IJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.plugins.AffineTransform2D;
import net.haesleinhuepf.clij2.plugins.AffineTransform3D;
import net.haesleinhuepf.clij2.utilities.HasAuthor;
import net.haesleinhuepf.clij2.utilities.HasLicense;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_affineTransform")
public class AffineTransform extends AffineTransform3D implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {
    @Override
    public boolean executeCL() {
        ClearCLImageInterface input = (ClearCLImageInterface) args[0];

        AbstractCLIJ2Plugin plugin = null;
        if (input.getDimension() == 2) {
            plugin = new AffineTransform2D();
        } else {
            plugin = new AffineTransform3D();
        }
        plugin.setClij(clij);
        plugin.setArgs(args);
        if (plugin instanceof CLIJOpenCLProcessor) {
            return ((CLIJOpenCLProcessor)plugin).executeCL();
        }
        return false;
    }

    @Deprecated
    public boolean affineTransform(CLIJ2 clij2, ClearCLImageInterface input, ClearCLImageInterface output, net.imglib2.realtransform.AffineTransform2D at) {
        if (input instanceof ClearCLBuffer) {
            return AffineTransform2D.affineTransform2D(clij2, (ClearCLBuffer)input, output, at);
        } else if (input instanceof ClearCLImageInterface){
            return AffineTransform2D.affineTransform2D(clij2, (ClearCLImage)input, output, at);
        }
        return false;
    }

    @Deprecated
    public boolean affineTransform(CLIJ2 clij2, ClearCLImageInterface input, ClearCLImageInterface output, net.imglib2.realtransform.AffineTransform3D at) {
        if (input instanceof ClearCLBuffer) {
            return AffineTransform3D.affineTransform3D(clij2, (ClearCLBuffer)input, output, at);
        } else if (input instanceof ClearCLImageInterface){
            return AffineTransform3D.affineTransform3D(clij2, (ClearCLImage)input, output, at);
        }
        return false;
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
