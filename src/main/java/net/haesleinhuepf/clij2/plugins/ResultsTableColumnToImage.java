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

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.FloatBuffer;

/**
 * Author: @haesleinhuepf
 *         April 2020
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_resultsTableColumnToImage")
public class ResultsTableColumnToImage extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        ClearCLBuffer buffer = (ClearCLBuffer)( args[0]);
        String column = (String)( args[1]);
        ResultsTable table = ResultsTable.getResultsTable();
        resultsTableColumnToImage(getCLIJ2(), buffer, table, column);
        return true;
    }

    public static boolean resultsTableColumnToImage(CLIJ2 clij2, ClearCLBuffer buffer, ResultsTable table, String column) {
        ClearCLBuffer temp = clij2.create(new long[]{table.getCounter(), 1, 1}, NativeTypeEnum.Float);

        float[] columnData = table.getColumn(table.getColumnIndex(column));

        FloatBuffer arrayBuffer = FloatBuffer.wrap(columnData);
        temp.readFrom(arrayBuffer, true);

        clij2.copy(temp, buffer);

        temp.close();

        return true;
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        ResultsTable table = ResultsTable.getResultsTable();
        return clij.create(new long[]{table.getCounter(), 1, 1}, NativeTypeEnum.Float);
    }

    @Override
    public String getParameterHelpText() {
        return "ByRef Image destination, String column_name";
    }

    @Override
    public String getDescription() {
        return "Converts a table column to an image. The values are stored in x dimension.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }

}
