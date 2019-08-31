package net.haesleinhuepf.clij.advancedfilters;

import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_boundingBox")
public class BoundingBox extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {

        Object[] args = openCLBufferArgs();
        ClearCLBuffer buffer1 = (ClearCLBuffer)( args[0]);
        double[] boundingBox = boundingBox(clij, buffer1);
        releaseBuffers(args);


        ResultsTable table = ResultsTable.getResultsTable();
        table.incrementCounter();
        table.addValue("BoundingBoxX", boundingBox[0]);
        table.addValue("BoundingBoxX", boundingBox[1]);
        if (buffer1.getDimension() > 2) {
            table.addValue("BoundingBoxZ", boundingBox[2]);
            table.addValue("BoundingBoxWidth", boundingBox[3]);
            table.addValue("BoundingBoxHeight", boundingBox[4]);
            table.addValue("BoundingBoxDepth", boundingBox[5]);
        } else {
            table.addValue("BoundingBoxWidth", boundingBox[2]);
            table.addValue("BoundingBoxHeight", boundingBox[3]);
        }
        table.show("Results");
        return true;
    }

    public static double[] boundingBox(CLIJ clij, ClearCLBuffer buffer) {
        double[] result;

        ClearCLBuffer temp1 = clij.create(buffer.getDimensions(), buffer.getNativeType());

        // X
        clij.op().multiplyImageAndCoordinate(buffer, temp1, 0);
        double maxX = clij.op().maximumOfAllPixels(temp1);
        double minX = MinimumOfMaskedPixels.minimumOfMaskedPixels(clij, temp1, buffer);

        // y
        clij.op().multiplyImageAndCoordinate(buffer, temp1, 1);
        double maxY = clij.op().maximumOfAllPixels(temp1);
        double minY = MinimumOfMaskedPixels.minimumOfMaskedPixels(clij, temp1, buffer);

        if (buffer.getDimension() > 2) {
            // z
            clij.op().multiplyImageAndCoordinate(buffer, temp1, 2);
            double maxZ = clij.op().maximumOfAllPixels(temp1);
            double minZ = MinimumOfMaskedPixels.minimumOfMaskedPixels(clij, temp1, buffer);

            result = new double[]{minX, minY, minZ, maxX - minX + 1, maxY - minY + 1, maxZ - minZ + 1};
        } else {
            result = new double[]{minX, minY, maxX - minX + 1, maxY - minY + 1};
        }
        return  result;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source1, Image source2";
    }

    @Override
    public String getDescription() {
        return "Determines the bounding box of all non-zero pixels in a binary image. The positions will be stored in a new row of ImageJs\n" +
                "Results table in the column 'BoundingBoxX', 'BoundingBoxY', 'BoundingBoxWidth' and 'BoundingBoxHeight' in case of 2D images. " +
                "If you pass over a 3D image stack, also columns 'BoundingBoxZ' and 'BoundingBoxDepth' will be given.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
