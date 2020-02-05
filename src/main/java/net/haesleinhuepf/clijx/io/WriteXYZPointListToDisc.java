package net.haesleinhuepf.clijx.io;


import ij.ImagePlus;
import ij.process.FloatProcessor;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.real.FloatType;
import org.scijava.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_writeXYZPointListToDisc")
public class WriteXYZPointListToDisc extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image pointlist, String filename";
    }

    @Override
    public boolean executeCL() {
        ClearCLBuffer pointlist = (ClearCLBuffer) args[0];
        String filename = (String) args[1];

        return writeXYZPointListToDisc(getCLIJx(), pointlist, filename);
    }

    public static boolean writeXYZPointListToDisc(CLIJx clijx, ClearCLBuffer pointlist, String filename) {
        StringBuilder coordinateList = new StringBuilder();
        {
            ClearCLBuffer pointListFloat = pointlist;
            if (pointlist.getNativeType() != clijx.Float) {
                pointListFloat = clijx.create(pointlist.getDimensions(), clijx.Float);

            }
            ImagePlus pointlistImp = clijx.pull(pointListFloat);
            FloatProcessor fp = (FloatProcessor) pointlistImp.getProcessor();


            int numberOfPoints = fp.getWidth();
            int numberOfCoordinates = fp.getHeight();
            System.out.println("n points: " + numberOfPoints);
            System.out.println("n coords: " + numberOfCoordinates);

            // header
            coordinateList.append(numberOfPoints + "\n\n");

            // nth point
            for (int n = 0; n < numberOfPoints; n++) {
                coordinateList.append(n + " ");
                // cth coordinate
                for (int c = 0; c < numberOfCoordinates; c++) {
                    coordinateList.append(fp.getf(n, c) + " ");
                }
                coordinateList.append("\n");
            }

            if (pointListFloat != pointlist) {
                clijx.release(pointListFloat);
            }
        }
        System.out.println(coordinateList.toString());

        try {
            new File(filename).getParentFile().mkdirs();
            Files.write(Paths.get(filename), coordinateList.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public String getDescription() {
        return "Takes a point list image representing n points (n*2 for 2D points, n*3 for 3D points) " +
                "and exports them in XYZ format.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }


    public static void main(String[] args) {
        CLIJx clijx = CLIJx.getInstance();

        // define a cube
        float[] points = new float[]{
                0, 1, 0, 1, 0, 1, 0, 1,
                0, 0, 1, 1, 0, 0, 1, 1,
                0, 0, 0, 0, 1, 1, 1, 1
        };

        Img<FloatType> point_img = ArrayImgs.floats(points, 8, 3);

        ClearCLBuffer pointlist = clijx.push(point_img);

        writeXYZPointListToDisc(clijx, pointlist, "C:/structure/temp/temp.xyz");

        clijx.clear();
    }
}
