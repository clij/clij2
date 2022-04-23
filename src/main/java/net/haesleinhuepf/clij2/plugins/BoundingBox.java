package net.haesleinhuepf.clij2.plugins;

import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_boundingBox")
public class BoundingBox extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized {
    @Override
    public String getCategories() {
        return "Measurements";
    }

    @Override
    public boolean executeCL() {
        ClearCLBuffer buffer1 = (ClearCLBuffer)( args[0]);
        double[] boundingBox = getCLIJ2().boundingBox(buffer1);

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

        ClearCLBuffer temp1 = clij2.create(buffer.getDimensions(), clij2.Float);

        // turns any image into a 0 1 binary
        clij2.greaterOrEqualConstant(buffer, temp1, 1f);

        ClearCLBuffer temp2 = clij2.create(buffer.getDimensions(), clij2.Float);

        // X
        clij2.multiplyImageAndCoordinate(temp1, temp2, 0);
        double maxX = clij2.maximumOfAllPixels(temp2);
        double minX = MinimumOfMaskedPixels.minimumOfMaskedPixels(clij2, temp2, buffer);

        // y
        clij2.multiplyImageAndCoordinate(temp1, temp2, 1);
        double maxY = clij2.maximumOfAllPixels(temp2);
        double minY = MinimumOfMaskedPixels.minimumOfMaskedPixels(clij2, temp2, buffer);

        if (buffer.getDimension() > 2) {
            // z
            clij2.multiplyImageAndCoordinate(temp1, temp2, 2);
            double maxZ = clij2.maximumOfAllPixels(temp2);
            double minZ = MinimumOfMaskedPixels.minimumOfMaskedPixels(clij2, temp2, buffer);

            result = new double[]{minX, minY, minZ, maxX - minX + 1, maxY - minY + 1, maxZ - minZ + 1};
        } else {
            result = new double[]{minX, minY, 0, maxX - minX + 1, maxY - minY + 1, 0};
        }
        clij2.release(temp1);
        clij2.release(temp2);
        return  result;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source";
    }

    @Override
    public String getDescription() {
        return "Determines the bounding box of all non-zero pixels in a binary image. \n\nIf called from macro, the " +
                "positions will be stored in a new row of ImageJs Results table in the columns 'BoundingBoxX', " +
                "'BoundingBoxY', 'BoundingBoxZ', 'BoundingBoxWidth', 'BoundingBoxHeight' 'BoundingBoxDepth'." +
                "In case of 2D images Z and depth will be zero.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
