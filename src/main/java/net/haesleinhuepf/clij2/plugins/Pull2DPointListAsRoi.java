package net.haesleinhuepf.clij2.plugins;

import ij.IJ;
import ij.gui.PointRoi;
import ij.gui.PolygonRoi;
import ij.gui.Roi;
import ij.process.FloatPolygon;
import ij.process.FloatProcessor;
import net.haesleinhuepf.clij.clearcl.ClearCL;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

import java.nio.FloatBuffer;

/**
 * Author: @haesleinhuepf
 *         July 2020
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_pull2DPointListAsRoi")
public class Pull2DPointListAsRoi extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        Roi result = pull2DPointListAsRoi(getCLIJ2(), (ClearCLBuffer) args[0]);
        IJ.getImage().setRoi(result);
        return true;

    }

    public static Roi pull2DPointListAsRoi(CLIJ2 clij2, ClearCLBuffer pointlist) {
        ClearCLBuffer temp = clij2.create(pointlist.getHeight(), pointlist.getWidth());
        clij2.transposeXY(pointlist, temp);
        float[][] coords = (float[][]) clij2.pullMatXYZ(temp);
        temp.close();

        System.out.println("width " + coords.length);
        System.out.println("height " + coords[0].length);

        FloatPolygon fp = new FloatPolygon(coords[0], coords[1], coords[0].length);
        PointRoi pr = new PointRoi(fp);

        return pr;
    }

    @Override
    public String getParameterHelpText() {
        return "Image input, ByRef Array destination";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }

    public static void main(String[] args) {
        CLIJ2 clij2 = CLIJ2.getInstance();
        ClearCLBuffer input = clij2.pushString(
                "1 2 3 4\n" +
                      "5 6 7 8");

        pull2DPointListAsRoi(clij2, input);
    }
}
