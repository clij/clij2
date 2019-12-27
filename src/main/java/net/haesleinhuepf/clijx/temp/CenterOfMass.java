package net.haesleinhuepf.clijx.temp;

import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_centerOfMass")
public class CenterOfMass extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {

        ResultsTable table = ResultsTable.getResultsTable();
        table.incrementCounter();


        ClearCLBuffer input = (ClearCLBuffer)( args[0]);

        double[] center = centerOfMass(getCLIJx(), input);

        table.addValue("MassX", center[0]);
        table.addValue("MassY", center[1]);
        if (input.getDimension() > 2 && input.getDepth() > 1) {
            table.addValue("MassZ", center[2]);
        }
        releaseBuffers(args);


        table.show("Results");
        return true;
    }

    public static double[] centerOfMass(CLIJx clijx, ClearCLBuffer input) {
        ClearCLBuffer multipliedWithCoordinate = clijx.create(input.getDimensions(), NativeTypeEnum.Float);
        double sum = clijx.sumPixels(input);
        double[] resultCenterOfMass;
        if (input.getDimension() > 2L && input.getDepth() > 1L) {
            resultCenterOfMass = new double[3];
        } else {
            resultCenterOfMass = new double[2];
        }

        clijx.multiplyImageAndCoordinate(input, multipliedWithCoordinate, 0);
        double sumX = clijx.sumPixels(multipliedWithCoordinate);
        resultCenterOfMass[0] = sumX / sum;
        clijx.multiplyImageAndCoordinate(input, multipliedWithCoordinate, 1);
        double sumY = clijx.sumPixels(multipliedWithCoordinate);
        resultCenterOfMass[1] = sumY / sum;
        if (input.getDimension() > 2L && input.getDepth() > 1L) {
            clijx.multiplyImageAndCoordinate(input, multipliedWithCoordinate, 2);
            double sumZ = clijx.sumPixels(multipliedWithCoordinate);
            resultCenterOfMass[2] = sumZ / sum;
        }

        multipliedWithCoordinate.close();
        return resultCenterOfMass;
    }


    @Override
    public String getParameterHelpText() {
        return "Image source";
    }

    @Override
    public String getDescription() {
        return "Determines the center of mass of an image or image stack and writes the result in the results table\n" +
                "in the columns MassX, MassY and MassZ.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
