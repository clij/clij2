package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLKernel;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.HasAuthor;
import net.haesleinhuepf.clij2.utilities.HasLicense;
import net.imglib2.img.array.ArrayImgs;
import org.scijava.plugin.Plugin;

import java.nio.FloatBuffer;
import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;
import static net.haesleinhuepf.clij2.utilities.CLIJUtilities.checkDimensions;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_skeletonize")
public class Skeletonize extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, HasAuthor, HasLicense {

    @Override
    public boolean executeCL() {
        return skeletonize(getCLIJ2(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]));
    }

    public static boolean skeletonize(CLIJ2 clij2, ClearCLBuffer src, ClearCLBuffer dst) {
        assertDifferent(src, dst);
        ClearCLBuffer flag = clij2.create(1,1,1);
        ClearCLBuffer temp = clij2.create(dst);

        float[] flag_arr = new float[1];
        FloatBuffer floatBuffer = FloatBuffer.wrap(flag_arr);

        ClearCLKernel kernel = null;

        clij2.copy(src, temp);

        boolean flipFlag = true;
        for (int i = 0; i < 2; i++) {
            for (int direction = 1; direction <= 4; direction++) {
                HashMap<String, Object> parameters = new HashMap<>();
                if (flipFlag) {
                    parameters.put("src", temp);
                    parameters.put("dst", dst);
                } else {
                    parameters.put("src", dst);
                    parameters.put("dst", temp);
                }
                flipFlag = !flipFlag;

                parameters.put("flag_dst", flag);
                parameters.put("direction", direction);

                if (!checkDimensions(src.getDimension(), src.getDimension(), dst.getDimension())) {
                    throw new IllegalArgumentException("Error: number of dimensions don't match! (minimumImageAndScalar)");
                }
                kernel = clij2.executeSubsequently(Skeletonize.class, "skeletonize_" + src.getDimension() + "d_x.cl", "skeletonize_" + src.getDimension() + "d", dst.getDimensions(), dst.getDimensions(), parameters, kernel);

                clij2.print(dst);
                break;
            }
            break;
        }

        if (flipFlag) {
            clij2.copy(temp, dst);
        }

        clij2.release(temp);
        clij2.release(flag);

        return true;
    }


    public static void main(String... args) {
        CLIJ2 clij2 = CLIJ2.getInstance();

        ClearCLBuffer binary_image = clij2.push(ArrayImgs.floats(new float[]{
                1, 1, 1, 0, 0, 0, 0, 0, 0, 0,
                1, 1, 1, 1, 1, 1, 0, 0, 0, 0,
                1, 1, 1, 1, 1, 1, 1, 0, 0, 0,
                0, 0, 0, 1, 1, 1, 1, 0, 0, 0,
                0, 0, 0, 0, 1, 1, 1, 0, 0, 0,

                0, 0, 0, 0, 1, 1, 1, 1, 0, 0,
                0, 0, 0, 0, 1, 1, 1, 1, 1, 0,
                0, 0, 0, 0, 1, 1, 1, 1, 1, 0,
                0, 0, 0, 0, 1, 1, 1, 1, 1, 0,
                0, 0, 0, 0, 1, 1, 1, 1, 1, 0,
            }, new long[]{10, 10}
        ));

        ClearCLBuffer skeleton = clij2.create(binary_image);

        Skeletonize.skeletonize(clij2, binary_image, skeleton);

        System.out.println("Result: ");
        clij2.print(skeleton);

        clij2.clear();
    }


    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination";
    }

    @Override
    public String getDescription() {
        return "Erodes a binary image until just its skeleton is left.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }


    @Override
    public String getAuthorName() {
        return "Robert Haase translated original work by Ignacio Arganda-Carreras";
    }

    @Override
    public String getLicense() {
        return "\n" +
                " Skeletonize3D plugin for ImageJ(C).\n" +
                " Copyright (C) 2008 Ignacio Arganda-Carreras \n" +
                " \n" +
                " This program is free software; you can redistribute it and/or\n" +
                " modify it under the terms of the GNU General Public License\n" +
                " as published by the Free Software Foundation (http://www.gnu.org/licenses/gpl.txt )\n" +
                "\n" +
                " This program is distributed in the hope that it will be useful,\n" +
                " but WITHOUT ANY WARRANTY; without even the implied warranty of\n" +
                " MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n" +
                " GNU General Public License for more details.\n" +
                " \n" +
                " You should have received a copy of the GNU General Public License\n" +
                " along with this program; if not, write to the Free Software\n" +
                " Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.\n" +
                " \n" +
                "";
    }
}
