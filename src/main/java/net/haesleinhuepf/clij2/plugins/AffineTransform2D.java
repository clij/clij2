package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.clearcl.enums.ImageChannelDataType;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij.utilities.AffineTransform;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.*;
import org.scijava.plugin.Plugin;

import java.nio.FloatBuffer;
import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;

/**
 * Author: @haesleinhuepf
 *         December 2019
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_affineTransform2D")
public class AffineTransform2D extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, HasAuthor, HasLicense, IsCategorized, HasClassifiedInputOutput {
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

        ClearCLBuffer input = (ClearCLBuffer) args[0];
        ClearCLBuffer output = (ClearCLBuffer) args[1];
        String transform = (String) args[2];

        return getCLIJ2().affineTransform2D(input, output, transform);
    }

    public static boolean affineTransform2D(CLIJ2 clij2, ClearCLBuffer input, ClearCLImageInterface output, String transform) {
        String[] transformCommands = transform.trim().toLowerCase().split(" ");
        net.imglib2.realtransform.AffineTransform2D at = new net.imglib2.realtransform.AffineTransform2D();
        for(String transformCommand : transformCommands) {
            String[] commandParts = transformCommand.split("=");
            //System.out.print("Command: " + commandParts[0]);
            if (commandParts[0].compareTo("center") == 0) {
                net.imglib2.realtransform.AffineTransform2D translateTransform = new net.imglib2.realtransform.AffineTransform2D();
                translateTransform.translate(-input.getWidth() / 2, -input.getHeight() / 2);
                at.concatenate(translateTransform);
            } else if (commandParts[0].compareTo("-center") == 0) {
                net.imglib2.realtransform.AffineTransform2D translateTransform = new net.imglib2.realtransform.AffineTransform2D();
                translateTransform.translate(input.getWidth() / 2, input.getHeight() / 2);
                at.concatenate(translateTransform);
            } else if (commandParts[0].compareTo("scale") == 0) {
                net.imglib2.realtransform.AffineTransform2D scaleTransform = new net.imglib2.realtransform.AffineTransform2D();
                scaleTransform.scale(1.0 / Double.parseDouble(commandParts[1]));
                at.concatenate(scaleTransform);
            } else if (commandParts[0].compareTo("scalex") == 0) {
                net.imglib2.realtransform.AffineTransform2D scaleTransform = new net.imglib2.realtransform.AffineTransform2D();
                scaleTransform.set(1.0 / Double.parseDouble(commandParts[1]),0,0);
                scaleTransform.set(1.0 , 1, 1);
                at.concatenate(scaleTransform);
            } else if (commandParts[0].compareTo("scaley") == 0) {
                net.imglib2.realtransform.AffineTransform2D scaleTransform = new net.imglib2.realtransform.AffineTransform2D();
                scaleTransform.set(1.0,0,0);
                scaleTransform.set(1.0  / Double.parseDouble(commandParts[1]) , 1, 1);
                at.concatenate(scaleTransform);
            } else if (commandParts[0].compareTo("rotate") == 0 || commandParts[0].compareTo("rotate") == 0) {
                net.imglib2.realtransform.AffineTransform2D rotateTransform = new net.imglib2.realtransform.AffineTransform2D();
                float angle = (float)(-asFloat(commandParts[1]) / 180.0f * Math.PI);
                rotateTransform.rotate(angle);
                at.concatenate(rotateTransform);
            } else if (commandParts[0].compareTo("translatex") == 0) {
                net.imglib2.realtransform.AffineTransform2D translateTransform = new net.imglib2.realtransform.AffineTransform2D();
                translateTransform.translate(Double.parseDouble(commandParts[1]), 0);
                at.concatenate(translateTransform);
            } else if (commandParts[0].compareTo("translatey") == 0) {
                net.imglib2.realtransform.AffineTransform2D translateTransform = new net.imglib2.realtransform.AffineTransform2D();
                translateTransform.translate(0,Double.parseDouble(commandParts[1]), 0);
                at.concatenate(translateTransform);
            } else if (commandParts[0].compareTo("shearxy") == 0) {
                double shear = Double.parseDouble(commandParts[1]);
                net.imglib2.realtransform.AffineTransform2D shearTransform = new net.imglib2.realtransform.AffineTransform2D();
                shearTransform.set(1.0, 0, 0 );
                shearTransform.set(1.0, 1, 1 );
                shearTransform.set(shear, 0, 1);
                //shearTransform.set(shear, 0, 2);
                at.concatenate(shearTransform);
            } else {
                System.out.print("Unknown transform: " + commandParts[0]);
            }
        }

        if (!clij2.hasImageSupport()) {
            return affineTransform2D(clij2, input, output, net.haesleinhuepf.clij.utilities.AffineTransform.matrixToFloatArray2D(at));
        } else {
            ClearCLImage inputImage = clij2.create(input.getDimensions(), ImageChannelDataType.Float);
            clij2.copy(input, inputImage);

            boolean result = affineTransform2D(clij2, inputImage, output, net.haesleinhuepf.clij.utilities.AffineTransform.matrixToFloatArray2D(at));

            clij2.release(inputImage);

            return result;
        }
    }

    public static boolean affineTransform2D(CLIJ2 clij2, ClearCLBuffer src, ClearCLImageInterface dst, float[] matrix) {
        assertDifferent(src, dst);

        ClearCLBuffer matrixCl = clij2.create(new long[]{matrix.length, 1, 1}, NativeTypeEnum.Float);

        FloatBuffer buffer = FloatBuffer.wrap(matrix);
        matrixCl.readFrom(buffer, true);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("input", src);
        parameters.put("output", dst);
        parameters.put("mat", matrixCl);

        clij2.execute(AffineTransform2D.class, "affine_transform_2d_x.cl", "affine_transform_2d", dst.getDimensions(), dst.getDimensions(), parameters);

        matrixCl.close();

        return true;
    }

    public static boolean affineTransform2D(CLIJ2 clij2, ClearCLBuffer src, ClearCLImageInterface dst, net.imglib2.realtransform.AffineTransform2D at) {
        assertDifferent(src, dst);

        //at = at.inverse();
        float[] matrix = AffineTransform.matrixToFloatArray2D(at);
        return affineTransform2D(clij2, src, dst, matrix);
    }

    public static boolean affineTransform2D(CLIJ2 clij2, ClearCLImage src, ClearCLImageInterface dst, float[] matrix) {
        assertDifferent(src, dst);

        ClearCLBuffer matrixCl = clij2.create(new long[]{matrix.length, 1, 1}, NativeTypeEnum.Float);

        FloatBuffer buffer = FloatBuffer.wrap(matrix);
        matrixCl.readFrom(buffer, true);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("input", src);
        parameters.put("output", dst);
        parameters.put("mat", matrixCl);

        clij2.execute(AffineTransform2D.class, "affine_transform_2d_interpolate_x.cl", "affine_transform_2d_interpolate", dst.getDimensions(), dst.getDimensions(), parameters);

        matrixCl.close();

        return true;
    }

    public static boolean affineTransform2D(CLIJ2 clij2, ClearCLImage src, ClearCLImageInterface dst, net.imglib2.realtransform.AffineTransform2D at) {
        assertDifferent(src, dst);

        //at = at.inverse();
        float[] matrix = AffineTransform.matrixToFloatArray2D(at);
        return affineTransform2D(clij2, src, dst, matrix);
    }


    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Image destination, String transform";
    }

    @Override
    public String getDescription() {
        return "Applies an affine transform to a 2D image.\n\n" +
                "The transform describes how coordinates in the target image are transformed to coordinates in the source image.\n" +
                "This may appear unintuitive and will be changed in the next major release. The replacement \n" +
                "affineTransform (currently part of CLIJx) will apply inverted transforms compared to this operation.\n" +
                "Individual transforms must be separated by spaces.\n" +
                "Parameters\n" +
                "----------\n" +
                "source : Image\n" +
                "    The input image to be processed.\n" +
                "destination : Image\n" +
                "    The output image where results are written into.\n" +
                "transform : String\n" +
                "    A space-separated list of individual transforms. Syntrax see below.\n" +
                "\n" +
                "Supported transforms:\n" +
                "\n* -center: translate the coordinate origin to the center of the image" +
                "\n* center: translate the coordinate origin back to the initial origin" +
                "\n* rotate=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees" +
                "\n* scale=[factor]: isotropic scaling according to given zoom factor" +
                "\n* scaleX=[factor]: scaling along X-axis according to given zoom factor" +
                "\n* scaleY=[factor]: scaling along Y-axis according to given zoom factor" +
                "\n* shearXY=[factor]: shearing along X-axis in XY plane according to given factor" +
                "\n* translateX=[distance]: translate along X-axis by distance given in pixels" +
                "\n* translateY=[distance]: translate along X-axis by distance given in pixels" +
                "\n\nExample transform:" +
                "\ntransform = \"-center scale=2 rotate=45 center\";";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }

    @Override
    public String getAuthorName() {
        return "Robert Haase and Peter Haub based on work by Martin Weigert";
    }

    @Override
    public String getLicense() {
        return " adapted from: https://github.com/maweigert/gputools/blob/master/gputools/transforms/kernels/transformations.cl\n" +
                "\n" +
                " Copyright (c) 2016, Martin Weigert\n" +
                " All rights reserved.\n" +
                "\n" +
                " Redistribution and use in source and binary forms, with or without\n" +
                " modification, are permitted provided that the following conditions are met:\n" +
                "\n" +
                " * Redistributions of source code must retain the above copyright notice, this\n" +
                "   list of conditions and the following disclaimer.\n" +
                "\n" +
                " * Redistributions in binary form must reproduce the above copyright notice,\n" +
                "   this list of conditions and the following disclaimer in the documentation\n" +
                "   and/or other materials provided with the distribution.\n" +
                "\n" +
                " * Neither the name of gputools nor the names of its\n" +
                "   contributors may be used to endorse or promote products derived from\n" +
                "   this software without specific prior written permission.\n" +
                "\n" +
                " THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS \"AS IS\"\n" +
                " AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE\n" +
                " IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE\n" +
                " DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE\n" +
                " FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL\n" +
                " DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR\n" +
                " SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER\n" +
                " CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,\n" +
                " OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE\n" +
                " OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.";
    }

    @Override
    public String getCategories() {
        return "Transform";
    }
}
