package net.haesleinhuepf.clij.advancedfilters;

import ij.ImagePlus;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.util.ElapsedTime;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij.utilities.CLIJUtilities;
import net.imglib2.Cursor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;
import org.scijava.plugin.Plugin;

import java.nio.FloatBuffer;
import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;
import static net.haesleinhuepf.clij.utilities.CLIJUtilities.radiusToKernelSize;

/**
 * ConnectedComponentsLabeling
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 06 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_connectedComponentsLabeling")
public class ConnectedComponentsLabeling extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {
    public static int MAX_NUMBER_OF_INDICES_TO_REPLACE_INDIVIDUALLY = 10;

    @Override
    public boolean executeCL() {
            Object[] args = openCLBufferArgs();
            ClearCLBuffer input = (ClearCLBuffer) args[0];
            ClearCLBuffer output = (ClearCLBuffer) args[1];

        boolean result = connectedComponentsLabeling(clij, input, output);
        releaseBuffers(args);
        return result;
    }

    public static boolean connectedComponentsLabeling(CLIJ clij, ClearCLBuffer input, ClearCLBuffer output) {

        ClearCLBuffer temp2 = clij.create(output);

        ClearCLBuffer flag = clij.create(new long[]{1,1,1}, output.getNativeType());

        clij.op().set(flag, 0f);

        ElapsedTime.measureForceOutput("set nonzeros to index", () -> {
            setNonZeroPixelsToPixelIndex(clij, input, output);
        });
        //clij.show(output, "start");
        //if (true) return;

        ElapsedTime.measureForceOutput("iterative min", () -> {
        int flagValue = 1;
        int iterationCount = 0;
        while (flagValue > 0) {
            //clij.show(temp1, "temp1 before " + iterationCount);

            if (iterationCount % 2 == 0) {
                //System.out.println("O>T");
                NonzeroMinimum3DDiamond.nonzeroMinimum3DDiamond(clij, output, flag, temp2);
                //nonzeroMinimumBox(clij, output, flag, temp2, 1, 1, 1);
                //clij.show(temp2, "temp2 after a " + iterationCount);
            } else {
                //System.out.println("T>O");
                NonzeroMinimum3DDiamond.nonzeroMinimum3DDiamond(clij, temp2, flag, output);
                //nonzeroMinimumBox(clij, temp2, flag, output, 1, 1, 1);
                //clij.show(output, "output after a " + iterationCount);
            }
            //clij.show(temp1, "temp1 after b " + iterationCount);
            //if (true) return;

            ImagePlus flagImp = clij.pull(flag);
            flagValue = flagImp.getProcessor().get(0,0);
            //System.out.println("flag: " + flagValue);
            clij.op().set(flag, 0f);
            iterationCount = iterationCount + 1;
            //System.out.println(iterationCount);
        }
        if (iterationCount % 2 == 0) {
            clij.op().copy(output, temp2);
        }
        });

        ElapsedTime.measureForceOutput("shift intensities", () -> {
                    shiftIntensitiesToCloseGaps(clij, temp2, output);
                });
        temp2.close();
        flag.close();

        //new WaitForUserDialog("hello").show();

        return true;
    }

    public static boolean shiftIntensitiesToCloseGaps(CLIJ clij, ClearCLBuffer input, ClearCLBuffer output) {
        int maximum = (int)clij.op().maximumOfAllPixels(input);
        float[] allNewIndices = new float[maximum + 1];

        HashMap<Float, Float> indexFlipMap = new HashMap<Float, Float>();
        RandomAccessibleInterval rai = clij.pullRAI(input);
        Cursor<RealType> cursor = Views.iterable(rai).cursor();
        float count = 1;
        while(cursor.hasNext()) {
            int key = new Float(cursor.next().getRealFloat()).intValue();
            if (key > 0 && allNewIndices[key] == 0) { // && !indexFlipMap.containsKey(key)) {
                allNewIndices[key] = count;
                indexFlipMap.put(new Float(key), count);
                count = count + 1;
            }
        }
        System.out.println("max: " + count);

        if (count > MAX_NUMBER_OF_INDICES_TO_REPLACE_INDIVIDUALLY) {
            System.out.println("replaceAWholeMap");
            ClearCLBuffer keyValueMap = clij.create(new long[]{allNewIndices.length, 1, 1}, NativeTypeEnum.Float);//clij.convert(indexFlipMap, ClearCLBuffer.class);
            keyValueMap.readFrom(FloatBuffer.wrap(allNewIndices), true);
            //clij.show(keyValueMap, "keyValueMap");

            replace(clij, input, keyValueMap, output);

            keyValueMap.close();
        } else {
            System.out.println("replace " + count + " indices individually.");

            boolean flip = false;
            for (Float key : indexFlipMap.keySet()) {
                Float value = indexFlipMap.get(key);
                //System.out.println("replace " + key + " " + value);

                if (flip) {
                    ReplaceIntensity.replaceIntensity(clij, input, output, key, value);
                } else {
                    ReplaceIntensity.replaceIntensity(clij, output, input, key, value);
                }
                flip = !flip;
            }
            if (flip) {
                clij.op().copy(input, output);
            }
        }
        return true;
    }

    public static boolean replace(CLIJ clij, ClearCLBuffer src, ClearCLBuffer map, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();

        parameters.clear();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("map", map);
        return clij.execute(ConnectedComponentsLabeling.class, "cca.cl", "replace_by_map", parameters);
    }

    public static boolean setNonZeroPixelsToPixelIndex(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();

        parameters.clear();
        parameters.put("src", src);
        parameters.put("dst", dst);
        return clij.execute(ConnectedComponentsLabeling.class, "cca.cl", "set_nonzero_pixels_to_pixelindex", parameters);
    }

    public static boolean nonzeroMinimumBox(CLIJ clij, ClearCLBuffer src, ClearCLBuffer flag, ClearCLBuffer dst, int radiusX, int radiusY, int radiusZ) {
        return executeSeparableKernel(clij, src, flag, dst, "cca.cl", "min_sep_image" + src.getDimension() + "d", radiusToKernelSize(radiusX), radiusToKernelSize(radiusY), radiusToKernelSize(radiusZ), radiusX, radiusY, radiusZ, src.getDimension());
    }

    private static boolean executeSeparableKernel(CLIJ clij, ClearCLBuffer src, ClearCLBuffer flag, ClearCLBuffer dst, String clFilename, String kernelname, int kernelSizeX, int kernelSizeY, int kernelSizeZ, float blurSigmaX, float blurSigmaY, float blurSigmaZ, long dimensions) {
        CLIJUtilities.assertDifferent(src, dst);

        int[] n = new int[]{kernelSizeX, kernelSizeY, kernelSizeZ};
        float[] blurSigma = new float[]{blurSigmaX, blurSigmaY, blurSigmaZ};

        ClearCLBuffer temp = clij.createCLBuffer((ClearCLBuffer) src);

        HashMap<String, Object> parameters = new HashMap<>();

        if (blurSigma[0] > 0) {
            parameters.clear();
            parameters.put("N", n[0]);
            parameters.put("s", blurSigma[0]);
            parameters.put("dim", 0);
            parameters.put("src", src);
            if (dimensions == 2) {
                parameters.put("dst", temp);
            } else {
                parameters.put("dst", dst);
            }
            parameters.put("flag_dst", flag);
            clij.execute(ConnectedComponentsLabeling.class, clFilename, kernelname, dst.getDimensions(), parameters);
        } else {
            if (dimensions == 2) {
                copyInternal(clij, src, temp, 2, 2);
            } else {
                copyInternal(clij, src, dst, 3, 3);
            }
        }

        if (blurSigma[1] > 0) {
            parameters.clear();
            parameters.put("N", n[1]);
            parameters.put("s", blurSigma[1]);
            parameters.put("dim", 1);
            if (dimensions == 2) {
                parameters.put("src", temp);
                parameters.put("dst", dst);
            } else {
                parameters.put("src", dst);
                parameters.put("dst", temp);
            }
            parameters.put("flag_dst", flag);
            clij.execute(ConnectedComponentsLabeling.class, clFilename, kernelname, dst.getDimensions(),parameters);
        } else {
            if (dimensions == 2) {
                copyInternal(clij, temp, dst, 2, 2);
            } else {
                copyInternal(clij, dst, temp, 3, 3);
            }
        }

        if (dimensions == 3) {
            if (blurSigma[2] > 0) {
                parameters.clear();
                parameters.put("N", n[2]);
                parameters.put("s", blurSigma[2]);
                parameters.put("dim", 2);
                parameters.put("src", temp);
                parameters.put("dst", dst);
                parameters.put("flag_dst", flag);
                clij.execute(ConnectedComponentsLabeling.class, clFilename, kernelname, dst.getDimensions(), parameters);
            } else {
                copyInternal(clij, temp, dst,3, 3);
            }
        }

        temp.close();

        return true;
    }

    private static boolean copyInternal(CLIJ clij, Object src, Object dst, long srcNumberOfDimensions, long dstNumberOfDimensions) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        if (!checkDimensions(srcNumberOfDimensions, dstNumberOfDimensions)) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }
        return clij.execute(Kernels.class, "duplication.cl", "copy_" + srcNumberOfDimensions + "d", parameters);
    }


    private static boolean checkDimensions(long... numberOfDimensions) {
        for (int i = 0; i < numberOfDimensions.length - 1; i++) {
            if (!(numberOfDimensions[i] == numberOfDimensions[i + 1])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image binary_input, Image labeling_destination";
    }

    @Override
    public String getDescription() {
        return "Performs connected components analysis to a binary image and generates a label map.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        return clij.create(input.getDimensions(), NativeTypeEnum.Float);
    }
}
