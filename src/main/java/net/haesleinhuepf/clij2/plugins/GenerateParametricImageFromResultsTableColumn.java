package net.haesleinhuepf.clij2.plugins;


import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_generateParametricImageFromResultsTableColumn")
public class GenerateParametricImageFromResultsTableColumn extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image label_map, ByRef Image parametric_image_destination, String column";
    }

    @Override
    public boolean executeCL() {
        boolean result = getCLIJ2().generateParametricImageFromResultsTableColumn((ClearCLBuffer) (args[0]),(ClearCLBuffer) (args[1]), ResultsTable.getResultsTable(), args[2].toString());
        return result;
    }

    public static boolean generateParametricImageFromResultsTableColumn(CLIJ2 clij2, ClearCLImageInterface label_map, ClearCLImageInterface parametric_image_destination, ResultsTable table, String columnName) {
        if ( table == null ) {
            throw new IllegalArgumentException("GenerateParametricImageFromResultsTableColumn error: There is no table open.");
        }
        ClearCLBuffer parameter_value_vector = clij2.create(table.size(), 1, 1);
        clij2.pushResultsTableColumn(parameter_value_vector, table, columnName);
        clij2.replaceIntensities(parameter_value_vector, label_map, parametric_image_destination);
        return true;
    }

    @Override
    public String getDescription() {
        return "Take a labelmap and a column from the results table to replace label 1 with the 1st value in the vector. \n\n" +
                "Note that indexing in the table column starts at zero. The results table should contain a line at the beginning" +
                "representing the background.\n";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
