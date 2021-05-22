package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_rigidTransform")
public class RigidTransform extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Image";
    }

    @Override
    public String getOutputType() {
        return "Image";
    }


    @Override
    public Object[] getDefaultValues() {
        return new Object[]{null, null, 0, 0, 0, 0,0,0};
    }

    @Override
    public String getParameterHelpText() {
        return "Image input, ByRef Image destination, Number translation_x, Number translation_y, Number translation_z, Number rotation_angle_x, Number rotation_angle_y, Number rotation_angle_z";
    }

    @Override
    public boolean executeCL() {
        return rigidTransform(getCLIJ2(), (ClearCLBuffer) args[0], (ClearCLBuffer) args[1], asFloat(args[2]), asFloat(args[3]), asFloat(args[4]), asFloat(args[5]), asFloat(args[6]), asFloat(args[7]));
    }

    public static boolean rigidTransform(CLIJ2 clij2, ClearCLBuffer pushed, ClearCLBuffer result, Float translation_x, Float translation_y, Float translation_z, Float rotation_x, Float rotation_y, Float rotation_z) {

        if (pushed.getDimension() == 2) {
            String transform = getTransform2D(translation_x, translation_y, translation_z, rotation_x, rotation_y, rotation_z);
            clij2.affineTransform2D(pushed, result, transform);
        } else {
            String transform = getTransform3D(translation_x, translation_y, translation_z, rotation_x, rotation_y, rotation_z);
            clij2.affineTransform3D(pushed, result, transform);
        }
        return true;
    }


    public static String getTransform3D(Float translation_x, Float translation_y, Float translation_z, Float rotation_x, Float rotation_y, Float rotation_z) {
            return
                    "-center" +
                            " translateX=" + translation_x +
                            " translateY=" + translation_y +
                            " translateZ=" + translation_z +
                            " rotateX=" + rotation_x +
                            " rotateY=" + rotation_y +
                            " rotateZ=" + rotation_z +
                            " center";
    }

    public static String getTransform2D(Float translation_x, Float translation_y, Float translation_z, Float rotation_x, Float rotation_y, Float rotation_z) {
        return
                "-center" +
                        " translateX=" + translation_x +
                        " translateY=" + translation_y +
                        " rotate=" + rotation_z +
                        " center";
    }

    @Override
    public String getDescription() {
        return "Applies a rigid transform using linear interpolation to an image stack.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public String getCategories() {
        return "Transform";
    }
}
