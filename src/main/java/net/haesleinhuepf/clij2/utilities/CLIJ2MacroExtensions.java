package net.haesleinhuepf.clij2.utilities;

import ij.IJ;
import ij.gui.GenericDialog;
import ij.macro.Functions;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.converters.CLIJConverterService;
import net.haesleinhuepf.clij.macro.CLIJHandler;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import java.util.ArrayList;

/**
 * CLIJ2MacroExtensions
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 11 2018
 */
@Plugin(type = Command.class, menuPath = "Plugins>ImageJ on GPU (CLIJ2)>CLIJ2 Macro Extensions")
public class CLIJ2MacroExtensions implements Command {

    @Parameter
    CLIJConverterService clijConverterService;

    private static boolean warnedOutdatedOpenCLVersion = false;

    @Override
    public void run() {
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
