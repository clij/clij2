package net.haesleinhuepf.clijx.plugins;

import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * June 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_getSize")
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
        return "Reads out the size of an image [stack] and writes it to the" +
                " results table in the columns 'Width', 'Height' and 'Depth'.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
