package net.haesleinhuepf.clij2;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLKernel;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * February 2021
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_erodeLabels")
public class ErodeLabels extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Label Image";
    }

    @Override
    public String getOutputType() {
        return "Label Image";
    }

    @Override
    public String getParameterHelpText() {
        return "Image labels_input, ByRef Image labels_destination, Number radius, Boolean relabel_islands";
    }

    @Override
    public Object[] getDefaultValues() {
        return new Object[]{null, null, 1, false};
    }

    @Override
    public boolean executeCL() {
        return erodeLabels(getCLIJ2(), (ClearCLBuffer) args[0], (ClearCLBuffer) args[1], asInteger(args[2]), asBoolean(args[3]));
    }

    public static boolean erodeLabels(CLIJ2 clij2, ClearCLBuffer labels_input, ClearCLBuffer labels_destination, Integer radius, Boolean relabel_islands) {
        if (radius <= 0) {
            clij2.copy(labels_input, labels_destination);
            return true;
        }
        ClearCLBuffer temp = clij2.create(labels_destination);
        ClearCLBuffer temp1 = clij2.create(labels_destination);
        clij2.detectLabelEdges(labels_input, temp);
        clij2.binaryNot(temp, temp1);
        clij2.mask(labels_input, temp1, temp);
        temp1.close();
        if (radius == 1) {
            clij2.copy(temp, labels_destination);
        } else {
            //clij2.copy(labels_input, temp);

            for (int i = 0; i < radius; i++) {
                if (i % 2 == 0) {
                    if (labels_destination.getDimension() == 3) {
                        clij2.minimum3DSphere(temp, labels_destination, 1, 1, 1);
                    } else {
                        clij2.minimum2DSphere(temp, labels_destination, 1, 1);
                    }
                } else {
                    if (labels_destination.getDimension() == 3) {
                        clij2.minimum3DBox(labels_destination, temp, 1, 1, 1);
                    } else {
                        clij2.minimum2DBox(labels_destination, temp, 1, 1);
                    }
                }
            }
        }
        if (relabel_islands) {
            if (radius % 2 == 0) {
                clij2.copy(temp, labels_destination);
            }
            clij2.notEqualConstant(labels_destination, temp, 0);
            clij2.connectedComponentsLabelingBox(temp, labels_destination);
        } else {
            if (radius % 2 != 0) {
                clij2.copy(labels_destination, temp);
            }
            clij2.closeIndexGapsInLabelMap(temp, labels_destination);
        }

        temp.close();


        return true;
    }

    @Override
    public String getDescription() {
        return "Extend labels with a given radius.\n\n" +
                "This is actually a local minimum filter applied to a label map after introducing background-gaps between labels.\n" +
                "In case relabel_islands is set, split objects will get new labels each. In this case, more labels might be in the result.\n" +
                "It is recommended to apply this operation to isotropic images only.\n" +
                "Warning: If labels were too small, they may be missing in the resulting label image.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public String getCategories() {
        return "Label, Filter";
    }
}
