package net.haesleinhuepf.clijx.advancedfilters;

import ij.ImagePlus;
import ij.gui.WaitForUserDialog;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLKernel;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
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
        ClearCLBuffer distanceMap2 = clijx.create(thresholded);
        ClearCLBuffer localMaxima = clijx.create(thresholded);
        ClearCLBuffer labelMap = clijx.create(thresholded);
        ClearCLBuffer labelMap2 = clijx.create(thresholded);

        clijx.distanceMap(thresholded, distanceMap);

        clijx.show(distanceMap, "distanceMap");

        Watershed.detectMaximaRegionBox(clijx.getClij(), distanceMap, localMaxima);
        ConnectedComponentsLabeling.connectedComponentsLabeling(clijx, localMaxima, labelMap);
        Watershed.dilateLabelsUntilNoChange(clijx, distanceMap, labelMap, distanceMap2, labelMap2);

        clijx.show(localMaxima, "localMaxima");
        clijx.show(labelMap2, "labelMap2");

        clijx.clear();
        System.out.println(clijx.reportMemory());

        new WaitForUserDialog("wait").show();

        return true;
    }

    public static boolean detectMaximaRegionBox(CLIJ clij, ClearCLBuffer input, ClearCLBuffer output) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", input);
        parameters.put("dst", output);
        clij.execute(Watershed.class, "watershed_detect_maxima_region_box_" + output.getDimension() + "d.cl", "watershed_detect_maxima_region_box_" + output.getDimension() + "d", parameters);
        return true;
    }

    private static ClearCLKernel dilateLabelsUntilNoChange(CLIJx clijx, ClearCLBuffer distanceMapIn, ClearCLBuffer labelMapIn, ClearCLBuffer flag, ClearCLBuffer distanceMapOut, ClearCLBuffer labelMapOut, ClearCLKernel kernel) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src_labelmap", labelMapIn);
        parameters.put("src_distancemap", distanceMapIn);
        parameters.put("dst_labelmap", labelMapOut);
        parameters.put("dst_distancemap", distanceMapOut);
        parameters.put("flag_dst", flag);
        return clijx.executeSubsequently(Watershed.class, "watershed_local_maximum_" + labelMapOut.getDimension() + "d_x.cl", "watershed_local_maximum_" + labelMapOut.getDimension() + "d", labelMapOut.getDimensions(), labelMapOut.getDimensions(), parameters, kernel);
    }


    public static boolean dilateLabelsUntilNoChange(CLIJx clijx, ClearCLBuffer distanceMapIn, ClearCLBuffer labelMapIn, ClearCLBuffer distanceMapOut, ClearCLBuffer labelMapOut) {

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
                clijx.saveAsTIF(distanceMapOut, "c:/structure/temp/dst/" + iterationCount[0] + ".tif");
                clijx.saveAsTIF(labelMapOut, "c:/structure/temp/lab/" + iterationCount[0] + ".tif");
            } else {
                if (flopkernel == null) {
                    flopkernel = dilateLabelsUntilNoChange(clijx, distanceMapOut, labelMapOut, flag, distanceMapIn, labelMapIn, flopkernel);
                } else {
                    flopkernel.run(true);
                }
                clijx.saveAsTIF(distanceMapIn, "c:/structure/temp/dst/" + iterationCount[0] + ".tif");
                clijx.saveAsTIF(labelMapOut, "c:/structure/temp/lab/" + iterationCount[0] + ".tif");
            }

            ImagePlus flagImp = clijx.pull(flag);
            flagValue = flagImp.getProcessor().get(0,0);
            flag.readFrom(aByteBufferWithAZero, true);
            iterationCount[0] = iterationCount[0] + 1;
            System.out.println("cycling " + iterationCount[0]);
        }

        if (iterationCount[0] % 2 == 0) {
            ConnectedComponentsLabeling.copyInternal(clijx.getClij(), labelMapIn, labelMapOut, labelMapOut.getDimension(), labelMapOut.getDimension());
            ConnectedComponentsLabeling.copyInternal(clijx.getClij(), distanceMapIn, distanceMapOut, distanceMapOut.getDimension(), distanceMapOut.getDimension());
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
        return "Image source, Image destination";
    }

    @Override
    public String getDescription() {
        return "Detects local maxima regions. Pixels in the resulting image are set to 1 if\n" +
                "there is no other pixel in a given radius which has a higher intensity, and to 0 otherwise.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
