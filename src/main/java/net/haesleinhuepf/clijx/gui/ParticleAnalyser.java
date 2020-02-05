package net.haesleinhuepf.clijx.gui;

import ij.IJ;
import ij.ImagePlus;
import ij.Prefs;
import ij.WindowManager;
import ij.gui.GenericDialog;
import ij.measure.ResultsTable;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;
import ij.util.Tools;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij2.plugins.ReplaceIntensities;
import net.haesleinhuepf.clij2.plugins.ConnectedComponentsLabeling;
import net.haesleinhuepf.clij2.plugins.ExcludeLabelsOnEdges;
import net.haesleinhuepf.clij2.plugins.StatisticsOfLabelledPixels;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clijx.CLIJx;

import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Vector;

/**
 *
 *
 * Code is inspired by the original 3D Objects counter by Fabrice Cordelieres:
 * https://github.com/fiji/3D_Objects_Counter/blob/master/src/main/java/_3D_objects_counter.java
 *  
 *  _3D_objects_counter.java
 *  *
 *  * Created on 7 novembre 2007, 11:54
 *  *
 *  * Copyright (C) 2007 Fabrice P. Cordelières
 *  *  
 *  * License:
 *  * This program is free software; you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as published by
 *  * the Free Software Foundation; either version 3 of the License, or
 *  * (at your option) any later version.
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU General Public License for more details.
 *  * You should have received a copy of the GNU General Public License
 *  * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *  
 *  Author: Robert Haase, rhaase@mpi-cbg.de
 *          September 2019
 */
public class ParticleAnalyser implements PlugIn, AdjustmentListener, FocusListener {

    ImagePlus imp;
    Vector sliders, values;
    int thr;
    private double min;
    private double max;
    private ImageProcessor ip;

    CLIJx clijx = null;

    @Override
    public void run(String arg) {
        imp = WindowManager.getCurrentImage();


//        int width=imp.getWidth();
//        
//        int height=imp.getHeight();
        int nbSlices = imp.getStackSize();
//        int length=height*width*nbSlices;
//        String title=imp.getTitle();
//
        min = Math.pow(2, imp.getBitDepth());
        max = 0;
//
        for (int i = 1; i <= nbSlices; i++) {
            imp.setSlice(i);
            ip = imp.getProcessor();
            min = Math.min(min, imp.getStatistics().min);
            max = Math.max(max, imp.getStatistics().max);
        }

        imp.setSlice(nbSlices / 2);
        imp.resetDisplayRange();
        ip = imp.getProcessor();
        double thr = ip.getAutoThreshold();
        ip.setThreshold(thr, max, ImageProcessor.RED_LUT);
        imp.updateAndDraw();

        int minSize = (int) Prefs.get("3D-OC_minSize.double", 10);
        int maxSize = imp.getWidth() * imp.getHeight() * nbSlices;
        boolean excludeOnEdges = Prefs.get("3D-OC_excludeOnEdges.boolean", true);
        boolean showObj = Prefs.get("3D-OC_showObj.boolean", true);
        boolean showSurf = Prefs.get("3D-OC_showSurf.boolean", true);
        boolean showCentro = Prefs.get("3D-OC_showCentro.boolean", true);
        boolean showCOM = Prefs.get("3D-OC_showCOM.boolean", true);
        boolean showStat = Prefs.get("3D-OC_showStat.boolean", true);
        boolean showSummary=Prefs.get("3D-OC_summary.boolean", true);

        //showMaskedImg=Prefs.get("3D-OC-Options_showMaskedImg.boolean", true);
        //closeImg=Prefs.get("3D-OC-Options_closeImg.boolean", false);


        //redirectTo=Prefs.get("3D-OC-Options_redirectTo.string", "none");
        //redirect=!this.redirectTo.equals("none") && WindowManager.getImage(this.redirectTo)!=null;

//        if (redirect){
//            ImagePlus imgRedir=WindowManager.getImage(this.redirectTo);
//            if (!(imgRedir.getWidth()==this.width && imgRedir.getHeight()==this.height && imgRedir.getNSlices()==this.nbSlices) || imgRedir.getBitDepth()>16){
//                redirect=false;
//                showMaskedImg=false;
//                IJ.log("Redirection canceled: images should have the same size and a depth of 8- or 16-bits.");
//            }
//            if (imgRedir.getTitle().equals(this.title)){
//                redirect=false;
//                showMaskedImg=false;
//                IJ.log("Redirection canceled: both images have the same title.");
//            }
//        }
//
//        if (!redirect){
//            Prefs.set("3D-OC-Options_redirectTo.string", "none");
//            Prefs.set("3D-OC-Options_showMaskedImg.boolean", false);
//        }

        GenericDialog gd = new GenericDialog("3D Object Counter on GPU (experimental, clij)");

        ArrayList<String> deviceList = CLIJ.getAvailableDeviceNames();
        if (clijx == null) {
            clijx = CLIJx.getInstance();
        }
        String[] deviceArray = new String[deviceList.size()];
        deviceList.toArray(deviceArray);
        gd.addChoice("CL_Device", deviceArray, clijx.getClij().getClearCLContext().getDevice().getName());

        gd.addSlider("Threshold", min, max, thr);
        gd.addSlider("Slice", 1, nbSlices, nbSlices / 2);

        sliders = gd.getSliders();
        ((Scrollbar) sliders.elementAt(0)).addAdjustmentListener(this);
        ((Scrollbar) sliders.elementAt(1)).addAdjustmentListener(this);
        values = gd.getNumericFields();
        ((TextField) values.elementAt(0)).addFocusListener(this);
        ((TextField) values.elementAt(1)).addFocusListener(this);

        gd.addMessage("Size filter: ");
        gd.addNumericField("Min.", minSize, 0);
        gd.addNumericField("Max.", maxSize, 0);
        gd.addCheckbox("Exclude_objects_on_edges", excludeOnEdges);
        gd.addMessage("Maps to show: ");
        gd.addCheckbox("Objects", showObj);
        gd.addCheckbox("Surfaces", showSurf);
        //gd.addCheckbox("Centroids", showCentro);
        //gd.addCheckbox("Centres_of_masses", showCOM);
        gd.addMessage("Results tables to show: ");
        gd.addCheckbox("Statistics", showStat);
        //gd.addCheckbox("Summary", showSummary);

        //if (redirect) gd.addMessage("\nRedirection:\nImage used as a mask: "+this.title+"\nMeasures will be done on: "+this.redirectTo+(showMaskedImg?"\nMasked image will be shown":"")+".");
        //if (closeImg) gd.addMessage("\nCaution:\nImage(s) will be closed during the processing\n(see 3D-OC options to change this setting).");

        gd.showDialog();

        if (gd.wasCanceled()) {
            ip.resetThreshold();
            imp.updateAndDraw();

            return;
        }

        String deviceName = gd.getNextChoice();
        clijx = CLIJx.getInstance(deviceName);

        thr = (int) gd.getNextNumber();
        gd.getNextNumber();
        minSize = (int) gd.getNextNumber();
        maxSize = (int) gd.getNextNumber();
        excludeOnEdges = gd.getNextBoolean();
        showObj = gd.getNextBoolean();
        showSurf=gd.getNextBoolean();
        //showCentro=gd.getNextBoolean();
        //showCOM=gd.getNextBoolean();
        showStat = gd.getNextBoolean();
        //showSummary=gd.getNextBoolean();

        Prefs.set("3D-OC_minSize.double", minSize);
        Prefs.set("3D-OC_excludeOnEdges.boolean", excludeOnEdges);
        Prefs.set("3D-OC_showObj.boolean", showObj);
        //Prefs.set("3D-OC_showSurf.boolean", showSurf);
        //Prefs.set("3D-OC_showCentro.boolean", showCentro);
        //Prefs.set("3D-OC_showCOM.boolean", showCOM);
        Prefs.set("3D-OC_showStat.boolean", showStat);
        //Prefs.set("3D-OC_summary.boolean", showSummary);
        //if (!redirect) Prefs.set("3D-OC-Options_redirectTo.string", "none");

        ip.resetThreshold();
        imp.updateAndDraw();


        ClearCLBuffer buffer = clijx.push(imp);
        ClearCLBuffer flip = clijx.create(buffer.getDimensions(), NativeTypeEnum.Float);
        ClearCLBuffer flop = clijx.create(flip);

        // thresholding
        clijx.threshold(buffer, flip, new Float(thr));
        //clij.show(flip, "thresholded");

        // connected components labelling
        ConnectedComponentsLabeling.connectedComponentsLabeling(clijx, flip, flop);
        //clij.show(flop, "cca");
        System.out.println("cca done");

        // exclude on edges
        if (excludeOnEdges) {
            ExcludeLabelsOnEdges.excludeLabelsOnEdges(clijx, flop, flip);
            clijx.copy(flip, flop);
            //clij.show(flop, "excl");
        }
        System.out.println("excl");

        // filter by size
        System.out.println("minSize " + minSize);
        System.out.println("maxSize " + maxSize);

        int numberOfObjects = (int) clijx.maximumOfAllPixels(flop);
        System.out.println("numberOfObjects " + numberOfObjects);
        ClearCLBuffer flap = clijx.create(new long[]{numberOfObjects + 1, 1, 1}, NativeTypeEnum.Float);
        clijx.fillHistogram(flop, flap, 0f, new Float(numberOfObjects));


        System.out.println("hist done");

        float[] indexList = new float[numberOfObjects + 1];

        flap.writeTo(FloatBuffer.wrap(indexList), true);

        if (minSize > 1 || maxSize < imp.getWidth() * imp.getHeight() * nbSlices) {
            int count = 1;
            indexList[0] = 0; // background stays background
            //clij.show(flap, "hist");
            for (int i = 1; i < indexList.length; i++) {

                if (indexList[i] >= minSize && indexList[i] <= maxSize) {
                    indexList[i] = count;
                    count++;
                } else {
                    indexList[i] = 0;
                }
            }
            System.out.println("Count: " + count);
            numberOfObjects = count;

            flap.readFrom(FloatBuffer.wrap(indexList), true);

            ReplaceIntensities.replaceIntensities(clijx, flop, flap, flip);
            clijx.copy(flip, flop);

            //clij.show(flop, "filtered by size");

            // refill histogram
            //flap.close();
            //flap = clij.create(new long[]{count, 1, 1}, NativeTypeEnum.Float);
            //clij.op().fillHistogram(flop, flap, 0f, new Float(count));
        }

        /*
        Counter3D OC = new Counter3D(imp, (int)thr, minSize, maxSize, excludeOnEdges, false); // false -> redirect
        OC.setObjects(
                convertImagePlusToIntArray(clij.pull(flop)),
                convertImagePlusToIntArray(clij.pull(flap))
        );

        int dotSize=(int) Prefs.get("3D-OC-Options_dotSize.double", 5);
        int fontSize=(int) Prefs.get("3D-OC-Options_fontSize.double", 10);
        boolean showNb=Prefs.get("3D-OC-Options_showNb.boolean", true);
        boolean whiteNb=Prefs.get("3D-OC-Options_whiteNb.boolean", true);

        if (showObj){OC.getObjMap(showNb, fontSize).show(); IJ.run("Fire");}

        // 3D edge detection + dilation?
        if (showSurf){OC.getSurfPixMap(showNb, whiteNb, fontSize).show(); IJ.run("Fire");}

        // filled 2D circles with diameter dotSize
        if (showCentro){OC.getCentroidMap(showNb, whiteNb, dotSize, fontSize).show(); IJ.run("Fire");}
        if (showCOM){OC.getCentreOfMassMap(showNb, whiteNb, dotSize, fontSize).show(); IJ.run("Fire");}
//
        boolean newRT = Prefs.get("3D-OC-Options_newRT.boolean", true);

//
        if (showStat) OC.showStatistics(newRT);
//
        if (showSummary) OC.showSummary();
        */

        boolean newRT = Prefs.get("3D-OC-Options_newRT.boolean", true);

        if (showObj) {
            //clij.show(flop, );
            ImagePlus img = clijx.pull(flop);
            img.setTitle("Objects map of " + imp.getTitle() + " (experimental, clij)");
            show(img, 0, numberOfObjects);

        }

        if (showSurf) {
            clijx.greaterOrEqualConstant(flop, flip, 1f);
            ClearCLBuffer temp1 = clijx.create(flop);
            clijx.binaryEdgeDetection(flip, temp1);
            clijx.multiplyImages(flop, temp1, flip);

            ImagePlus img = clijx.pull(flip);
            img.setTitle("Surface map of " + imp.getTitle() + " (experimental, clij)");
            show(img, 0, numberOfObjects);

            temp1.close();
        }

        // statistics
        if (showStat) {

            ResultsTable table = newRT ? new ResultsTable() : ResultsTable.getResultsTable();
            if (!newRT) {
                table.reset();
            }
            StatisticsOfLabelledPixels.statisticsOfLabelledPixels(clijx, buffer, flop, table);

            if (showStat) {
                if (newRT) {
                    table.show("Statistics for " + imp.getTitle() + " (experimental, clij)");
                } else {
                    table.show("Results");
                }
            }
        }

        // cleanup
        buffer.close();
        flip.close();
        flop.close();
        flap.close();

    }

    private void show(ImagePlus img, int min, int max) {
        img.setCalibration(imp.getCalibration());
        img.setDisplayRange(min, max);

        img.show();
        while (img.getWindow().getWidth() > imp.getWindow().getWidth()) {
            IJ.run(img, "Out [-]", "");
        }
        //IJ.run(img,"Out [-]", "");
        //IJ.run(img,"Out [-]", "");
        img.getWindow().setLocation(imp.getWindow().getX() + 50, imp.getWindow().getY() + 50);

        IJ.run(img,"Fire", "");
    }


    public void adjustmentValueChanged(AdjustmentEvent e) {
        updateImg();
    }

    public void focusLost(FocusEvent e) {
        if (e.getSource().equals(values.elementAt(0))) {
            int val = (int) Tools.parseDouble(((TextField) values.elementAt(0)).getText());
            val = (int) Math.min(max, Math.max(min, val));
            ((TextField) values.elementAt(0)).setText("" + val);
        }

        if (e.getSource().equals(values.elementAt(1))) {
            int val = (int) Tools.parseDouble(((TextField) values.elementAt(1)).getText());
            val = (int) Math.min(max, Math.max(min, val));
            ((TextField) values.elementAt(1)).setText("" + val);
        }

        updateImg();
    }

    public void focusGained(FocusEvent e) {
    }

    private void updateImg() {
        thr = ((Scrollbar) sliders.elementAt(0)).getValue();
        imp.setSlice(((Scrollbar) sliders.elementAt(1)).getValue());
        imp.resetDisplayRange();
        ip.setThreshold(thr, max, ImageProcessor.RED_LUT);
    }

    private int[] convertImagePlusToIntArray(ImagePlus img) {
        int nbSlices = img.getNSlices();
        int width = img.getWidth();
        int height = img.getHeight();

        int[] imgArray = new int[nbSlices * width * height];
        int index = 0;
        for(int i = 1; i<=nbSlices;i++) {
            img.setSlice(i);
            for (int j = 0; j < height; j++) {
                for (int k = 0; k < width; k++) {
                    imgArray[index] = img.getProcessor().getPixel(k, j);
                    index++;
                }
            }
        }
        return imgArray;
    }
}
