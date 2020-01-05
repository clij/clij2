package net.haesleinhuepf.clijx.io;


import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.WaitForUserDialog;
import ij.process.FloatProcessor;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
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
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_writeVTKLineListToDisc")
public class WriteVTKLineListToDisc extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public String getParameterHelpText() {
        return "Image pointlist, Image touch_matrix, String filename";
    }

    @Override
    public boolean executeCL() {
        ClearCLBuffer pointlist = (ClearCLBuffer) args[0];
        ClearCLBuffer touchmatrix = (ClearCLBuffer) args[1];
        String filename = (String) args[2];

        return writeVTKLineListToDisc(getCLIJx(), pointlist, touchmatrix, filename);
    }

    public static boolean writeVTKLineListToDisc(CLIJx clijx, ClearCLBuffer pointlist, ClearCLBuffer touchmatrix, String filename) {
        StringBuilder coordinateList = new StringBuilder();

        float[][] positions = null;

        int numberOfPoints = 0;
        {
            ClearCLBuffer pointListFloat = pointlist;
            if (pointlist.getNativeType() != clijx.Float) {
                pointListFloat = clijx.create(pointlist.getDimensions(), clijx.Float);
                clijx.copy(pointlist, pointListFloat);
            }
            ImagePlus pointlistImp = clijx.pull(pointListFloat);
            FloatProcessor fp = (FloatProcessor) pointlistImp.getProcessor();


            numberOfPoints = fp.getWidth();
            int numberOfCoordinates = fp.getHeight();
            System.out.println("n points: " + numberOfPoints);
            System.out.println("n coords: " + numberOfCoordinates);

            positions = new float[numberOfPoints][numberOfCoordinates];

            // add header
            coordinateList.append("POINTS " + numberOfPoints + " float\n");

            // nth point
            for (int n = 0; n < numberOfPoints; n++) {
                // cth coordinate
                for (int c = 0; c < numberOfCoordinates; c++) {
                    float value = fp.getf(n, c);
                    coordinateList.append(value + " ");
                    positions[n][c] = value;
                }
                coordinateList.append("\n");
            }

            if (pointListFloat != pointlist) {
                clijx.release(pointListFloat);
            }
        }

        //

        int numberOfTouches = (int) clijx.countNonZeroPixels(touchmatrix);
        StringBuilder lineList = new StringBuilder();
        StringBuilder cellData = new StringBuilder();
        {
            cellData.append("CELL_DATA " + numberOfTouches + "\n");
            cellData.append("SCALARS distance float 1\n");
            cellData.append("LOOKUP_TABLE default\n");

            ClearCLBuffer touchPointList = clijx.create(new long[]{numberOfTouches, 2}, clijx.Float);
            ClearCLBuffer temp = clijx.create(touchmatrix);
            clijx.labelSpots(touchmatrix, temp);
            clijx.labelledSpotsToPointList(temp, touchPointList);
            //clijx.spotsToPointList(touchmatrix, touchPointList);

            //new ImageJ();
            //clijx.show(touchPointList, "tpl");
            //new WaitForUserDialog("w").show();

            ImagePlus pointlistImp = clijx.pull(touchPointList);
            FloatProcessor fp = (FloatProcessor) pointlistImp.getProcessor();

            lineList.append("LINES " + numberOfTouches + " " + (numberOfTouches * 3) + "\n"); // its numtouh*3 because: number of points, start, end

            // nth point
            for (int t = 0; t < numberOfTouches; t++) {
                lineList.append("2 ");

                // cth coordinate
                for (int p = 0; p < 2; p++) { // it's always 2 because every line has just one start and end point
                    lineList.append((int)(fp.getf(t, p) - 1) + " ");
                }

                float[] p1 = positions[(int)fp.getf(t, 0) - 1];
                float[] p2 = positions[(int)fp.getf(t, 1) - 1];

                double squaredDistance = 0;
                for (int d = 0; d < p1.length; d++) {
                    squaredDistance = squaredDistance + Math.pow(p1[d] - p2[d], 2);
                }
                double distance = Math.sqrt(squaredDistance);

                cellData.append((float)distance + "\n");

                lineList.append("\n");
            }

            clijx.release(touchPointList);
            clijx.release(temp);


        }
/*
        {
            if (measurements == null) {
                for (int i = 0; i < numberOfTouches; i++) {
                    cellData.append("1\n");
                }
            } else {
                ClearCLBuffer measurementsFloat = measurements;
                if (measurementsFloat.getNativeType() != clijx.Float) {
                    measurementsFloat = clijx.create(measurements.getDimensions(), clijx.Float);
                    clijx.copy(measurements, measurementsFloat);
                }

                float[] arr = new float[(int) measurements.getWidth()];
                FloatBuffer buffer = FloatBuffer.wrap(arr);
                measurementsFloat.writeTo(buffer, true);

                for (int i = 0; i < numberOfTouches; i++) {
                    if (i < arr.length && !(Float.isNaN(arr[i]))) {
                        cellData.append(arr[i] + "\n");
                    }
                }

                if (measurements != measurementsFloat) {
                    clijx.release(measurementsFloat);
                }
            }


        }
*/
        System.out.println(coordinateList.toString());
        System.out.println(lineList.toString());

        StringBuilder vtkFileContent = new StringBuilder();
        vtkFileContent.append("# vtk DataFile Version 2.0\n");
        vtkFileContent.append("\n");
        vtkFileContent.append("ASCII\n");
        vtkFileContent.append("DATASET POLYDATA\n");
        vtkFileContent.append(coordinateList.toString());
        vtkFileContent.append(lineList.toString());
        vtkFileContent.append(cellData.toString());




        System.out.println(vtkFileContent.toString());

        try {
            new File(filename).getParentFile().mkdirs();
            Files.write(Paths.get(filename), vtkFileContent.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();

        }

        /*
         # vtk DataFile Version 2.0
Cube example
ASCII
DATASET POLYDATA
POINTS 8 float
0.0 0.0 0.0
1.0 0.0 0.0
1.0 1.0 0.0
0.0 1.0 0.0
0.0 0.0 1.0
1.0 0.0 1.0
1.0 1.0 1.0
0.0 1.0 1.0
POLYGONS 6 30
40123
44567
40154
42376
40473
41265
         */


        return false;
    }

    @Override
    public String getDescription() {
        return "Takes a point list image representing n points (n*2 for 2D points, n*3 for 3D points) and a corresponding touch matrix , sized (n+1)*(n+1), and exports them in VTK format.";
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

        float[] touches = new float[] {
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 1, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 1, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 1, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 1, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 1, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 1, 0, 0,
                0, 1, 0, 0, 0, 0, 0, 1, 0
        };

        Img<FloatType> point_img = ArrayImgs.floats(points, 8, 3);
        Img<FloatType> touch_imp = ArrayImgs.floats(touches, 9, 9);

        ClearCLBuffer pointlist = clijx.push(point_img);
        ClearCLBuffer touch_matrix = clijx.push(touch_imp);

        writeVTKLineListToDisc(clijx, pointlist, touch_matrix, "C:/structure/temp/temp.vtk");


        clijx.clear();
    }
}
