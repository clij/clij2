package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_reduceLabelsToCentroids")
public class ReduceLabelsToCentroids extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Label Image";
    }

    @Override
    public String getOutputType() {
        return "Label Image";
    }

    @Override
    public String getParameterHelpText() {
        return "Image input_labels, ByRef Image destination_labels";
    }

    @Override
    public boolean executeCL() {
        return reduceLabelsToCentroids(getCLIJ2(), (ClearCLBuffer) args[0], (ClearCLBuffer) args[1]);
    }

    public static boolean reduceLabelsToCentroids(CLIJ2 clij2, ClearCLBuffer pushed, ClearCLBuffer result) {
        int number_of_labels = (int)clij2.maximumOfAllPixels(pushed);
        ClearCLBuffer positions = clij2.create(number_of_labels + 1,pushed.getDimension(), 1);

        clij2.centroidsOfBackgroundAndLabels(pushed, positions);

        ClearCLBuffer positions_and_labels = clij2.create(number_of_labels + 1,pushed.getDimension() + 1, 1);
        clij2.setRampX(positions_and_labels);
        clij2.setColumn(positions, 0, -1); // prevent putting a 0 at the centroid position of the background
        clij2.paste(positions, positions_and_labels, 0, 0);

        clij2.set(result, 0);
        clij2.writeValuesToPositions(positions_and_labels, result);

        positions.close();
        positions_and_labels.close();

        return true;
    }

    @Override
    public String getDescription() {
        return "Takes a label map and reduces all labels to their center spots. Label IDs stay and background will be zero.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public String getCategories() {
        return "Label";
    }
}
