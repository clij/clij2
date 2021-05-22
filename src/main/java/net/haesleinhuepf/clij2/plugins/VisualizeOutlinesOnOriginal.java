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

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_visualizeOutlinesOnOriginal")
public class VisualizeOutlinesOnOriginal extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Label Image";
    }

    @Override
    public String getOutputType() {
        return "Image";
    }

    @Override
    public String getParameterHelpText() {
        return "Image intensity, Image labels, ByRef Image destination";
    }

    @Override
    public boolean executeCL() {
        return visualizeOutlinesOnOriginal(getCLIJ2(), (ClearCLBuffer) args[0], (ClearCLBuffer) args[1], (ClearCLBuffer) args[2]);
    }

    public static boolean visualizeOutlinesOnOriginal(CLIJ2 clij2, ClearCLBuffer intensity, ClearCLBuffer labels, ClearCLBuffer result) {
        ClearCLBuffer temp = clij2.create(labels.getDimensions(), NativeTypeEnum.UnsignedByte);
        clij2.detectLabelEdges(labels, temp);

        double max_intensity = clij2.maximumOfAllPixels(intensity);

        ClearCLBuffer temp1 = clij2.create(intensity);
        clij2.multiplyImageAndScalar(temp, temp1, max_intensity + 1);
        temp.close();

        clij2.maximumImages(temp1, intensity, result);
        temp1.close();
        return true;
    }

    @Override
    public String getDescription() {
        return "Combines an intensity image and a label (or binary) image so that you can see segmentation outlines on the intensity image.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public String getCategories() {
        return "Label,Math";
    }
}
