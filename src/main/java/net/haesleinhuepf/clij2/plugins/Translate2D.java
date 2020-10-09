package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.clearcl.enums.ImageChannelDataType;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij.utilities.AffineTransform;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.CLIJUtilities;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import net.imglib2.realtransform.AffineTransform2D;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_translate2D")
public class Translate2D extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Image";
    }

    @Override
    public String getOutputType() {
        return "Image";
    }

    @Override
    public String getCategories() {
        return "Transform";
    }

    @Override
    public Object[] getDefaultValues() {
        return new Object[]{null, null, 0, 0};
    }

    @Override
    public boolean executeCL() {
        float translateX = -asFloat(args[2]);
        float translateY = -asFloat(args[3]);

        ClearCLBuffer input = ((ClearCLBuffer) args[0]);
        ClearCLBuffer output = ((ClearCLBuffer) args[1]);

        return getCLIJ2().translate2D(input, output, translateX, translateY);
    }

    public static boolean translate2D(CLIJ2 clij2, ClearCLBuffer input, ClearCLBuffer output, Float translateX, Float translateY) {
        AffineTransform2D at = new AffineTransform2D();

        at.translate(-translateX, -translateY);
        if (!clij2.hasImageSupport()) {
            return clij2.affineTransform2D(input, output, AffineTransform.matrixToFloatArray2D(at));
        } else {
            ClearCLImage image = clij2.create(input.getDimensions(), ImageChannelDataType.Float);
            clij2.copy(input, image);
            clij2.affineTransform2D(image, output, AffineTransform.matrixToFloatArray2D(at));
            clij2.release(image);
            return true;
        }
        /*
        Translate2D translate2D = new Translate2D();
        translate2D.setClij(clij2.getClij());
        translate2D.setArgs(new Object[]{input, output, translateX, translateY});
        return translate2D.executeCL();
        */
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Image destination, Number translateX, Number translateY";
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
