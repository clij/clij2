package net.haesleinhuepf.clijx.gui;

import ij.ImagePlus;
import ij.WindowManager;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

/**
 * 	Author: @haesleinhuepf
 * 	        August 2019
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_organiseWindows")
public class OrganiseWindows extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {

        int startX = asInteger(args[0]);
        int startY = asInteger(args[1]);
        int tilesX = asInteger(args[2]);
        int tilesY = asInteger(args[3]);
        int tileWidth = asInteger(args[4]);
        int tileHeight = asInteger(args[5]);

        return organiseWindows(clij, startX, startY, tilesX, tilesY, tileWidth, tileHeight);

    }

    public static boolean organiseWindows(CLIJ clij, Integer startX, Integer startY, Integer tilesX, Integer tilesY, Integer tileWidth, Integer tileHeight) {
        int xCount = 0;
        int yCount = 0;
        if (WindowManager.getIDList() == null) {
            return false;
        }
        for (int id : WindowManager.getIDList()) {
            ImagePlus imp = WindowManager.getImage(id);
            imp.getWindow().setLocationAndSize( startX + xCount * tileWidth, startY + yCount * tileHeight, tileWidth, tileHeight );
            yCount ++;
            if (yCount >= tilesY) {
                yCount = 0;
                xCount ++;
                if(xCount >= tilesX) {
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public String getParameterHelpText() {
        return "Number startX, Number startY, Number tilesX, Number tilesY, Number tileWidth, Number tileHeight";
    }

    @Override
    public String getDescription() {
        return "Organises windows on screen.";
    }
    
    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
