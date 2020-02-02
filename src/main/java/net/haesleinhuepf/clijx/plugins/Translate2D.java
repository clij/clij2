package net.haesleinhuepf.clijx.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij.utilities.AffineTransform;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import net.haesleinhuepf.clij2.utilities.CLIJUtilities;
import net.imglib2.realtransform.AffineTransform2D;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_translate2D")
public class Translate2D extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        float translateX = -asFloat(args[2]);
        float translateY = -asFloat(args[3]);

        AffineTransform2D at = new AffineTransform2D();
        Object[] args = openCLBufferArgs();

        at.translate(translateX, translateY);

        ClearCLBuffer input = ((ClearCLBuffer) args[0]);
        ClearCLBuffer output = ((ClearCLBuffer) args[1]);
        CLIJx clijx = getCLIJx();

        if (!clij.hasImageSupport()) {
            return clijx.affineTransform2D(input, output, AffineTransform.matrixToFloatArray2D(at));
        } else {
            ClearCLImage image = clijx.create(input.getDimensions(), CLIJUtilities.nativeToChannelType(input.getNativeType()));
            clijx.copy(input, image);
            clijx.affineTransform2D(image, output, AffineTransform.matrixToFloatArray2D(at));
            clijx.release(image);
            return true;
        }
    }

    public static boolean translate2D(CLIJx clijx, ClearCLBuffer input, ClearCLBuffer output, Float translateX, Float translateY) {
        Translate2D translate2D = new Translate2D();
        translate2D.setClij(clijx.getClij());
        translate2D.setArgs(new Object[]{input, output, translateX, translateY});
        return translate2D.executeCL();
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number translateX, Number translateY";
    }

    @Override
    public String getDescription() {
        return "Translate an image stack in X and Y.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }
}
