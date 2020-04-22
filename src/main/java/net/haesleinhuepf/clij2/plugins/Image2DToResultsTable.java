package net.haesleinhuepf.clij2.plugins;

import ij.ImagePlus;
import ij.measure.ResultsTable;
import ij.process.ImageProcessor;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * June 2019
 */
@Deprecated
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_image2DToResultsTable")
public class Image2DToResultsTable extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        ClearCLBuffer buffer = (ClearCLBuffer)( args[0]);
        ResultsTable table = ResultsTable.getResultsTable();
        image2DToResultsTable(getCLIJ2(), buffer, table);
        table.show("Results");
        return true;
    }

    @Deprecated
    public static ResultsTable image2DToResultsTable(CLIJ2 clij2, ClearCLBuffer buffer, ResultsTable table) {
        ImagePlus converted = clij2.pull(buffer);
        ImageProcessor ip = converted.getProcessor();
        return image2DToResultsTable(ip, table);
    }

    @Deprecated
    public static ResultsTable image2DToResultsTable(CLIJ2 clij2, ClearCLImage image, ResultsTable table) {
        ImagePlus converted = clij2.convert(image, ImagePlus.class);
        ImageProcessor ip = converted.getProcessor();
        return image2DToResultsTable(ip, table);
    }

    @Deprecated
    static ResultsTable image2DToResultsTable(ImageProcessor ip, ResultsTable table) {
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
