package net.haesleinhuepf.clij2.plugins;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clijx.CLIJx;
import org.scijava.plugin.Plugin;

import java.nio.FloatBuffer;
import java.util.HashMap;


/**
 * Author: @haesleinhuepf
 * June 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_excludeLabelsSubSurface")
public class ExcludeLabelsSubSurface extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        ClearCLBuffer pointlist = (ClearCLBuffer)(args[0]);
        ClearCLBuffer label_map_in = (ClearCLBuffer)( args[1]);
        ClearCLBuffer label_map_out = (ClearCLBuffer)( args[2]);
        float centerX = asFloat(args[3]);
        float centerY = asFloat(args[4]);
        float centerZ = asFloat(args[5]);

        return excludeLabelsSubSurface(getCLIJ2(), pointlist, label_map_in, label_map_out, centerX, centerY, centerZ);
    }

    public static boolean excludeLabelsSubSurface(CLIJ2 clij2, ClearCLBuffer pointlist, ClearCLBuffer label_map_in, ClearCLBuffer label_map_out, Float centerX, Float centerY, Float centerZ) {
        int max_label = (int)pointlist.getWidth();
        if (max_label == 0) {
            clij2.set(label_map_out, 0f);
            return true;
        }

        ClearCLBuffer label_index_map = clij2.create(new long[]{max_label, 1, 1}, clij2.Float);
        clij2.setRampX(label_index_map);



        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("src_pointlist", pointlist);
        parameters.put("src_label_map", label_map_in);
        parameters.put("label_index_dst", label_index_map);
        parameters.put("centerX", centerX);
        parameters.put("centerY", centerY);
        if (label_map_in.getDimension() == 3) {
            parameters.put("centerZ", centerZ);
        }

        long[] globalSizes = new long[]{pointlist.getWidth(), 1, 1};
        clij2.execute(ExcludeLabelsSubSurface.class, "exclude_labels_sub_surface_" + label_map_in.getDimension() + "d_x.cl", "exclude_labels_sub_surface_" + label_map_in.getDimension() + "d", label_index_map.getDimensions(), globalSizes, parameters);

        float[] label_indices = new float[(int) label_index_map.getWidth()];
        label_index_map.writeTo(FloatBuffer.wrap(label_indices), true);

        int count = 1;
        for (int i = 0; i < label_indices.length; i++) {
            if (label_indices[i] > 0) {
                label_indices[i] = count;
                count++;
            }
        }

        label_index_map.readFrom(FloatBuffer.wrap(label_indices), true);

        clij2.replaceIntensities(label_map_in, label_index_map, label_map_out);

        return true;
    }

    public static void main(String... args) {
        CLIJ2 clij2 = CLIJ2.getInstance();
        ImagePlus imp = IJ.openImage("src/test/resources/blobs.tif");

        ClearCLBuffer image = clij2.push(imp);
        ClearCLBuffer binary = clij2.create(image);
        ClearCLBuffer labelmap = clij2.create(image);
        ClearCLBuffer labelTemp = clij2.create(image);
        ClearCLBuffer surfaceLabels = clij2.create(image);

        clij2.automaticThreshold(image,binary, "Otsu");
        clij2.connectedComponentsLabeling(binary, labelmap);

        ClearCLBuffer flag = clij2.create(1,1,1);
        for (int i = 0; i < 20; i++) {
            clij2.onlyzeroOverwriteMaximumBox(labelmap, flag, labelTemp);
            clij2.onlyzeroOverwriteMaximumDiamond(labelTemp, flag, labelmap);
        }
        clij2.show(labelmap, "labelamp");

        int numberOfPoints = (int) clij2.maximumOfAllPixels(labelmap);
        ClearCLBuffer pointlist = clij2.create(numberOfPoints, 2);
        clij2.centroidsOfLabels(labelmap, pointlist);
        new ImageJ();

        excludeLabelsSubSurface(clij2, pointlist, labelmap, surfaceLabels, 128f, 128f, 0f);

        clij2.show(surfaceLabels, "surf");
    }

    @Override
    public String getParameterHelpText() {
        return "Image pointlist, Image label_map_input, Image label_map_destination, Number centerX, Number centerY, Number centerZ";
    }

    @Override
    public String getDescription() {
        return "This operation follows a ray from a given position towards a label (or opposite direction) and checks if " +
                " there is another label between the label an the image border. If yes, this label is eliminated from" +
                " the label map.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        return clij.create(input.getDimensions(), NativeTypeEnum.Float);
    }
}
