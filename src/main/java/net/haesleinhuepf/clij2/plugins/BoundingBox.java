package net.haesleinhuepf.clij2.plugins;

import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
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
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_boundingBox")
public class BoundingBox extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        ClearCLBuffer buffer1 = (ClearCLBuffer)( args[0]);
        double[] boundingBox = boundingBox(getCLIJ2(), buffer1);

        ResultsTable table = ResultsTable.getResultsTable();
        table.incrementCounter();
        table.addValue("BoundingBoxX", boundingBox[0]);
        table.addValue("BoundingBoxY", boundingBox[1]);
        table.addValue("BoundingBoxZ", boundingBox[2]);
        table.addValue("BoundingBoxWidth", boundingBox[3]);
        table.addValue("BoundingBoxHeight", boundingBox[4]);
        table.addValue("BoundingBoxDepth", boundingBox[5]);
        table.show("Results");
        return true;
    }

    public static double[] boundingBox(CLIJ2 clij2, ClearCLBuffer buffer) {
        double[] result;

        ClearCLBuffer temp1 = clij2.create(buffer.getDimensions(), buffer.getNativeType());

        // X
        clij2.multiplyImageAndCoordinate(buffer, temp1, 0);
        double maxX = clij2.maximumOfAllPixels(temp1);
        double minX = MinimumOfMaskedPixels.minimumOfMaskedPixels(clij2, temp1, buffer);

        // y
        clij2.multiplyImageAndCoordinate(buffer, temp1, 1);
        double maxY = clij2.maximumOfAllPixels(temp1);
        double minY = MinimumOfMaskedPixels.minimumOfMaskedPixels(clij2, temp1, buffer);

        if (buffer.getDimension() > 2) {
            // z
            clij2.multiplyImageAndCoordinate(buffer, temp1, 2);
            double maxZ = clij2.maximumOfAllPixels(temp1);
            double minZ = MinimumOfMaskedPixels.minimumOfMaskedPixels(clij2, temp1, buffer);

            result = new double[]{minX, minY, minZ, maxX - minX + 1, maxY - minY + 1, maxZ - minZ + 1};
        } else {
            result = new double[]{minX, minY, 0, maxX - minX + 1, maxY - minY + 1, 0};
        }
        clij2.release(temp1);
        return  result;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source";
    }

    @Override
    public String getDescription() {
        return "Determines the bounding box of all non-zero pixels in a binary image. The positions will be stored in a new row of ImageJs\n" +
                "Results table in the column 'BoundingBoxX', 'BoundingBoxY', 'BoundingBoxZ', 'BoundingBoxWidth', 'BoundingBoxHeight' 'BoundingBoxDepth'." +
                "In case of 2D images Z and depth will be zero.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
