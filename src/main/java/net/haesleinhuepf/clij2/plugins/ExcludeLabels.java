package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

import java.nio.FloatBuffer;
import java.util.HashMap;


/**
 * Author: @haesleinhuepf
 *         April 2020
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_excludeLabels")
public class ExcludeLabels extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        ClearCLBuffer flaglist = (ClearCLBuffer)(args[0]);
        ClearCLBuffer label_map_in = (ClearCLBuffer)( args[1]);
        ClearCLBuffer label_map_out = (ClearCLBuffer)( args[2]);

        return getCLIJ2().excludeLabels(flaglist, label_map_in, label_map_out);
    }

    public static boolean excludeLabels(CLIJ2 clij2, ClearCLBuffer flaglist, ClearCLBuffer label_map_in, ClearCLBuffer label_map_out) {
        int max_label = (int)flaglist.getWidth();
        if (max_label == 0) {
            clij2.set(label_map_out, 0f);
            return true;
        }

        ClearCLBuffer newIndexList = clij2.create(flaglist.getDimensions(), NativeTypeEnum.Float);
        clij2.copy(flaglist, newIndexList);

        float[] label_indices = new float[(int) flaglist.getWidth()];
        newIndexList.writeTo(FloatBuffer.wrap(label_indices), true);

        int count = 1;
        for (int i = 0; i < label_indices.length; i++) {
            //System.out.print("i " + i + " " + label_indices[i]);
            if (i > 0 && label_indices[i] == 0) {
                //System.out.println(" keep");
                label_indices[i] = count;
                count++;
            } else {
                //System.out.println(" exclude");
                label_indices[i] = 0;
            }
        }

        newIndexList.readFrom(FloatBuffer.wrap(label_indices), true);

        clij2.replaceIntensities(label_map_in, newIndexList, label_map_out);

        newIndexList.close();

        return true;
    }


    @Override
    public String getParameterHelpText() {
        return "Image binary_flaglist, Image label_map_input, ByRef Image label_map_destination";
    }

    @Override
    public String getDescription() {
        return "This operation removes labels from a labelmap and renumbers the remaining labels. \n\n" +
                "Hand over a binary flag list vector starting with a flag for the background, continuing with label1, label2, ...\n\n" +
                "For example if you pass 0,1,0,0,1: Labels 1 and 4 will be removed (those with a 1 in the vector will be excluded). Labels 2 and 3 will be kept and renumbered to 1 and 2.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        ClearCLBuffer labelmap =(ClearCLBuffer)( args[1]);
        return getCLIJ2().create(labelmap);
    }
}
