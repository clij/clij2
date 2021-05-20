package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_cylinderTransform")
public class CylinderTransform extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {

    @Override
    public String getParameterHelpText() {
        return "Image input, ByRef Image destination, Number number_of_angles, Number delta_angle_in_degrees, Number relative_center_x, Number relative_center_z";
    }

    @Override
    public Object[] getDefaultValues() {
        return new Object[]{null, null, 360, 1, 0.5, 0.5};
    }

    @Override
    public boolean executeCL() {
        return cylinderTransform(getCLIJ2(), (ClearCLBuffer) args[0], (ClearCLBuffer) args[1], asInteger(args[2]), asFloat(args[3]), asFloat(args[4]), asFloat(args[5]));
    }

    public static boolean cylinderTransform(CLIJ2 clij2, ClearCLBuffer pushed, ClearCLBuffer result, Integer number_of_angles,
                                            Float delta_angle_in_degrees,
                                            Float relative_center_x,
                                            Float relative_center_z) {

        System.out.println("number_of_angles = " + number_of_angles);
        System.out.println("delta_angle_in_degrees = " + delta_angle_in_degrees);
        System.out.println("relative_center_x = " + relative_center_x);
        System.out.println("relative_center_z = " + relative_center_z);

        int center_x = (int) (pushed.getWidth() * relative_center_x);
        int center_y = (int) (pushed.getDepth() * relative_center_z);

        int radius = (int) Math.sqrt(Math.pow(pushed.getWidth() / 2, 2) + Math.pow(pushed.getDepth() / 2, 2));

        ClearCLBuffer resliced_from_top = clij2.create(pushed.getWidth(), pushed.getDepth(), pushed.getHeight());
        clij2.resliceTop(pushed, resliced_from_top);
        //pushed.close();

        ClearCLBuffer radial_resliced = clij2.create(radius, pushed.getHeight(), number_of_angles);

        float start_angle = -90;
        float scale_x = 1f;
        float scale_z = 1f;

        clij2.resliceRadial(resliced_from_top, radial_resliced, delta_angle_in_degrees, start_angle, center_x, center_y, scale_x, scale_z);
        resliced_from_top.close();

        clij2.transposeXZ(radial_resliced, result);
        radial_resliced.close();

        return true;
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer pushed) {
        int radius = (int) Math.sqrt(Math.pow(pushed.getWidth() / 2, 2) + Math.pow(pushed.getDepth() / 2, 2));
        int number_of_angles = asInteger(args[2]);

        return getCLIJ2().create(number_of_angles, pushed.getHeight(), radius);
    }

    @Override
    public String getDescription() {
        return "Applies a cylinder transform to an image stack assuming the center line goes in Y direction in the center of the stack.\n\n" +
                "This transforms an image stack from an XYZ coordinate system to a AYD coordinate system with \n" +
                "A the angle around the center line, \n" +
                "Y the original Y coordinate and \n" +
                "D, the distance from the center.\n\n" +
                "Thus, going in virtual Z direction (actually D) in the resulting stack, you go from the center to the" +
                "periphery.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

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
}
