package net.haesleinhuepf.clij2.plugins;

import ij.IJ;
import ij.macro.Variable;
import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

import java.nio.FloatBuffer;

/**
 * Author: @haesleinhuepf
 *         January 2021
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_pullToResultsTableColumn")
public class PullToResultsTableColumn extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        ClearCLBuffer buffer = (ClearCLBuffer)( args[0]);
        String column = (String)( args[1]);
        Boolean append = asBoolean(args[2]);
        ResultsTable table = ResultsTable.getResultsTable();
        pullToResultsTableColumn(getCLIJ2(), buffer, table, column, append);
        table.show("Results");
        return true;
    }

    public static boolean pullToResultsTableColumn(CLIJ2 clij2, ClearCLBuffer buffer, ResultsTable table, String column, Boolean append) {
        long time = System.currentTimeMillis();
        int column_index = table.getColumnIndex(column);
        if (column_index == -1) {
            table.addValue(column, 0);
            column_index = table.getColumnIndex(column);
        }
        ClearCLBuffer temp = clij2.create(new long[]{buffer.getWidth(), 1, 1}, NativeTypeEnum.Float);
        clij2.crop(buffer, temp, 0, 0, 0);

        float[] columnData = new float[(int)buffer.getWidth()];
        FloatBuffer arrayBuffer = FloatBuffer.wrap(columnData);
        temp.writeTo(arrayBuffer, true);
        temp.close();

        System.out.println("pull took " + (System.currentTimeMillis() - time));
        time = System.currentTimeMillis();

        int starting_row = 0;
        if (append) {
            starting_row = table.size();
        } else {
            starting_row = table.size() - (int)buffer.getWidth();
        }
        if(starting_row < 0) {
            starting_row = 0;
        }
        for (int i = 0; i < buffer.getWidth(); i++) {
            table.setValue(column_index, i + starting_row, columnData[i]);
        }
        System.out.println("filling the table took " + (System.currentTimeMillis() - time));

        return true;
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        ResultsTable table = ResultsTable.getResultsTable();
        return clij.create(new long[]{table.getCounter(), 1, 1}, NativeTypeEnum.Float);
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, String column_name, Boolean append_new_rows";
    }

    @Override
    public String getDescription() {
        return "Copies the content of a vector image to a column in the results table.\n" +
                "You can configure if new rows should be appended or if existing values should be overwritten.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }

}
