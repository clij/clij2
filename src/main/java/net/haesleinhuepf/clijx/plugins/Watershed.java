package net.haesleinhuepf.clijx.plugins;

import ij.ImagePlus;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLKernel;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.plugins.ConnectedComponentsLabeling;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

import java.nio.ByteBuffer;
import java.util.HashMap;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_watershed")
public class Watershed extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        return watershed(getCLIJx(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]));
    }

    public static boolean watershed(CLIJx clijx, ClearCLBuffer thresholded, ClearCLBuffer output) {
        ClearCLBuffer distanceMap = clijx.create(thresholded);
        clijx.distanceMap(thresholded, distanceMap);

        ClearCLBuffer localMaxima = clijx.create(thresholded);
        ClearCLBuffer localMaxima2 = clijx.create(thresholded);
        detectMaximaRegionBox(clijx.getClij(), distanceMap, localMaxima);
        eliminateWrongMaxima(clijx, localMaxima, distanceMap, localMaxima2);
        clijx.release(localMaxima);

        ClearCLBuffer labelMap = clijx.create(thresholded);
        ConnectedComponentsLabeling.connectedComponentsLabeling(clijx, localMaxima2, labelMap);
        clijx.release(localMaxima2);

        ClearCLBuffer labelMap2 = clijx.create(thresholded);
        ClearCLBuffer distanceMap2 = clijx.create(thresholded);
        Watershed.dilateLabelsUntilNoChange(clijx, distanceMap, labelMap, distanceMap2, labelMap2);
        clijx.release(distanceMap2);
        clijx.release(labelMap);
        clijx.release(distanceMap);

        binarizeLabelmap(clijx, labelMap2, output);
        clijx.release(labelMap2);
        // shiftIntensitiesToCloseGaps(clijx, temp3, output);

        return true;
    }

    static boolean binarizeLabelmap(CLIJx clijx, ClearCLBuffer input, ClearCLBuffer output) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src_labelmap", input);
        parameters.put("dst_binary", output);
        clijx.execute(Watershed.class, "watershed_binarize_labelmap_" + output.getDimension() + "d_x.cl", "watershed_binarize_labelmap_" + output.getDimension() + "d", output.getDimensions(), output.getDimensions(), parameters);
        return true;
    }

    static boolean detectMaximaRegionBox(CLIJ clij, ClearCLBuffer input, ClearCLBuffer output) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", input);
        parameters.put("dst", output);
        clij.execute(Watershed.class, "watershed_detect_maxima_region_box_" + output.getDimension() + "d.cl", "watershed_detect_maxima_region_box_" + output.getDimension() + "d", parameters);
        return true;
    }

    static ClearCLKernel dilateLabelsUntilNoChange(CLIJx clijx, ClearCLBuffer distanceMapIn, ClearCLBuffer labelMapIn, ClearCLBuffer flag, ClearCLBuffer distanceMapOut, ClearCLBuffer labelMapOut, ClearCLKernel kernel) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src_labelmap", labelMapIn);
        parameters.put("src_distancemap", distanceMapIn);
        parameters.put("dst_labelmap", labelMapOut);
        parameters.put("dst_distancemap", distanceMapOut);
        parameters.put("flag_dst", flag);
        return clijx.executeSubsequently(Watershed.class, "watershed_local_maximum_" + labelMapOut.getDimension() + "d_x.cl", "watershed_local_maximum_" + labelMapOut.getDimension() + "d", labelMapOut.getDimensions(), labelMapOut.getDimensions(), parameters, kernel);
    }

    static ClearCLKernel eliminateWrongMaxima(CLIJx clijx, ClearCLBuffer maximaIn, ClearCLBuffer distanceMapIn, ClearCLBuffer flag, ClearCLBuffer maximaOut, ClearCLKernel kernel) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src_distancemap", distanceMapIn);
        parameters.put("src_maxima", maximaIn);
        parameters.put("dst_maxima", maximaOut);
        parameters.put("flag_dst", flag);
        return clijx.executeSubsequently(Watershed.class, "watershed_eliminate_wrong_maxima_" + maximaOut.getDimension() + "d_x.cl", "watershed_eliminate_wrong_maxima_" + maximaOut.getDimension() + "d", maximaOut.getDimensions(), maximaOut.getDimensions(), parameters, kernel);
    }

    static boolean eliminateWrongMaxima(CLIJx clijx, ClearCLBuffer maximaIn, ClearCLBuffer distanceMapIn, ClearCLBuffer maximaOut) {
        ClearCLBuffer flag = clijx.create(new long[]{1,1,1}, NativeTypeEnum.Byte);
        ByteBuffer aByteBufferWithAZero = ByteBuffer.allocate(1);
        aByteBufferWithAZero.put((byte)0);
        flag.readFrom(aByteBufferWithAZero, true);

        clijx.set(maximaOut, 0f);

        final int[] iterationCount = {0};
        int flagValue = 1;

        ClearCLKernel flipkernel = null;
        ClearCLKernel flopkernel = null;

        while (flagValue > 0) {
            if (iterationCount[0] % 2 == 0) {
                if (flipkernel == null) {
                    flipkernel = eliminateWrongMaxima(clijx, maximaIn, distanceMapIn, flag, maximaOut, flipkernel);
                } else {
                    flipkernel.run(true);
                }
                //clijx.saveAsTIF(maximaOut, "c:/structure/temp/max/" + iterationCount[0] + ".tif");
            } else {
                if (flopkernel == null) {
                    flopkernel = eliminateWrongMaxima(clijx, maximaOut, distanceMapIn, flag, maximaIn, flopkernel);
                } else {
                    flopkernel.run(true);
                }
                //clijx.saveAsTIF(maximaIn, "c:/structure/temp/max/" + iterationCount[0] + ".tif");
            }

            ImagePlus flagImp = clijx.pull(flag);
            flagValue = flagImp.getProcessor().get(0,0);
            flag.readFrom(aByteBufferWithAZero, true);
            iterationCount[0] = iterationCount[0] + 1;
            //System.out.println("cyclingf " + iterationCount[0]);
        }

        if (iterationCount[0] % 2 == 0) {
           clijx.copy(maximaIn, maximaOut);
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

    static boolean dilateLabelsUntilNoChange(CLIJx clijx, ClearCLBuffer distanceMapIn, ClearCLBuffer labelMapIn, ClearCLBuffer distanceMapOut, ClearCLBuffer labelMapOut) {

        ClearCLBuffer flag = clijx.create(new long[]{1,1,1}, NativeTypeEnum.Byte);
        ByteBuffer aByteBufferWithAZero = ByteBuffer.allocate(1);
        aByteBufferWithAZero.put((byte)0);
        flag.readFrom(aByteBufferWithAZero, true);

        clijx.set(labelMapOut, 0f);
        clijx.set(distanceMapOut, 0f);


        final int[] iterationCount = {0};
        int flagValue = 1;

        ClearCLKernel flipkernel = null;
        ClearCLKernel flopkernel = null;

        while (flagValue > 0) {
            if (iterationCount[0] % 2 == 0) {
                if (flipkernel == null) {
                    flipkernel = dilateLabelsUntilNoChange(clijx, distanceMapIn, labelMapIn, flag, distanceMapOut, labelMapOut, flipkernel);
                } else {
                    flipkernel.run(true);
                }
                //clijx.saveAsTIF(distanceMapOut, "c:/structure/temp/dst/" + iterationCount[0] + ".tif");
                //clijx.saveAsTIF(labelMapOut, "c:/structure/temp/lab/" + iterationCount[0] + ".tif");
            } else {
                if (flopkernel == null) {
                    flopkernel = dilateLabelsUntilNoChange(clijx, distanceMapOut, labelMapOut, flag, distanceMapIn, labelMapIn, flopkernel);
                } else {
                    flopkernel.run(true);
                }
                //clijx.saveAsTIF(distanceMapIn, "c:/structure/temp/dst/" + iterationCount[0] + ".tif");
                //clijx.saveAsTIF(labelMapOut, "c:/structure/temp/lab/" + iterationCount[0] + ".tif");
            }

            ImagePlus flagImp = clijx.pull(flag);
            flagValue = flagImp.getProcessor().get(0,0);
            flag.readFrom(aByteBufferWithAZero, true);
            iterationCount[0] = iterationCount[0] + 1;
            //System.out.println("cycling " + iterationCount[0]);
        }
        flag.close();

        if (iterationCount[0] % 2 == 0) {
            clijx.copy(labelMapIn, labelMapOut);
            clijx.copy(distanceMapIn, distanceMapOut);
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
        return "Image binary_source, Image destination";
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
