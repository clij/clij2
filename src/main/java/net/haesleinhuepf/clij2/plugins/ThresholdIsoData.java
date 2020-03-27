package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.HasAuthor;
import net.haesleinhuepf.clij2.utilities.HasLicense;
import org.scijava.plugin.Plugin;

/**
 * ThresholdIsoData
 * <p>
 * Author: @haesleinhuepf
 *         February 2020
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_thresholdIsoData")
// This is generated code. See net.haesleinhuepf.clijx.codegenerator.GenerateThresholdOperations for details
public class ThresholdIsoData extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, HasAuthor, HasLicense {

    @Override
    public boolean executeCL() {
        ClearCLBuffer src = (ClearCLBuffer) (args[0]);
        ClearCLBuffer dst = (ClearCLBuffer) (args[1]);

        return thresholdIsoData(getCLIJ2(), src, dst);
    }

    public static boolean thresholdIsoData(CLIJ2 clij2, ClearCLBuffer src, ClearCLBuffer dst) {
        return clij2.automaticThreshold(src, dst, "IsoData");
    }

    @Override
    public String getDescription() {
        StringBuilder doc = new StringBuilder();
        doc.append("The automatic thresholder utilizes the IsoData threshold method implemented in ImageJ using a histogram determined on \n" +
                "the GPU to create binary images as similar as possible to ImageJ 'Apply Threshold' method.");
        return doc.toString();
    }


    @Override
    public String getParameterHelpText() {
        return "Image input, ByRef Image destination";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public String getAuthorName() {
        return "Robert Haase based on work by G. Landini and W. Rasband";
    }

    @Override
    public String getLicense() {
        return "The code for the automatic thresholding methods originates from " +
                "https://github.com/imagej/imagej1/blob/master/ij/process/AutoThresholder.java" +
                "\n\n" +
                "Detailed documentation on the implemented methods can be found online: " +
                "https://imagej.net/Auto_Threshold";
    }
}
