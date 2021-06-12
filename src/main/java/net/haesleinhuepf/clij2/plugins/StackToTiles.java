package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.plugins.Paste3D;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_stackToTiles")
public class StackToTiles extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Image";
    }

    @Override
    public String getOutputType() {
        return "Image";
    }


    @Override
    public boolean executeCL() {
        boolean result = stackToTiles(getCLIJ2(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asInteger(args[2]), asInteger(args[3]));
        return result;
    }

    public static boolean stackToTiles(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst, Integer tiles_x, Integer tiles_y) {
        int count = 0;
        ClearCLBuffer temp = clij2.create(new long[]{src.getWidth(), src.getHeight()}, src.getNativeType());
        for (int x = 0; x < tiles_x; x++) {
            for (int y = 0; y < tiles_y; y++) {
                clij2.copySlice(src, temp, count);
                Paste3D.paste(clij2, temp, dst, (int)(tiles_x * src.getWidth()), (int)(tiles_y * src.getHeight()), 0);
                count++;
            }
        }

        temp.close();
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Image destination, Number tiles_x, Number tiles_y";
    }


    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        int tiles_x = asInteger(args[2]);
        int tiles_y = asInteger(args[3]);

        return clij.createCLBuffer(new long[]{input.getWidth() * tiles_x, input.getHeight() * tiles_y}, input.getNativeType());
    }

    @Override
    public String getDescription() {
        return "Stack to tiles.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D -> 2D";
    }
}
