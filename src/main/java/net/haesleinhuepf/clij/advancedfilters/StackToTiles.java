package net.haesleinhuepf.clij.advancedfilters;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_stackToTiles")
public class StackToTiles extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();
        boolean result = stackToTiles(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asInteger(args[2]), asInteger(args[3]));
        releaseBuffers(args);
        return result;
    }

    public static boolean stackToTiles(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer tiles_x, Integer tiles_y) {
        int count = 0;
        ClearCLBuffer temp = clij.create(new long[]{src.getWidth(), src.getHeight()}, src.getNativeType());
        for (int x = 0; x < tiles_x; x++) {
            for (int y = 0; y < tiles_y; y++) {
                clij.op().copySlice(src, temp, count);
                //clij.show(temp, "temp");
                Paste3D.paste(clij, temp, dst, (int)(tiles_x * src.getWidth()), (int)(tiles_y * src.getHeight()), 0);
                //clij.show(dst, "dst");
                count++;
            }
            temp.close();
            return true;
        }

        temp.close();
        return true;
    }

    public static boolean stackToTiles(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer tiles_x, Integer tiles_y) {
        int count = 0;
        ClearCLImage temp = clij.create(new long[]{src.getWidth(), src.getHeight()}, src.getChannelDataType());
        for (int x = 0; x < tiles_x; x++) {
            for (int y = 0; y < tiles_y; y++) {
                clij.op().copySlice(src, temp, count);
                //clij.show(temp, "temp");
                Paste2D.paste(clij, temp, dst, (int)(tiles_x * src.getWidth()), (int)(tiles_y * src.getHeight()));
                //clij.show(dst, "dst");
                count++;
            }
            temp.close();
            return true;
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
