package net.haesleinhuepf.clijx.advancedfilters;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij.utilities.AffineTransform;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import net.haesleinhuepf.clijx.utilities.CLIJUtilities;
import net.imglib2.realtransform.AffineTransform3D;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_translate3D")
public class Translate3D extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        float translateX = -asFloat(args[2]);
        float translateY = -asFloat(args[3]);
        float translateZ = -asFloat(args[4]);

        AffineTransform3D at = new AffineTransform3D();
        Object[] args = openCLBufferArgs();

        at.translate(translateX, translateY, translateZ);

        ClearCLBuffer input = ((ClearCLBuffer) args[0]);
        ClearCLBuffer output = ((ClearCLBuffer) args[1]);
        CLIJx clijx = getCLIJx();

        if (!clij.hasImageSupport()) {
            return clijx.affineTransform3D(input, output, AffineTransform.matrixToFloatArray(at));
        } else {
            ClearCLImage image = clijx.create(input.getDimensions(), CLIJUtilities.nativeToChannelType(input.getNativeType()));
            clijx.copy(input, image);
            clijx.affineTransform3D(image, output, AffineTransform.matrixToFloatArray(at));
            clijx.release(image);
            return true;
        }
    }


    public static boolean translate3D(CLIJx clijx, ClearCLBuffer input, ClearCLBuffer output, Float translateX, Float translateY, Float translateZ) {
        Translate3D translate3D = new Translate3D();
        translate3D.setClij(clijx.getClij());
        translate3D.setArgs(new Object[]{input, output, translateX, translateY, translateZ});
        return translate3D.executeCL();
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number translateX, Number translateY, Number translateZ";
    }

    @Override
    public String getDescription() {
        return "Translate an image stack in X, Y and Z.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }
}
