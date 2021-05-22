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

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_sphereTransform")
public class SphereTransform extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Image";
    }

    @Override
    public String getOutputType() {
        return "Image";
    }


    @Override
    public String getParameterHelpText() {
        return "Image input, ByRef Image destination, Number number_of_angles, Number delta_angle_in_degrees, Number relative_center_x, Number relative_center_y, Number relative_center_z";
    }

    @Override
    public Object[] getDefaultValues() {
        return new Object[]{null, null, 360, 1, 0.5, 0.5, 0.5};
    }

    @Override
    public boolean executeCL() {
        return sphereTransform(getCLIJ2(), (ClearCLBuffer) args[0], (ClearCLBuffer) args[1], asInteger(args[2]), asFloat(args[3]), asFloat(args[4]), asFloat(args[5]), asFloat(args[6]));
    }

    public static boolean sphereTransform(CLIJ2 clij2, ClearCLBuffer pushed, ClearCLBuffer result, Integer number_of_angles,
                                            Float delta_angle_in_degrees,
                                          Float relative_center_x,
                                          Float relative_center_y,
                                            Float relative_center_z) {

        System.out.println("number_of_angles = " + number_of_angles);
        System.out.println("delta_angle_in_degrees = " + delta_angle_in_degrees);
        System.out.println("relative_center_x = " + relative_center_x);
        System.out.println("relative_center_y = " + relative_center_y);
        System.out.println("relative_center_z = " + relative_center_z);


        ReslicePolar.reslicePolar(clij2, pushed, result,
                delta_angle_in_degrees, 0f, 0f,
                relative_center_x, relative_center_y, relative_center_z,
                1f, 1f, 1f,
                0f, 0f, 0f,
                0f, 0f, 0f);

        return true;
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer pushed) {
        int radius = (int) Math.sqrt(Math.pow(pushed.getWidth() / 2, 2) + Math.pow(pushed.getHeight() / 2, 2)  + Math.pow(pushed.getDepth() / 2, 2) );
        int number_of_angles = asInteger(args[2]);

        return getCLIJ2().create(number_of_angles, number_of_angles / 2, radius);
    }

    @Override
    public String getDescription() {
        return "Turns an image stack in XYZ cartesian coordinate system to an AID polar coordinate system.\n\n" +
                "A corresponds to azimut," +
                "I to inclination and " +
                "D to the distance from the center." +
                "Thus, going in virtual Z direction (actually D) in the resulting stack, you go from the center to the" +
                "periphery.";
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
