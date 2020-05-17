package net.haesleinhuepf.clij2.plugins;

import ij.ImagePlus;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLKernel;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

import java.nio.ByteBuffer;
import java.util.HashMap;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_watershed")
public class Watershed extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        return watershed(getCLIJ2(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]));
    }

    public static boolean watershed(CLIJ2 clij2, ClearCLBuffer thresholded, ClearCLBuffer output) {
        ClearCLBuffer distanceMap = clij2.create(thresholded);
        clij2.distanceMap(thresholded, distanceMap);

        ClearCLBuffer localMaxima = clij2.create(thresholded);
        ClearCLBuffer localMaxima2 = clij2.create(thresholded);
        detectMaximaRegionBox(clij2.getClij(), distanceMap, localMaxima);
        eliminateWrongMaxima(clij2, localMaxima, distanceMap, localMaxima2);
        clij2.release(localMaxima);

        ClearCLBuffer labelMap = clij2.create(thresholded);
        //clij2.labelSpots(localMaxima2, labelMap);
        ConnectedComponentsLabeling.connectedComponentsLabeling(clij2, localMaxima2, labelMap);
        clij2.release(localMaxima2);

        ClearCLBuffer labelMap2 = clij2.create(thresholded);
        ClearCLBuffer distanceMap2 = clij2.create(thresholded);
        Watershed.dilateLabelsUntilNoChange(clij2, distanceMap, labelMap, distanceMap2, labelMap2);
        clij2.release(distanceMap2);
        clij2.release(labelMap);
        clij2.release(distanceMap);

        binarizeLabelmap(clij2, labelMap2, output);
        clij2.release(labelMap2);
        // shiftIntensitiesToCloseGaps(clijx, temp3, output);

        return true;
    }

    static boolean binarizeLabelmap(CLIJ2 clij2, ClearCLBuffer input, ClearCLBuffer output) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src_labelmap", input);
        parameters.put("dst_binary", output);
        clij2.execute(Watershed.class, "watershed_binarize_labelmap_" + output.getDimension() + "d_x.cl", "watershed_binarize_labelmap_" + output.getDimension() + "d", output.getDimensions(), output.getDimensions(), parameters);
        return true;
    }

    static boolean detectMaximaRegionBox(CLIJ clij, ClearCLBuffer input, ClearCLBuffer output) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", input);
        parameters.put("dst", output);
        clij.execute(Watershed.class, "watershed_detect_maxima_region_box_" + output.getDimension() + "d.cl", "watershed_detect_maxima_region_box_" + output.getDimension() + "d", parameters);
        return true;
    }

    static ClearCLKernel dilateLabelsUntilNoChange(CLIJ2 clij2, ClearCLBuffer distanceMapIn, ClearCLBuffer labelMapIn, ClearCLBuffer flag, ClearCLBuffer distanceMapOut, ClearCLBuffer labelMapOut, ClearCLKernel kernel) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src_labelmap", labelMapIn);
        parameters.put("src_distancemap", distanceMapIn);
        parameters.put("dst_labelmap", labelMapOut);
        parameters.put("dst_distancemap", distanceMapOut);
        parameters.put("flag_dst", flag);
        return clij2.executeSubsequently(Watershed.class, "watershed_local_maximum_" + labelMapOut.getDimension() + "d_x.cl", "watershed_local_maximum_" + labelMapOut.getDimension() + "d", labelMapOut.getDimensions(), labelMapOut.getDimensions(), parameters, kernel);
    }

    static ClearCLKernel eliminateWrongMaxima(CLIJ2 clij2, ClearCLBuffer maximaIn, ClearCLBuffer distanceMapIn, ClearCLBuffer flag, ClearCLBuffer maximaOut, ClearCLKernel kernel) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src_distancemap", distanceMapIn);
        parameters.put("src_maxima", maximaIn);
        parameters.put("dst_maxima", maximaOut);
        parameters.put("flag_dst", flag);
        return clij2.executeSubsequently(Watershed.class, "watershed_eliminate_wrong_maxima_" + maximaOut.getDimension() + "d_x.cl", "watershed_eliminate_wrong_maxima_" + maximaOut.getDimension() + "d", maximaOut.getDimensions(), maximaOut.getDimensions(), parameters, kernel);
    }

    static boolean eliminateWrongMaxima(CLIJ2 clij2, ClearCLBuffer maximaIn, ClearCLBuffer distanceMapIn, ClearCLBuffer maximaOut) {
        ClearCLBuffer flag = clij2.create(new long[]{1,1,1}, NativeTypeEnum.Byte);
        ByteBuffer aByteBufferWithAZero = ByteBuffer.allocate(1);
        aByteBufferWithAZero.put((byte)0);
        flag.readFrom(aByteBufferWithAZero, true);

        clij2.set(maximaOut, 0f);

        final int[] iterationCount = {0};
        int flagValue = 1;

        ClearCLKernel flipkernel = null;
        ClearCLKernel flopkernel = null;

        while (flagValue > 0) {
            if (iterationCount[0] % 2 == 0) {
                if (flipkernel == null) {
                    flipkernel = eliminateWrongMaxima(clij2, maximaIn, distanceMapIn, flag, maximaOut, flipkernel);
                } else {
                    flipkernel.run(true);
                }
                //clijx.saveAsTIF(maximaOut, "c:/structure/temp/max/" + iterationCount[0] + ".tif");
            } else {
                if (flopkernel == null) {
                    flopkernel = eliminateWrongMaxima(clij2, maximaOut, distanceMapIn, flag, maximaIn, flopkernel);
                } else {
                    flopkernel.run(true);
                }
                //clijx.saveAsTIF(maximaIn, "c:/structure/temp/max/" + iterationCount[0] + ".tif");
            }

            ImagePlus flagImp = clij2.pull(flag);
            flagValue = flagImp.getProcessor().get(0,0);
            flag.readFrom(aByteBufferWithAZero, true);
            iterationCount[0] = iterationCount[0] + 1;
            //System.out.println("cyclingf " + iterationCount[0]);
        }

        if (iterationCount[0] % 2 == 0) {
           clij2.copy(maximaIn, maximaOut);
        }
        if (flipkernel != null) {
            flipkernel.close();
        }
        if (flopkernel != null) {
            flopkernel.close();
        }
        flag.close();

        return true;
    }

    static boolean dilateLabelsUntilNoChange(CLIJ2 clij2, ClearCLBuffer distanceMapIn, ClearCLBuffer labelMapIn, ClearCLBuffer distanceMapOut, ClearCLBuffer labelMapOut) {

        ClearCLBuffer flag = clij2.create(new long[]{1,1,1}, NativeTypeEnum.Byte);
        ByteBuffer aByteBufferWithAZero = ByteBuffer.allocate(1);
        aByteBufferWithAZero.put((byte)0);
        flag.readFrom(aByteBufferWithAZero, true);

        clij2.set(labelMapOut, 0f);
        clij2.set(distanceMapOut, 0f);


        final int[] iterationCount = {0};
        int flagValue = 1;

        ClearCLKernel flipkernel = null;
        ClearCLKernel flopkernel = null;

        while (flagValue > 0) {
            if (iterationCount[0] % 2 == 0) {
                if (flipkernel == null) {
                    flipkernel = dilateLabelsUntilNoChange(clij2, distanceMapIn, labelMapIn, flag, distanceMapOut, labelMapOut, flipkernel);
                } else {
                    flipkernel.run(true);
                }
                //clijx.saveAsTIF(distanceMapOut, "c:/structure/temp/dst/" + iterationCount[0] + ".tif");
                //clijx.saveAsTIF(labelMapOut, "c:/structure/temp/lab/" + iterationCount[0] + ".tif");
            } else {
                if (flopkernel == null) {
                    flopkernel = dilateLabelsUntilNoChange(clij2, distanceMapOut, labelMapOut, flag, distanceMapIn, labelMapIn, flopkernel);
                } else {
                    flopkernel.run(true);
                }
                //clijx.saveAsTIF(distanceMapIn, "c:/structure/temp/dst/" + iterationCount[0] + ".tif");
                //clijx.saveAsTIF(labelMapOut, "c:/structure/temp/lab/" + iterationCount[0] + ".tif");
            }

            ImagePlus flagImp = clij2.pull(flag);
            flagValue = flagImp.getProcessor().get(0,0);
            flag.readFrom(aByteBufferWithAZero, true);
            iterationCount[0] = iterationCount[0] + 1;
            //System.out.println("cycling " + iterationCount[0]);
        }
        flag.close();

        if (iterationCount[0] % 2 == 0) {
            clij2.copy(labelMapIn, labelMapOut);
            clij2.copy(distanceMapIn, distanceMapOut);
        }
        if (flipkernel != null) {
            flipkernel.close();
        }
        if (flopkernel != null) {
            flopkernel.close();
        }

        return true;
    }


    @Override
    public String getParameterHelpText() {
        return "Image binary_source, ByRef Image destination";
    }

    @Override
    public String getDescription() {
        return "Apply a binary watershed to a binary image and introduces black pixels between objects.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
