package net.haesleinhuepf.clijx.plugins;


import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.imglib2.realtransform.AffineTransform3D;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_lfrecon")
public class LFRecon extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image input, Image destination, Number umax, Number vmax";
    }

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();
        boolean result = lfrecon(clij, (ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]), asInteger(args[2]), asInteger(args[3]));
        releaseBuffers(args);
        return result;
    }

    public static boolean lfrecon(CLIJ clij, ClearCLBuffer input, ClearCLBuffer output, Integer umax, Integer vmax) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", input);
        parameters.put("dst", output);
        parameters.put("umax", umax);
        parameters.put("vmax", vmax);
        return clij.execute(LFRecon.class, "lfrecon.cl", "reorder_views", parameters);
    }

    public static boolean lfrecon(CLIJ clij, ClearCLImage input, ClearCLImage output, Integer umax, Integer vmax) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", input);
        parameters.put("dst", output);
        parameters.put("umax", umax);
        parameters.put("vmax", vmax);
        return clij.execute(LFRecon.class, "lfrecon.cl", "reorder_views", parameters);
    }

    @Override
    public String getDescription() {
        return "LFRecon.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D -> 3D";
    }

    public static void main(String... args) {
        testExp();
    }

    public static void testExp() {
        new ImageJ();
        String filename = "C:\\structure\\data\\MBL_RudolfOldenburg\\GUV_LightFieldExperFocusMiddle.tif";
        int tilesize = 20;
        int num_tiles = 45;

        ImagePlus imp = IJ.openImage(filename);
        IJ.run(imp, "32-bit", "");
        imp.setRoi(17, 9, 866, 866);
        IJ.run(imp, "Crop", "");
        imp.show();

        int imagesize = imp.getWidth();

        CLIJ clij = CLIJ.getInstance();
        ClearCLImage input = clij.convert(imp, ClearCLImage.class);
        ClearCLImage temp = clij.create(new long[]{900, 900}, input.getChannelDataType());
        ClearCLImage output = clij.create(new long[]{imagesize / tilesize, imagesize / tilesize, tilesize * tilesize}, input.getChannelDataType());

        AffineTransform3D at = new AffineTransform3D();
        //at.scale(temp.getWidth() / input.getWidth());

        AffineTransform3D scaleTransform = new AffineTransform3D();
        scaleTransform.set(1.0,0,0);
        scaleTransform.set((double)temp.getHeight() / input.getHeight() , 1, 1);
        scaleTransform.set(1, 2, 2);
        at.concatenate(scaleTransform);

        scaleTransform = new AffineTransform3D();
        scaleTransform.set((double)temp.getWidth() / input.getWidth(),0,0);
        scaleTransform.set(1.0 , 1, 1);
        scaleTransform.set(1, 2, 2);
        at.concatenate(scaleTransform);

        clij.op().affineTransform3D(input, temp, at);

        clij.show(temp, "temp");

        lfrecon(clij, temp, output, tilesize, tilesize);

        clij.show(output, "output");

        System.out.println("sum output: " + clij.op().sumPixels(output));

        ClearCLImage tiles_output = clij.create(new long[]{output.getWidth() * tilesize, output.getHeight() * tilesize}, output.getChannelDataType());

        StackToTiles.stackToTiles(clij, output, tiles_output, tilesize, tilesize);
        System.out.println("sum output tiles: " + clij.op().sumPixels(tiles_output));

        clij.show(tiles_output, "tiles");

    }

    void testSim() {

        new ImageJ();

        String filename = "C:\\structure\\data\\MBL_RudolfOldenburg\\GUV_LightFieldSimFocusMiddle.tiff";
        int tilesize = 16;

        ImagePlus imp = IJ.openImage(filename);
        //imp.show();

        int imagesize = imp.getWidth();

        CLIJ clij = CLIJ.getInstance();
        ClearCLBuffer input = clij.push(imp);
        ClearCLBuffer output = clij.create(new long[]{imagesize / tilesize, imagesize / tilesize, tilesize * tilesize}, input.getNativeType());

        lfrecon(clij, input, output, tilesize, tilesize);

        clij.show(output, "output");
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        long width = input.getWidth();
        long height = input.getHeight();

        long[] dimensions = {width / asInteger(args[2]), height / asInteger(args[3]), asInteger(args[2]) * asInteger(args[3]) };

        return clij.create(dimensions, input.getNativeType());
    }
}
