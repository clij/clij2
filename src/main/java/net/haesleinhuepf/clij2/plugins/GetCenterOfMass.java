package net.haesleinhuepf.clij2.plugins;

import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 *         April 2020
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_getCenterOfMass")
public class GetCenterOfMass extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized {
    @Override
    public String getCategories() {
        return "Measurements";
    }


    @Override
    public boolean executeCL() {
        ClearCLBuffer input = (ClearCLBuffer)( args[0]);

        double[] center = getCLIJ2().getCenterOfMass(input);
        for (int v = 0; v < center.length; v++) {
            ((Double[]) args[v + 1])[0] = center[v];
        }
        return true;
    }

    public static double[] getCenterOfMass(CLIJ2 clij2, ClearCLBuffer input) {
        return clij2.centerOfMass(input);
    }


    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Number centerOfMassX, ByRef Number centerOfMassY, ByRef Number centerOfMassZ";
    }

    @Override
    public String getDescription() {
        return "Determines the center of mass of an image or image stack.\n\n It writes the result in the variables\n" +
                " centerOfMassX, centerOfMassY and centerOfMassZ.\n\n" +
        "Note: This method has a misleading name. It should be called getCentroid. This will be changed in the major release.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
