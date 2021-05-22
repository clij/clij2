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

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_labelSurface")
public class LabelSurface extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
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
        return "Image input_labels, ByRef Image destination_labels, Number relative_center_x, Number relative_center_y, Number relative_center_z";
    }

    @Override
    public Object[] getDefaultValues() {
        return new Object[] {null, null, 0.5, 0.5, 0.5};
    }

    @Override
    public boolean executeCL() {
        return labelSurface(getCLIJ2(), (ClearCLBuffer) args[0], (ClearCLBuffer) args[1], asFloat(args[2]), asFloat(args[3]), asFloat(args[4]));
    }

    public static boolean labelSurface(CLIJ2 clij2, ClearCLBuffer pushed, ClearCLBuffer result, Float relative_center_x, Float relative_center_y, Float relative_center_z) {
        int number_of_labels = (int)clij2.maximumOfAllPixels(pushed);
        ClearCLBuffer positions = clij2.create(number_of_labels + 1,pushed.getDimension(), 1);

        clij2.centroidsOfLabels(pushed, positions);

        clij2.excludeLabelsSubSurface(positions, pushed, result,
                pushed.getWidth() * relative_center_x,
                pushed.getHeight() * relative_center_y,
                pushed.getDepth() * relative_center_z);

        positions.close();

        return true;
    }

    @Override
    public String getDescription() {
        return "Takes a label map and excludes all labels which are not on the surface.\n\n" +
                "For each label, a ray from a given center towards the label. If the ray crosses another label, the label" +
                "in question is not at the surface and thus, removed.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public String getCategories() {
        return "Label, Measurements";
    }
}
