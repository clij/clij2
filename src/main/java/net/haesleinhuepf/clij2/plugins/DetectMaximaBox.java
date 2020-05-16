package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;
import static net.haesleinhuepf.clij2.utilities.CLIJUtilities.checkDimensions;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_detectMaximaBox")
public class DetectMaximaBox extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        return getCLIJ2().detectMaximaBox((ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asInteger(args[2]), asInteger(args[2]), asInteger(args[2]));
    }

    public static boolean detectMaximaBox(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst, Integer radiusX, Integer radiusY, Integer radiusZ) {
        assertDifferent(src, dst);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (detectOptima)");
        }

        ClearCLBuffer temp = clij2.create(dst.getDimensions(), clij2.Float);
        clij2.meanBox(src, temp, radiusX, radiusY, radiusZ);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", temp);
        parameters.put("dst", dst);

        clij2.execute(DetectMaximaBox.class, "detect_maxima_" + src.getDimension() + "d_x.cl", "detect_maxima_" + src.getDimension() + "d", dst.getDimensions(), dst.getDimensions(), parameters);

        temp.close();
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Image destination, Number radius";
    }

    @Override
    public String getDescription() {
        return "Detects local maxima in a given square/cubic neighborhood. \n\n" +
                "Pixels in the resulting image are set to 1 if there is no other pixel in a given radius which has a \n" +
                "higher intensity, and to 0 otherwise.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
