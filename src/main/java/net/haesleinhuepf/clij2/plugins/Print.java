package net.haesleinhuepf.clij2.plugins;

import ij.ImagePlus;
import ij.process.ImageProcessor;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 *         March 2020 in Bordeaux
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_print")
public class Print extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {

        return print(getCLIJ2(), (ClearCLBuffer) args[0]);
    }

    public static boolean print(CLIJ2 clij2, ClearCLImageInterface input) {
        ImagePlus imp = clij2.pull(input);

        for (int z = 0; z < imp.getNSlices(); z++) {
            imp.setZ(z + 1);
            if (imp.getNSlices() > 1) {
                System.out.print("z = " + z + ":\n");
            }
            ImageProcessor ip = imp.getProcessor();
            for (int y = 0; y < ip.getHeight(); y++) {
                for (int x = 0; x < ip.getWidth(); x++) {
                    System.out.print(ip.getf(x, y) + " ");
                }
                System.out.print("\n");
            }
        }


        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image input";
    }

    @Override
    public String getDescription() {
        return "Visualises an image on standard out (console).";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
