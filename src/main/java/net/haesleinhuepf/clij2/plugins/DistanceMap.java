package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

import java.nio.FloatBuffer;
import java.util.HashMap;

/**
 * Author: @haesleinhuepf
 *         September 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_distanceMap")
public class DistanceMap extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized {

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();

        ClearCLBuffer src = (ClearCLBuffer)( args[0]);
        ClearCLBuffer dst = (ClearCLBuffer)( args[1]);

        getCLIJ2().distanceMap(src, dst);

        return true;
    }

    public static boolean distanceMap(CLIJ2 clij2, ClearCLBuffer src, ClearCLBuffer dst) {
        ClearCLBuffer flag = clij2.create(new long[]{1,1,1});
        float[] flagValue = {1};

        ClearCLBuffer temp1 = clij2.create(dst.getDimensions(), NativeTypeEnum.Float);
        ClearCLBuffer temp2 = clij2.create(dst.getDimensions(), NativeTypeEnum.Float);
        ClearCLBuffer temp3 = clij2.create(dst.getDimensions(), NativeTypeEnum.Float);
        ClearCLBuffer temp4 = clij2.create(dst.getDimensions(), NativeTypeEnum.Float);

        clij2.greaterConstant(src, temp1, 0);
        clij2.copy(temp1, temp3);

        int iteration = 0;
        while (flagValue[0] > 0) {
            flagValue[0] = 0;
            flag.readFrom(FloatBuffer.wrap(flagValue), true);

            if (iteration % 2 == 0 ) {
                localPositiveMinimumDiamond(clij2, temp1, temp2, flag);
                clij2.addImages(temp3, temp2, temp4);
            } else {
                localPositiveMinimumBox(clij2, temp2, temp1, flag);
                clij2.addImages(temp4, temp1, temp3);
            }

            flag.writeTo(FloatBuffer.wrap(flagValue), true);
            iteration++;
        }
        if (iteration % 2 != 0 ) {
            clij2.copy(temp3, dst);
        } else {
            clij2.copy(temp4, dst);
        }

        clij2.release(temp1);
        clij2.release(temp2);
        clij2.release(temp3);
        clij2.release(temp4);
        clij2.release(flag);

        return true;
    }

    private static boolean localPositiveMinimumBox(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst, ClearCLImageInterface flag_dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("flag_dst", flag_dst);

        clij2.execute(DistanceMap.class, "distancemap_localPositiveMinimum_box_" + dst.getDimension() + "d_x.cl", "distancemap_local_positive_minimum_box_" + dst.getDimension() +  "d", dst.getDimensions(), dst.getDimensions(), parameters);

        return true;
    }

    private static boolean localPositiveMinimumDiamond(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst, ClearCLImageInterface flag_dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("flag_dst", flag_dst);

        clij2.execute(DistanceMap.class, "distancemap_localPositiveMinimum_diamond_" + dst.getDimension() + "d_x.cl", "distancemap_local_positive_minimum_diamond_" + dst.getDimension() +  "d", dst.getDimensions(), dst.getDimensions(), parameters);

        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Image destination";
    }

    @Override
    public String getDescription() {
        return "Generates a distance map from a binary image. \n\nPixels with non-zero value in the binary image are set to a number " +
                "representing the distance to the closest zero-value pixel.\n\n" +
                "Note: This is not a distance matrix. See generateDistanceMatrix for details.";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        return getCLIJ2().create(input.getDimensions(), NativeTypeEnum.Float);
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public String getCategories() {
        return "Measurements, Filter";
    }
}
