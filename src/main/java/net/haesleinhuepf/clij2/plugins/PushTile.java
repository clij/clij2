package net.haesleinhuepf.clij2.plugins;

import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.NewImage;
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

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_pushTile")
public class PushTile extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        String imageName = (String) args[0];
        int tileX = asInteger(args[1]);
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
            throw new IllegalArgumentException("You tried to push the image '" + args[0] + "' to the GPU.\n" +
                    "However, this image doesn't exist.");
        }

        ImagePlus imp = WindowManager.getImage(imageName);

        pushTile(getCLIJ2(), imp, imageName, tileX, tileY, tileZ, width, height, depth, marginWidth, marginHeight, marginDepth);

        return true;
    }

    public static ClearCLBuffer pushTile(CLIJ2 clij2, ImagePlus imp, Integer tileX, Integer tileY, Integer tileZ, Integer width, Integer height, Integer depth, Integer marginWidth, Integer marginHeight, Integer marginDepth) {
        Roi roi = imp.getRoi();
        int startX = tileX * width - marginWidth;
        int startY = tileY * height - marginHeight;
        int endX = (tileX + 1) * width + marginWidth - 1;
        int endY = (tileY + 1) * height + marginHeight - 1;

        if (startX < 0) {
            startX = 0;
        }
        if (startY < 0) {
            startY = 0;
        }
        if (endX >= imp.getWidth()) {
            endX = imp.getWidth() - 1;
        }
        if (endY >= imp.getHeight()) {
            endY = imp.getHeight() - 1;
        }

        imp.setRoi(startX, startY, endX - startX + 1, endY - startY + 1);

        int endZ = (tileZ + 1) * depth + marginDepth;
        int startZ = tileZ * depth - marginDepth;
        if (startZ < 0) {
            startZ = 0;
        }
        if (endZ >= imp.getNSlices()) {
            endZ = imp.getNSlices() - 1;
        }



//        System.out.println("Start z " + startZ);
//        System.out.println("End z   " + endZ);

        //ImagePlus tile = new Duplicator().run(imp, imp.getChannel(), imp.getChannel(), startZ + 1, endZ, imp.getFrame(), imp.getFrame());


        //////////////////

        System.out.println("pull " + tileX + "/" + tileY);
        ImagePlus tile = NewImage.createImage("temp", endX - startX + 1, endY - startY + 1, endZ - startZ + 1, imp.getBitDepth(), NewImage.FILL_BLACK);

        for (int z = startZ; z <= endZ; z++) {
            imp.setZ(z + 1);
            tile.setZ(z + 1 - startZ);
            tile.getProcessor().copyBits(imp.getProcessor().crop(), 0, 0, Blitter.COPY);
        }
        //////////////////


        //if (tileX == 0 && tileY == 0) {
        //    tile.show();
        //    tile.setTitle("tit " + startZ + " " + endZ);
        //}

        imp.setRoi(roi);
        ClearCLBuffer buffer = clij2.push(tile);
        System.out.println("push " + startX +"/" + startY + " - " + endX + "/" + endY + " sum " + clij2.sumOfAllPixels(buffer));
        return buffer;
    }

    public static void pushTile(CLIJ2 clij2, ImagePlus imp, String imageName, Integer tileX, Integer tileY, Integer tileZ, Integer width, Integer height, Integer depth, Integer marginWidth, Integer marginHeight, Integer marginDepth) {
        ClearCLBuffer buffer = pushTile(clij2, imp, tileX, tileY, tileZ, width, height, depth, marginWidth, marginHeight, marginDepth);
        CLIJHandler.getInstance().pushInternal(buffer, imageName);
    }

    public static ClearCLBuffer pushTile(CLIJ2 clij2, ClearCLBuffer input, Integer tileX, Integer tileY, Integer tileZ, Integer width, Integer height, Integer depth, Integer marginWidth, Integer marginHeight, Integer marginDepth) {
        long startX = tileX * width - marginWidth;
        long startY = tileY * height - marginHeight;
        long endX = (tileX + 1) * width + marginWidth - 1;
        long endY = (tileY + 1) * height + marginHeight - 1;

        if (startX < 0) {
            startX = 0;
        }
        if (startY < 0) {
            startY = 0;
        }
        if (endX >= input.getWidth()) {
            endX = input.getWidth() - 1;
        }
        if (endY >= input.getHeight()) {
            endY = input.getHeight() - 1;
        }

        long endZ = (tileZ + 1) * depth + marginDepth;
        long startZ = tileZ * depth - marginDepth;
        if (startZ < 0) {
            startZ = 0;
        }
        if (endZ >= input.getDepth()) {
            endZ = input.getDepth() - 1;
        }

        ClearCLBuffer output = clij2.create(new long[]{endX - startX + 1, endY - startY + 1, endZ - startZ + 1}, input.getNativeType());
        clij2.crop(input, output, startX, startY, startZ);
        return output;
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
        return "Push a tile in an image specified by its name, position and size to GPU memory in order to process it there later.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
