package net.haesleinhuepf.clij2.plugins;

import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.Roi;
import ij.plugin.Duplicator;
import ij.process.Blitter;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJHandler;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 *         March 2020
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_pullTile")
public class PullTile extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        String imageName = (String) args[0];
        Integer tileX = asInteger(args[1]);
        int tileY = asInteger(args[2]);
        int tileZ = asInteger(args[3]);
        int width = asInteger(args[4]);
        int height = asInteger(args[5]);
        int depth = asInteger(args[6]);
        int marginWidth = asInteger(args[7]);
        int marginHeight = asInteger(args[8]);
        int marginDepth = asInteger(args[9]);

        if (WindowManager.getImage(imageName) == null) {
            //Macro.abort();
            throw new IllegalArgumentException("You tried to pull a tile to the image '" + args[0] + "'.\n" +
                    "However, this image doesn't exist. Create it with the newImage command before pulling tiles to it.");
        }

        ImagePlus imp = WindowManager.getImage(imageName);

        pullTile(getCLIJ2(), imp, imageName, tileX, tileY, tileZ, width, height, depth, marginWidth, marginHeight, marginDepth);

        return true;
    }

    public static void pullTile(CLIJ2 clij2, ImagePlus imp, String imageName, Integer tileX, Integer tileY, Integer tileZ, Integer width, Integer height, Integer depth, Integer marginWidth, Integer marginHeight, Integer marginDepth) {
        ClearCLBuffer buffer = CLIJHandler.getInstance().getFromCache(imageName);
        pullTile(clij2, imp, buffer, tileX, tileY, tileZ, width, height, depth, marginWidth, marginHeight, marginDepth);
    }

    public static void pullTile(CLIJ2 clij2, ImagePlus imp, ClearCLBuffer buffer, Integer tileX, Integer tileY, Integer tileZ, Integer width, Integer height, Integer depth, Integer marginWidth, Integer marginHeight, Integer marginDepth) {
        Roi roiBefore = imp.getRoi();
        int zBefore = imp.getZ();
        //imp.setRoi(tileX * width, tileY * height, width, height);

        int zEnd = (tileZ + 1) * depth;
        int zStart = tileZ * depth;

        ImagePlus tileSource = clij2.pull(buffer);
        System.out.println("w " + tileSource.getWidth());

        if (tileX == 0) {
            marginWidth = 0;
        }
        if (tileY == 0) {
            marginHeight = 0;
        }
        if (tileZ == 0) {
            marginDepth = 0;
        }
        tileSource.setRoi(marginWidth, marginHeight, width, height);
        //if (tileX == 0 && tileY == 0) {
        //    tileSource.show();
            //System.out.println("zi " + imp.getZ() + " zt " + tileSource.getZ());
        //}

        System.out.println("pull " + tileX + "/" + tileY);

        for (int z = zStart; z <= zEnd; z++) {
            imp.setZ(z + 1);
            tileSource.setZ(z + marginDepth + 1 - zStart);
            //if (tileX == 0 && tileY == 0) {
            //    System.out.println("pullcopy from " + (z + marginDepth + 1 - zStart) + " to " + (z + 1));
            //}
            imp.getProcessor().copyBits(tileSource.getProcessor().crop(), tileX * width, tileY * height, Blitter.COPY);
        }

        imp.setRoi(roiBefore);
        imp.setZ(zBefore);
    }

    public static void pullTile(CLIJ2 clij2, ClearCLBuffer input, ClearCLBuffer output, Integer tileX, Integer tileY, Integer tileZ, Integer width, Integer height, Integer depth, Integer marginWidth, Integer marginHeight, Integer marginDepth) {

        ClearCLBuffer temp = clij2.create(new long[]{width, height, depth}, input.getNativeType());

        int zEnd = (tileZ + 1) * depth;
        int zStart = tileZ * depth;

        if (tileX == 0) {
            marginWidth = 0;
        }
        if (tileY == 0) {
            marginHeight = 0;
        }
        if (tileZ == 0) {
            marginDepth = 0;
        }

        clij2.crop(input, temp, marginWidth, marginHeight, marginDepth);
        clij2.paste(temp, output, tileX * width, tileY * height, tileZ * depth);

        clij2.release(temp);
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {

        int width = asInteger(args[4]);
        int height = asInteger(args[5]);
        int depth = asInteger(args[6]);

        if (input.getDimension() == 2) {
            return getCLIJ2().create(new long[]{width, height}, input.getNativeType());
        } else {
            return getCLIJ2().create(new long[]{width, height, depth}, input.getNativeType());
        }
    }

    @Override
    public String getParameterHelpText() {
        return "String image, Number tileIndexX, Number tileIndexY, Number tileIndexZ, Number width, Number height, Number depth, Number marginWidth, Number marginHeight, Number marginDepth";
    }

    @Override
    public String getDescription() {
        return "Pushes a tile in an image specified by its name, position and size from GPU memory.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
