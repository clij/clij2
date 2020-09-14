package net.haesleinhuepf.clij2.legacy;

import ij.IJ;
import ij.gui.GenericDialog;
import ij.macro.ExtensionDescriptor;
import ij.macro.Functions;
import ij.plugin.PlugIn;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.converters.CLIJConverterService;
import net.haesleinhuepf.clij.converters.FallBackCLIJConverterService;
import net.haesleinhuepf.clij.macro.CLIJHandler;
import net.haesleinhuepf.clij.macro.CLIJMacroExtensions;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * Author: @haesleinhuepf
 * June 2019
 */
public class CLIJLegacyMacroExtensions implements PlugIn {

    private static boolean warnedOutdatedOpenCLVersion = false;

    @Override
    public void run(String s) {
        ArrayList<String> deviceList = CLIJ.getAvailableDeviceNames();
        String[] deviceArray = new String[deviceList.size()];
        deviceList.toArray(deviceArray);

        GenericDialog gd = new GenericDialog("CLIJ");
        gd.addChoice("CL_Device", deviceArray, deviceArray[0]);
        gd.showDialog();

        if (gd.wasCanceled()) {
            return;
        }

        // macro extensions and converters must use the same CLIJ instance in order to make everything run on the same GPU.
        CLIJ clij = CLIJ.getInstance(gd.getNextChoice());
        CLIJHandler.automaticOutputVariableNaming = true;
        //CLIJMacroExtensions cme = new CLIJMacroExtensions();

        CLIJHandler.getInstance().setPluginService(new FallBackCLIJMacroPluginService());
        System.out.println("output naming " + CLIJHandler.automaticOutputVariableNaming);
        ExtensionDescriptor ed = CLIJHandler.getInstance().getPluginService().getPluginExtensionDescriptor("CLIJ2_gaussianBlur2D");
        System.out.println("Argtypes: " + Arrays.toString(ed.argTypes));
        System.out.println("output naming " + CLIJHandler.automaticOutputVariableNaming);

        //cme.run();

        //IJ.log("Found clij extensions: ");
        //for (ExtensionDescriptor ed : CLIJHandler.getInstance().getExtensionFunctions()) {
        //    IJ.log(ed.name);
        //}

        CLIJConverterService clijConverterService = FallBackCLIJConverterService.getInstance();
        clijConverterService.setCLIJ(clij);
        clij.setConverterService(clijConverterService);

        if (!warnedOutdatedOpenCLVersion) {
            if (clij.getOpenCLVersion() < 1.2) {
                IJ.log("Warning: Your GPU does not support OpenCL 1.2. Some operations may not work precisely. For example: CLIJ does not support linear interpolation; it uses nearest-neighbor interpolation instead. Consider upgrading GPU Driver version or GPU hardware.");
                warnedOutdatedOpenCLVersion = true;
            }
        }

        if (!IJ.macroRunning()) {
            IJ.error("Cannot install extensions from outside a macro.");
            return;
        }
        Functions.registerExtensions(CLIJHandler.getInstance());

    }
}