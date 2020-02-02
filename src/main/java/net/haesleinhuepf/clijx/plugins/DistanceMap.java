package net.haesleinhuepf.clijx.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

import java.nio.FloatBuffer;
import java.util.HashMap;

/**
 * Author: @haesleinhuepf
 *         September 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_distanceMap")
public class DistanceMap extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();

        ClearCLBuffer src = (ClearCLBuffer)( args[0]);
        ClearCLBuffer dst = (ClearCLBuffer)( args[1]);

        distanceMap(getCLIJx(), src, dst);

        return true;
    }

    public static boolean distanceMap(CLIJx clijx, ClearCLBuffer src, ClearCLBuffer dst) {
        ClearCLBuffer flag = clijx.create(new long[]{1,1,1});
        float[] flagValue = {1};

        ClearCLBuffer temp1 = clijx.create(dst);
        ClearCLBuffer temp2 = clijx.create(dst);
        ClearCLBuffer temp3 = clijx.create(dst);

        clijx.op.copy(src, temp1);
        clijx.op.copy(src, temp3);

        int iteration = 0;
        while (flagValue[0] > 0) {
            flagValue[0] = 0;
            flag.readFrom(FloatBuffer.wrap(flagValue), true);

            if (iteration % 2 == 0 ) {
                localPositiveMinimum(clijx, temp1, temp2, flag);
                clijx.op.addImages(temp3, temp2, dst);
            } else {
                localPositiveMinimum(clijx, temp2, temp1, flag);
                clijx.op.addImages(dst, temp1, temp3);
            }

            flag.writeTo(FloatBuffer.wrap(flagValue), true);
            iteration++;
        }
        if (iteration % 2 != 0 ) {
            clijx.op.copy(temp3, dst);
        }
        temp1.close();
        temp2.close();
        temp3.close();
        flag.close();

        return true;
    }

    public static boolean localPositiveMinimum(CLIJx clijx, ClearCLImageInterface src, ClearCLImageInterface dst, ClearCLImageInterface flag_dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("flag_dst", flag_dst);

        clijx.execute(DistanceMap.class, "distancemap_localPositiveMinimum" + dst.getDimension() + "d_x.cl", "distancemap_local_positive_minimum_box_" + dst.getDimension() +  "d", dst.getDimensions(), dst.getDimensions(), parameters);

        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination";
    }

    @Override
    public String getDescription() {
        return "Generates a distance map from a binary image. Pixels with non-zero value in the binary image are set to a number " +
                "representing the distance to the closest zero-value pixel.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
