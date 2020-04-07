package net.haesleinhuepf.clij2.plugins;


import ij.ImagePlus;
import ij.process.FloatProcessor;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_labelSpots")
public class LabelSpots extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image input_spots, ByRef Image labelled_spots_destination";
    }

    @Override
    public boolean executeCL() {
        return labelSpots(getCLIJ2(), (ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]));
    }

    public static boolean labelSpots(CLIJ2 clij2, ClearCLBuffer input, ClearCLBuffer output) {
        /*
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.clear();
        parameters.put("src", input);
        parameters.put("dst", output);

        long[] globalSizes = new long[]{1, 1, 1};

        clij2.execute(LabelSpots.class, "label_spots_" + input.getDimension() + "d_x.cl", "label_spots_" + input.getDimension() + "d", input.getDimensions(), globalSizes, parameters);
        */
        //ClearCLBuffer buffer = clij2.create(input.getDimensions(), clij2.Float);
        //clij2.setNonZeroPixelsToPixelIndex(input, buffer);
        //clij2.closeIndexGapsInLabelMap(buffer, output);

        //ClearCLBuffer temp = clij2.create(new long[]{input.getDepth(), input.getHeight()}, input.getNativeType());
        //clij2.sumXProjection(input, temp);

        //clij2.sumImageSliceBySlice(input);


        ClearCLBuffer spotCountPerX = clij2.create(new long[]{input.getDepth(), input.getHeight()});
        clij2.sumXProjection(input, spotCountPerX);

        ClearCLBuffer spotCountPerXY = clij2.create(new long[]{input.getDepth(), input.getHeight()});
        clij2.sumYProjection(spotCountPerX, spotCountPerXY);

        long[] dims = new long[]{1, input.getHeight(), input.getDepth()};
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("dst", output);
        parameters.put("src", input);
        parameters.put("spotCountPerX", spotCountPerX);
        parameters.put("spotCountPerXY", spotCountPerXY);
        clij2.execute(LabelSpots.class, "label_spots_in_x.cl", "label_spots_in_x", dims, dims, parameters);

        spotCountPerX.close();
        spotCountPerXY.close();

        //clij2.show(output, "labelled spots");

        /*
        long time = System.currentTimeMillis();
        clij2.sumImageSliceBySlice(input);
        System.out.println("slibisli " + (System.currentTimeMillis() - time));

        ClearCLBuffer inputFloat = input;
        if(inputFloat.getNativeType() != NativeTypeEnum.Float) {
            inputFloat = clij2.create(input.getDimensions(), NativeTypeEnum.Float);
            clij2.copy(input, inputFloat);
        }


        time = System.currentTimeMillis();
        ImagePlus imp = clij2.pull(inputFloat);
        System.out.println("psuh " + (System.currentTimeMillis() - time));


        int count = 0;
        for (int z = 0; z < imp.getNSlices(); z++) {
            imp.setZ(z);
            FloatProcessor fp = (FloatProcessor) imp.getProcessor();
            float[] pixels = (float[]) fp.getPixels();
            for (int i = 0; i < pixels.length; i++ ) {
                if (i > 0) {
                    count ++;
                    pixels[i] = count;
                }
            }
        }

        time = System.currentTimeMillis();
        ClearCLBuffer temp = clij2.push(imp);
        System.out.println("pllu " + (System.currentTimeMillis() - time));

        time = System.currentTimeMillis();
        clij2.copy(temp, output);
        System.out.println("copy " + (System.currentTimeMillis() - time));

        temp.close();

        if (inputFloat != input) {
            inputFloat.close();
        }*/

        return true;




        /*
        ClearCLBuffer temp = null;
        if (input.getNativeType() != clij2.Float) {
            temp = clij2.create(input.getDimensions(), NativeTypeEnum.Float);
            clij2.copy(input, temp);
            input = temp;
        }

        int count = 0;
        ImagePlus spots = clij2.pull(input);
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

        ClearCLBuffer result = clij2.push(spots);
        clij2.copy(result, output);
        clij2.release(result);
        */
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        return getCLIJ2().create(input.getDimensions(), getCLIJ2().Float);
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
