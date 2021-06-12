package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.enums.ImageChannelDataType;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_voronoiOtsuLabeling")
public class VoronoiOtsuLabeling extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Image";
    }

    @Override
    public String getOutputType() {
        return "Label Image";
    }

    @Override
    public Object[] getDefaultValues() {
        return new Object[]{null, null, 2, 2};
    }

    @Override
    public String getParameterHelpText() {
        return "Image input, ByRef Image destination, Number spot_sigma, Number outline_sigma";
    }

    @Override
    public boolean executeCL() {
        return voronoiOtsuLabeling(getCLIJ2(), (ClearCLBuffer) args[0], (ClearCLBuffer) args[1], asFloat(args[2]), asFloat(args[3]));
    }

    public static boolean voronoiOtsuLabeling(CLIJ2 clij2, ClearCLBuffer pushed, ClearCLBuffer result, Float spot_sigma, Float outline_sigma) {
        ClearCLBuffer temp = clij2.create(pushed.getDimensions(), NativeTypeEnum.Float);
        clij2.gaussianBlur(pushed, temp, spot_sigma, spot_sigma, spot_sigma);

        ClearCLBuffer spots = clij2.create(pushed.getDimensions(), NativeTypeEnum.Byte);
        clij2.detectMaxima3DBox(temp, spots, 0, 0, 0);

        clij2.gaussianBlur(pushed, temp, outline_sigma, outline_sigma, outline_sigma);

        ClearCLBuffer segmentation = clij2.create(pushed.getDimensions(), NativeTypeEnum.Byte);
        clij2.thresholdOtsu(temp, segmentation);

        ClearCLBuffer binary = clij2.create(pushed.getDimensions(), NativeTypeEnum.Byte);
        clij2.binaryAnd(spots, segmentation, binary);
        MaskedVoronoiLabeling.maskedVoronoiLabeling(clij2, binary, segmentation, temp);
        clij2.mask(temp, segmentation, result);

        binary.close();
        segmentation.close();
        spots.close();
        temp.close();
        return true;
    }

    @Override
    public String getDescription() {
        return "Labeles objects directly from grey-value images.\n\n" +
                "The two sigma parameters allow tuning the segmentation result. " +
                "The first sigma controls how close detected cells can be (spot_sigma) and the second controls how " +
                "precise segmented objects are outlined (outline_sigma)." +
                "Under the hood, this filter applies two Gaussian blurs, spot detection, Otsu-thresholding and Voronoi-labeling.\n" +
                "The thresholded binary image is flooded using the Voronoi approach starting from the found local maxima.\n" +
                "Noise-removal sigma for spot detection and thresholding can be configured separately.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public String getCategories() {
        return "Label";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        return getCLIJ2().create(input.getDimensions(), NativeTypeEnum.Float);
    }
}
