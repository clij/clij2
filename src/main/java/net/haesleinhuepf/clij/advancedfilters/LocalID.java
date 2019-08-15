package net.haesleinhuepf.clij.advancedfilters;


import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.NewImage;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_localID")
public class LocalID extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image input, Image destination";
    }

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();
        boolean result = localID(clij, (ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]));
        releaseBuffers(args);
        return result;
    }

    public static boolean localID(CLIJ clij, ClearCLBuffer input1, ClearCLBuffer output) {

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", input1);
        parameters.put("localsizes", new long[]{4,4,4});
        parameters.put("dst", output);

        return clij.execute(LocalID.class, "localID.cl", "localID", parameters);
    }

    @Override
    public String getDescription() {
        return "local id";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }


    public static void main(String[] args) {
        new ImageJ();
        CLIJ clij = CLIJ.getInstance();

        ImagePlus imp = NewImage.createFloatImage("", 1024, 1024, 10, NewImage.FILL_RAMP);
        ClearCLBuffer input = clij.push(imp);
        ClearCLBuffer output = clij.create(input);

        localID(clij, input, output);

        clij.show(output, "result");
    }
}
