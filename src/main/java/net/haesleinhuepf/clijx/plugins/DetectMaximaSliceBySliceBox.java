package net.haesleinhuepf.clijx.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;
import static net.haesleinhuepf.clijx.utilities.CLIJUtilities.checkDimensions;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_detectMaximaSliceBySliceBox")
public class DetectMaximaSliceBySliceBox extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        return detectMaximaSliceBySliceBox(getCLIJx(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asInteger(args[2]));
    }


    public static boolean detectMaximaSliceBySliceBox(CLIJx clijx, ClearCLBuffer src, ClearCLBuffer dst, Integer radius) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("radius", radius);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (detectOptima)");
        }
        clijx.execute(DetectMaximaSliceBySliceBox.class, "detect_maxima_3d_slice_by_slice_x.cl", "detect_maxima_3d_slice_by_slice", dst.getDimensions(), dst.getDimensions(), parameters);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number radius";
    }

    @Override
    public String getDescription() {
        return "Detects local maxima in a given square neighborhood of an input image stack. The input image stack is \n" +
                "processed slice by slice. Pixels in the resulting image are set to 1 if there is no other pixel in a \n" +
                "given radius which has a higher intensity, and to 0 otherwise.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }

}
