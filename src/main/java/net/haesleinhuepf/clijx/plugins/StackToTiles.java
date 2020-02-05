package net.haesleinhuepf.clijx.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.plugins.Paste3D;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_stackToTiles")
public class StackToTiles extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();
        boolean result = stackToTiles(getCLIJx(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asInteger(args[2]), asInteger(args[3]));
        releaseBuffers(args);
        return result;
    }

    public static boolean stackToTiles(CLIJx clijx, ClearCLImageInterface src, ClearCLImageInterface dst, Integer tiles_x, Integer tiles_y) {
        int count = 0;
        ClearCLBuffer temp = clijx.create(new long[]{src.getWidth(), src.getHeight()}, src.getNativeType());
        for (int x = 0; x < tiles_x; x++) {
            for (int y = 0; y < tiles_y; y++) {
                clijx.copySlice(src, temp, count);
                //clij.show(temp, "temp");
                Paste3D.paste(clijx, temp, dst, (int)(tiles_x * src.getWidth()), (int)(tiles_y * src.getHeight()), 0);
                //clij.show(dst, "dst");
                count++;
            }
        }

        temp.close();
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number tiles_x, Number tiles_y";
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
