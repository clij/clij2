package net.haesleinhuepf.clijx.io.preloader;

import ij.IJ;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clijx.io.ReadRawImageFromDisc;

public class ImagePreloader {
    private String currentFileName = "";
    private String nextLoadedImageFileName = "";

    private Object loaderLock = new Object();

    private ClearCLBuffer currentImage = null;
    private ClearCLBuffer nextImage = null;

    private CLIJ clij;

    private Loader loader = null;

    public ClearCLBuffer load(ClearCLBuffer buffer, String currentFileName, String nextFileName) {
        if (buffer == null) {
            buffer = loadInternal(buffer, currentFileName);
            this.currentFileName = currentFileName;
        }
        if (currentImage != buffer) {
            this.currentImage = buffer;
            if (nextImage != null) {
                nextImage.close();
            }
            this.nextImage = clij.create(buffer);
        }
        synchronized (loaderLock) {
            if (this.currentFileName != currentFileName) { // image already there
                if (nextLoadedImageFileName.compareTo(currentFileName) != 0) {
                    // images was not loaded yet
                    if (loader == null || this.currentFileName.compareTo(currentFileName) != 0) { // the image has never been requested before
                        currentImage = loadInternal(currentImage, currentFileName);
                        this.currentFileName = currentFileName;
                    } else { // if the loader was initialized earlier, wait for it to finish
                        // try again later
                        return sleepload(buffer, currentFileName, nextFileName);
                    }
                } else {
                    // next image was loaded already
                    clij.op().copy(nextImage, currentImage);
                    currentImage = nextImage;
                    this.currentFileName = nextLoadedImageFileName;
                }
            }

            // preload the next image in a separate thread
            loader = new Loader(nextFileName);
            loader.start();
        }
        return currentImage;
    }

    private ClearCLBuffer sleepload(ClearCLBuffer buffer, String currentFileName, String nextFileName) {
        try {
            loader.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        return load(buffer, currentFileName, nextFileName);
    }

    private class Loader extends Thread {
        String filenameToload;

        public Loader(String filenameToload) {
            this.filenameToload = filenameToload;
        }

        @Override
        public void run() {
            synchronized (loaderLock) {
                nextImage = loadInternal(nextImage, filenameToload);
                nextLoadedImageFileName = filenameToload;
            }
        }
    }

    private ClearCLBuffer loadInternal(ClearCLBuffer buffer, String filenameToload) {
        if (buffer == null) {
            if (filenameToload.toLowerCase().endsWith(".raw")) {
                throw new IllegalArgumentException("cannot load images of type raw without knowing its size. Create an image with the correct size and type first!");
            } else {
                buffer = clij.push(IJ.openImage(filenameToload));
            }
        } else if (filenameToload.toLowerCase().endsWith(".raw")) {
            ReadRawImageFromDisc.readRawImageFromDisc(clij, buffer, filenameToload);
        } else {
            if (buffer == nextImage) {
                if (buffer != null) {
                    buffer.close();
                }
                buffer = clij.push(IJ.openImage(filenameToload));
            } else {
                ClearCLBuffer result = clij.push(IJ.openImage(filenameToload));
                clij.op().copy(result, buffer);
                result.close();
            }
        }
        return buffer;
    }

    public void setCLIJ(CLIJ clij) {
        this.clij = clij;
    }
}
