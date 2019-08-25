package net.haesleinhuepf.clij.io;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.io.preloader.ImagePreloader;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

/**
 * PreloadFromDisc
 *
 * Todo: Preloaders block memory internally when being used once. Fix this!
 *
 * Author: @haesleinhuepf
 *         August 2019
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_preloadFromDisc")
public class PreloadFromDisc extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {
    private static HashMap<String, ImagePreloader> loaderMap = new HashMap<String, ImagePreloader>();

    private String bufferedImagefilename = "";

    private final static String DEFAULT_LOADER_ID = "default";

    @Override
    public String getParameterHelpText() {
        return "Image destination, String filename, String nextFilename, String loaderId";
    }

    private static ImagePreloader getPreloader(String id) {
        synchronized (loaderMap) {
            if (loaderMap.containsKey(id)) {
                return loaderMap.get(id);
            } else {
                ImagePreloader preloader = new ImagePreloader();
                loaderMap.put(id, preloader);
                return preloader;
            }
        }
    }

    @Override
    public boolean executeCL() {
        ClearCLBuffer buffer = (ClearCLBuffer) args[0];
        String currentFileName = args[1].toString();
        String nextFileName = args[2].toString();
        String loaderId = args[3].toString();
        if (loaderId.length() == 0) {
            loaderId = DEFAULT_LOADER_ID;
        }
        if (bufferedImagefilename.compareTo(currentFileName) != 0) {
            preloadFromDisc(clij, buffer, currentFileName, nextFileName, loaderId);
        }
        return true;
    }

    public static ClearCLBuffer preloadFromDisc(CLIJ clij, ClearCLBuffer buffer, String currentFileName, String nextFileName, String loaderId) {
        ImagePreloader imagePreloader = getPreloader(loaderId);
        imagePreloader.setCLIJ(clij);
        buffer = imagePreloader.load(buffer, currentFileName, nextFileName);
        return buffer;
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        String currentFileName = args[1].toString();
        String nextFileName = args[2].toString();
        String loaderId = args[3].toString();
        if (loaderId.length() == 0) {
            loaderId = DEFAULT_LOADER_ID;
        }

        ImagePreloader imagePreloader = getPreloader(loaderId);
        imagePreloader.setCLIJ(clij);
        ClearCLBuffer buffer = imagePreloader.load(null, currentFileName, nextFileName);
        bufferedImagefilename = currentFileName;
        return buffer;
    }

    @Override
    public String getDescription() {
        return "This plugin takes two image filenames and loads them into RAM. " +
                "The first image is returned immediately, the second image is loaded in the background and " +
                " will be returned when the plugin is called again.\n\n" +
                " It is assumed that all images have the same size. If this is not the case, call release(image) before " +
                " getting the second image.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
