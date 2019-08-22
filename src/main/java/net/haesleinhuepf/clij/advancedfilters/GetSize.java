package net.haesleinhuepf.clij.advancedfilters;

import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCL;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.imglib2.Cursor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

/**
 * Author: @haesleinhuepf
 * June 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_getSize")
public class GetSize extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        ClearCLBuffer buffer = (ClearCLBuffer)( args[0]);

        ResultsTable table = ResultsTable.getResultsTable();
        table.incrementCounter();
        table.addValue("Width", buffer.getWidth());
        table.addValue("Height", buffer.getHeight());
        if (buffer.getDimension() > 2) {
            table.addValue("Depth", buffer.getDepth());
        }
        table.show("Results");
        return true;
    }

    public static long[] getSize(CLIJ clij, ClearCLBuffer buffer) {
        if (buffer.getDimension() == 3) {
            return new long[]{buffer.getWidth(), buffer.getHeight(), buffer.getDepth()};
        } else {
            return new long[]{buffer.getWidth(), buffer.getHeight()};
        }
    }



    @Override
    public String getParameterHelpText() {
        return "Image source";
    }

    @Override
    public String getDescription() {
        return "Determines the number of all pixels in a given image which are not equal to 0. It will be stored in a new row of ImageJs\n" +
                "Results table in the column 'Count_non_zero'.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
