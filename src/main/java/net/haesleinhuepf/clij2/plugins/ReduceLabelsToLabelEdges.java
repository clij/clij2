package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_reduceLabelsToLabelEdges")
public class ReduceLabelsToLabelEdges extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
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
        return reduceLabelsToLabelEdges(getCLIJ2(), (ClearCLBuffer) args[0], (ClearCLBuffer) args[1]);
    }

    public static boolean reduceLabelsToLabelEdges(CLIJ2 clij2, ClearCLBuffer input_labels, ClearCLBuffer destination_labels) {
        ClearCLBuffer label_edges = clij2.create(input_labels.getDimensions(), NativeTypeEnum.UnsignedByte);
        clij2.detectLabelEdges(input_labels, label_edges);
        clij2.mask(input_labels, label_edges, destination_labels);
        return true;
    }

    @Override
    public String getDescription() {
        return "Takes a label map and reduces all labels to their edges. Label IDs stay the same and background will be zero.";
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
