package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_copySlice")
public class CopySlice extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized {
    @Override
    public String getCategories() {
        return "Transform";
    }

    @Override
    public Object[] getDefaultValues() {
        return new Object[]{null, null, 0};
    }

    @Override
    public boolean executeCL() {
        return getCLIJ2().copySlice((ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asInteger(args[2]));
    }

    public static boolean copySlice(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst, Integer planeIndex) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("slice", planeIndex);
        clij2.activateSizeIndependentKernelCompilation();
        //return clij.execute(Kernels.class, "duplication.cl", "copySlice", parameters);
        if (src.getDimension() == 2 && dst.getDimension() == 3) {
            clij2.execute(CopySlice.class, "copy_slice_to_3d_x.cl", "copy_slice_to_3d", src.getDimensions(), src.getDimensions(), parameters);
        } else if (src.getDimension() == 3 && dst.getDimension() == 2) {
            clij2.execute(CopySlice.class, "copy_slice_from_3d_x.cl", "copy_slice_from_3d", dst.getDimensions(), dst.getDimensions(), parameters);
        } else {
            System.out.println("Warning: Images have wrong dimension. Must be 3D->2D or 2D->3D.");
            clij2.copy(src, dst);
        }
        return true;
    }


    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Image destination, Number sliceIndex";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        if (input.getDimension() == 2) {
            return clij.createCLBuffer(new long[]{ input.getWidth(), input.getHeight(), asInteger(args[2]) + 1}, input.getNativeType());
        } else  {
            return clij.createCLBuffer(new long[]{ input.getWidth(), input.getHeight()}, input.getNativeType());
        }
    }


    @Override
    public String getDescription() {
        return "This method has two purposes: \n" +
                "It copies a 2D image to a given slice z position in a 3D image stack or \n" +
                "It copies a given slice at position z in an image stack to a 2D image.\n\n" +
                "The first case is only available via ImageJ macro. If you are using it, it is recommended that the \n" +
                "target 3D image already pre-exists in GPU memory before calling this method. Otherwise, CLIJ create \n" +
                "the image stack with z planes.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D -> 2D and 2D -> 3D";
    }
}
