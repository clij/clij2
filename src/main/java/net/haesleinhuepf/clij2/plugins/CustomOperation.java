package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.clearcl.util.CLKernelExecutor;
import net.haesleinhuepf.clij.macro.CLIJHandler;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.HasAuthor;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

import static net.haesleinhuepf.clij2.plugins.AddImagesWeighted.addImagesWeighted;

/**
 * Author: @haesleinhuepf
 *         @maarzt
 *         March 2020
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_customOperation")
public class CustomOperation extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, HasAuthor {

    @Override
    public boolean executeCL() {
        String operation_code = (String) args[0];
        String global_code = (String) args[1];
        Object[] image_parameter_list = (Object[]) args[2];

        HashMap<String, ClearCLBuffer> images = new HashMap<String, ClearCLBuffer>();
        for (int i = 0; i < image_parameter_list.length; i += 2) {
            String parameterName = (String)image_parameter_list[i];
            ClearCLBuffer imageParameter = CLIJHandler.getInstance().getFromCache((String)image_parameter_list[i + 1]);

            images.put(parameterName, imageParameter);
        }

        return getCLIJ2().customOperation(operation_code, global_code, images);
    }

    public static boolean customOperation(CLIJ2 clij2, String operation_code, String global_code, HashMap<String, ClearCLBuffer> images) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        HashMap<String, Object> constants = new HashMap<String, Object>();

        StringBuilder parameterList = new StringBuilder();

        long[] dimensions = null;
        for (String key : images.keySet()) {
            ClearCLBuffer buffer = images.get(key);
            dimensions = buffer.getDimensions();
            if (parameterList.length() > 0) {
                parameterList.append(", ");
            }

            parameterList.append("IMAGE_" + key + "_TYPE " + key);
            parameters.put(key, buffer);
        }

        String code = "const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;\n" +
                "\n" +
                global_code +
                "\n" +
                "__kernel void custom_operation(" + parameterList.toString() + ") {\n" +
                "  const int x = get_global_id(0);\n" +
                "  const int y = get_global_id(1);\n" +
                "  const int z = get_global_id(2);\n" +
                operation_code +
                "}\n";

        clij2.executeCode(code, "custom_operation", dimensions, dimensions, parameters, constants);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "String operation_code, String global_code, Array image_parameters";
    }

    @Override
    public String getDescription() {
        return "Executes a custom operation wirtten in OpenCL on a custom list of images. \n\n" +
                "All images must be created before calling this method. Image parameters should be handed over as an array with parameter names and image names alternating, e.g." +
                "\n\n" +
                "Ext.CLIJ2_customOperation(..., ..., newArray(\"image1\", \"blobs.gif\", \"image2\", \"Processed_blobs.gif\"))" +
                "\n\n" +
                "In the custom code, you can use the predefined variables x, y and z to deal with coordinates.\n" +
                "You can for example use it to access pixel intensities like this:" +
                "\n\n" +
                "float value = READ_IMAGE(image, sampler, POS_image_INSTANCE(x, y, z, 0)).x;\n" +
                "WRITE_IMAGE(image, POS_image_INSTANCE(x, y, z, 0), CONVERT_image_PIXEL_TYPE(value));" +
                "\n\n" +
                "Note: replace `image` with the given image parameter name. You can furthermore use custom function to " +
                "organise code in the global_code parameter. In OpenCL they may look like this:" +
                "\n\n" +
                "inline float sum(float a, float b) {\n" +
                "    return a + b;\n" +
                "}\n";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public String getAuthorName() {
        return "Robert Haase, Matthias Arzt";
    }
}
