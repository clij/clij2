package net.haesleinhuepf.clijx.matrix;


import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_labelSpots")
public class LabelSpots extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image input_spots, Image labelled_spots_destination";
    }

    @Override
    public boolean executeCL() {
        return labelSpots(getCLIJx(), (ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]));
    }

    public static boolean labelSpots(CLIJx clijx, ClearCLBuffer input, ClearCLBuffer output) {
        /*
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.clear();
        parameters.put("src", input);
        parameters.put("dst", output);

        long[] globalSizes = new long[]{1, 1, 1};

        clijx.execute(LabelSpots.class, "label_spots_" + input.getDimension() + "d_x.cl", "label_spots_" + input.getDimension() + "d", input.getDimensions(), globalSizes, parameters);
        */
        ClearCLBuffer buffer = clijx.create(input.getDimensions(), clijx.Float);
        clijx.setNonZeroPixelsToPixelIndex(input, buffer);
        clijx.shiftIntensitiesToCloseGaps(buffer, output);
        return true;




        /*
        ClearCLBuffer temp = null;
        if (input.getNativeType() != clijx.Float) {
            temp = clijx.create(input.getDimensions(), NativeTypeEnum.Float);
            clijx.copy(input, temp);
            input = temp;
        }

        int count = 0;
        ImagePlus spots = clijx.pull(input);
        if (temp != null) {
            temp.close();
        }

        for (int z = 0; z < spots.getNSlices(); z++) {
            spots.setZ(z + 1);
            float[] buffer = (float[]) ((FloatProcessor)spots.getProcessor()).getPixels();
            for (int i = 0; i < buffer.length; i++) {
                if (buffer[i] != 0) {
                    buffer[i] = count;
                    count++;
                }
            }
        }

        ClearCLBuffer result = clijx.push(spots);
        clijx.copy(result, output);
        clijx.release(result);
        */
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        return getCLIJx().create(input.getDimensions(), getCLIJx().Float);
    }

    @Override
    public String getDescription() {
        return "Transforms a spots image as resulting from maximum/minimum detection in an image of the same size where every spot has " +
                "a number 1, 2, ... n.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
