package net.haesleinhuepf.clijx.advancedfilters;

import ij.ImagePlus;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLKernel;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

import java.nio.ByteBuffer;
import java.util.HashMap;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_seededWatershed")
public class SeededWatershed extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        return seededWatershed(getCLIJx(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), (ClearCLBuffer)(args[2]));
    }

    public static boolean seededWatershed(CLIJx clijx, ClearCLBuffer thresholded, ClearCLBuffer spot_seeds, ClearCLBuffer output) {
        ClearCLBuffer distanceMap = clijx.create(thresholded);
        clijx.distanceMap(thresholded, distanceMap);

        ClearCLBuffer labelMap = clijx.create(thresholded);
        ConnectedComponentsLabeling.connectedComponentsLabeling(clijx, spot_seeds, labelMap);
        //clijx.release(spot_seeds);

        ClearCLBuffer labelMap2 = clijx.create(thresholded);
        ClearCLBuffer distanceMap2 = clijx.create(thresholded);
        Watershed.dilateLabelsUntilNoChange(clijx, distanceMap, labelMap, distanceMap2, labelMap2);
        clijx.release(distanceMap2);
        clijx.release(labelMap);
        clijx.release(distanceMap);

        Watershed.binarizeLabelmap(clijx, labelMap2, output);
        clijx.release(labelMap2);
        // shiftIntensitiesToCloseGaps(clijx, temp3, output);

        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image binary_source, Image spot_seeds, Image destination";
    }

    @Override
    public String getDescription() {
        return "Apply a binary watershed to a binary image and introduces black pixels between objects.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
