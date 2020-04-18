package net.haesleinhuepf.clij2.plugins;

import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_centerOfMass")
public class CenterOfMass extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {

        ResultsTable table = ResultsTable.getResultsTable();
        table.incrementCounter();


        ClearCLBuffer input = (ClearCLBuffer)( args[0]);

        double[] center = centerOfMass(getCLIJ2(), input);

        table.addValue("MassX", center[0]);
        table.addValue("MassY", center[1]);
        if (input.getDimension() > 2 && input.getDepth() > 1) {
            table.addValue("MassZ", center[2]);
        }
        releaseBuffers(args);


        table.show("Results");
        return true;
    }

    public static double[] centerOfMass(CLIJ2 clij2, ClearCLBuffer input) {
        ClearCLBuffer multipliedWithCoordinate = clij2.create(input.getDimensions(), NativeTypeEnum.Float);
        double sum = clij2.sumOfAllPixels(input);
        double[] resultCenterOfMass;
        if (input.getDimension() > 2L && input.getDepth() > 1L) {
            resultCenterOfMass = new double[3];
        } else {
            resultCenterOfMass = new double[2];
        }

        clij2.multiplyImageAndCoordinate(input, multipliedWithCoordinate, 0);
        double sumX = clij2.sumOfAllPixels(multipliedWithCoordinate);
        resultCenterOfMass[0] = sumX / sum;
        clij2.multiplyImageAndCoordinate(input, multipliedWithCoordinate, 1);
        double sumY = clij2.sumOfAllPixels(multipliedWithCoordinate);
        resultCenterOfMass[1] = sumY / sum;
        if (input.getDimension() > 2L && input.getDepth() > 1L) {
            clij2.multiplyImageAndCoordinate(input, multipliedWithCoordinate, 2);
            double sumZ = clij2.sumOfAllPixels(multipliedWithCoordinate);
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
        return "Determines the center of mass of an image or image stack. \n\nIt writes the result in the results table\n" +
                "in the columns MassX, MassY and MassZ.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
