package net.haesleinhuepf.clij2.plugins;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.plugins.ResliceRadial;
import net.haesleinhuepf.clij2.utilities.CLIJUtilities;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import net.imglib2.realtransform.AffineTransform3D;
import org.scijava.plugin.Plugin;

import java.nio.FloatBuffer;
import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;

/**
 * ResliceRadial
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 *         July 2020
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_reslicePolar")
public class ReslicePolar extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Image";
    }

    @Override
    public String getOutputType() {
        return "Image";
    }

    @Override
    public String getCategories() {
        return "Transform";
    }

    @Override
    public boolean executeCL() {
        boolean result = reslicePolar(getCLIJ2(),
                (ClearCLBuffer)( args[0]),
                (ClearCLBuffer)(args[1]),
                asFloat(args[3]),
                asFloat(args[4]),
                asFloat(args[5]),
                asFloat(args[6]),
                asFloat(args[7]),
                asFloat(args[8]),
                asFloat(args[9]),
                asFloat(args[10]),
                asFloat(args[11]),
                asFloat(args[12]),
                asFloat(args[13]),
                asFloat(args[14]),
                asFloat(args[15]),
                asFloat(args[16]),
                asFloat(args[17])
        );
        return result;
    }

    public static boolean reslicePolar(CLIJ2 clij2, ClearCLBuffer src, ClearCLBuffer dst, Float deltaAngle, Float startInclinationDegrees, Float startAzimuthDegrees, Float centerX, Float centerY, Float centerZ, Float scaleFactorX, Float scaleFactorY, Float scaleFactorZ,
                                       float translation_x, float translation_y, float translation_z, float rotation_x, float rotation_y, float rotation_z) {
        assertDifferent(src, dst);

        ClearCLImage image = clij2.create(src.getDimensions(), CLIJUtilities.nativeToChannelType(src.getNativeType()));
        clij2.copy(src, image);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", image);
        parameters.put("dst", dst);
        parameters.put("deltaAngle", deltaAngle);
        parameters.put("centerX", centerX);
        parameters.put("centerY", centerY);
        parameters.put("centerZ", centerZ);
        parameters.put("startInclinationDegrees", startInclinationDegrees);
        parameters.put("startAzimuthDegrees", startAzimuthDegrees);
        parameters.put("scaleX", scaleFactorX);
        parameters.put("scaleY", scaleFactorY);
        parameters.put("scaleZ", scaleFactorZ);

        AffineTransform3D at = new AffineTransform3D();
        at.translate(-centerX, -centerY, -centerZ);
        at.rotate(0, rotation_x);
        at.rotate(0, rotation_y);
        at.rotate(0, rotation_z);
        at.translate(centerX, centerY, centerZ);
        at.translate(translation_x, translation_y, translation_z);

        float[] matrix = net.haesleinhuepf.clij.utilities.AffineTransform.matrixToFloatArray(at);


        ClearCLBuffer matrixCl = clij2.create(new long[]{matrix.length, 1, 1}, NativeTypeEnum.Float);

        FloatBuffer buffer = FloatBuffer.wrap(matrix);
        matrixCl.readFrom(buffer, true);
        parameters.put("mat", matrixCl);

        clij2.execute(ReslicePolar.class, "reslice_polar_interpolate_x.cl", "reslice_polar", dst.getDimensions(), dst.getDimensions(), parameters);

        clij2.release(matrixCl);
        clij2.release(image);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, " +
                "ByRef Image destination, " +
                "Number numberOfAngles, " +
                "Number angleStepSize, " +
                "Number startInclinationAngleDegrees, " +
                "Number startAzimuthAngleDegrees, " +
                "Number centerX, " +
                "Number centerY, " +
                "Number centerZ, " +
                "Number scaleFactorX, " +
                "Number scaleFactorY, " +
                "Number scaleFactorZ, " +
                "Number translation_x, " +
                "Number translation_y, " +
                "Number translation_z, " +
                "Number rotation_x, " +
                "Number rotation_y, " +
                "Number rotation_z, ";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        int numberOfAngles = asInteger(args[2]);
        float angleStepSize = asFloat(args[3]);

        int effectiveNumberOfAngles = (int)((float)numberOfAngles / angleStepSize);
        int maximumRadius = (int)Math.sqrt(Math.pow(input.getWidth() / 2, 2) + Math.pow(input.getHeight() / 2, 2) + Math.pow(input.getDepth() / 2, 2));

        return getCLIJ2().create(new long[]{maximumRadius, input.getDepth(), effectiveNumberOfAngles}, input.getNativeType());
    }

    @Override
    public String getDescription() {
        return "todo";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }


    public static void main(String[] args) {
        float zoom = 0.5f;
        float delta_angle = 0.5f;

        new ImageJ();
        ImagePlus imp = IJ.openImage("C:/structure/data/clincubator_data/ISB200714_well5_1pos_ON_t000000.tif");
        imp.show();

        float scale_x = (float) (imp.getCalibration().pixelWidth) / zoom;
        float scale_y = (float) (imp.getCalibration().pixelHeight)/ zoom;
        float scale_z = (float) (imp.getCalibration().pixelDepth) / zoom;

        CLIJ2 clij2 = CLIJ2.getInstance();

        ClearCLBuffer input = clij2.push(imp);
        //ClearCLBuffer resliced = clij2.create(input.getHeight(), input.getDepth(), input.getWidth());
        //clij2.resliceLeft(input, resliced);
        //clij2.show(resliced, "resliced");
        //input.close();
        //input = resliced;

        ClearCLBuffer output = clij2.create((long)(360 / delta_angle), (long)(180 / delta_angle), 500);
        ClearCLBuffer projection = clij2.create((long)(360 / delta_angle), (long)(180 / delta_angle));

        ClearCLBuffer video = clij2.create((long)(360 / delta_angle), (long)(180 / delta_angle), 180 / 5);

        for (float param = 0; param < 180; param += 5) {
            reslicePolar(clij2, input, output, delta_angle, 180f, 0f,
                    (float) (input.getWidth() / 2), (float) (input.getHeight() / 2), (float) (input.getDepth() / 2),
                    scale_x, scale_y, scale_z,
                    0,0,param - 90,
                    0,0,0);
        //clij2.show(output, "output");

            //(float)(param * Math.PI / 180.0)

            clij2.maximumZProjection(output, projection);
       // clij2.show(projection, "projection");
            clij2.copySlice(projection, video, param / 5);
        }

        clij2.show(video, "video");



        //clij2.show(output, "output");



    }

}
