package net.haesleinhuepf.clij.advancedfilters;

import ij.ImagePlus;
import ij.measure.ResultsTable;
import ij.process.ImageProcessor;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * June 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_image2DToResultsTable")
public class Image2DToResultsTable extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {

        Object[] args = openCLBufferArgs();

        ClearCLBuffer buffer = (ClearCLBuffer)( args[0]);

        ResultsTable table = ResultsTable.getResultsTable();
        image2DToResultsTable(clij, buffer, table);
        table.show("Results");
        return true;
    }

    public static ResultsTable image2DToResultsTable(CLIJ clij, ClearCLBuffer buffer, ResultsTable table) {
        ImagePlus converted = clij.pull(buffer);
        ImageProcessor ip = converted.getProcessor();
        return image2DToResultsTable(ip, table);
    }
    public static ResultsTable image2DToResultsTable(CLIJ clij, ClearCLImage image, ResultsTable table) {
        ImagePlus converted = clij.convert(image, ImagePlus.class);
        ImageProcessor ip = converted.getProcessor();
        return image2DToResultsTable(ip, table);
    }

    private static ResultsTable image2DToResultsTable(ImageProcessor ip, ResultsTable table) {
        if (table == null) {
            table = ResultsTable.getResultsTable();
        }

        for (int y = 0; y < ip.getHeight(); y++ ) {
            table.incrementCounter();
            for (int x = 0; x < ip.getWidth(); x++ ) {
                table.addValue("X" + x, ip.getPixelValue(x, y));
            }
        }

        return table;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source";
    }

    @Override
    public String getDescription() {
        return "Converts an image into a table.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }

}
