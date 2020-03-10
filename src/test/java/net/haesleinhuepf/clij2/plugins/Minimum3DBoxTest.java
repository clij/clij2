package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import ij.IJ;
import ij.ImagePlus;
import ij.gui.Roi;
import ij.plugin.Duplicator;
import net.haesleinhuepf.clij.test.TestUtilities;
import net.haesleinhuepf.clijx.CLIJx;
import org.junit.Test;

import static org.junit.Assert.*;

public class Minimum3DBoxTest {
  //  @Ignore //ignore test as we know and need to accept that the tested method does not do the same its ImageJ counterpart
    @Test
    public void minimum3dSeparable() throws InterruptedException {
        CLIJx clijx = CLIJx.getInstance();

        ImagePlus testFlyBrain3D = IJ.openImage("src/test/resources/flybrain.tif");

        ClearCLImage src = clijx.convert(testFlyBrain3D, ClearCLImage.class);;
        ClearCLImage minimumCL = clijx.create(src);
        ClearCLImage minimumSepCL = clijx.create(src);

        clijx.minimum3DSphere( src, minimumCL, 7,7, 7);
        clijx.minimum3DBox( src, minimumSepCL, 3, 3, 3);

        ImagePlus minimumImp = clijx.convert(minimumCL, ImagePlus.class);
        ImagePlus minimumSepImp = clijx.convert(minimumSepCL, ImagePlus.class);

        // ignore edges
        minimumImp.setRoi(new Roi(1, 1, minimumImp.getWidth() - 2, minimumImp.getHeight() - 2));
        minimumSepImp.setRoi(new Roi(1, 1, minimumSepImp.getWidth() - 2, minimumSepImp.getHeight() - 2));
        minimumImp = new Duplicator().run(minimumImp);
        minimumSepImp = new Duplicator().run(minimumSepImp);

        src.close();
        minimumCL.close();
        minimumSepCL.close();

        System.out.println("Todo: minimumSphere uses a elipsoid as mask while minimumBox uses a cuboid");
        assertTrue(TestUtilities.compareImages(minimumSepImp, minimumImp, 2.0));
        IJ.exit();
        clijx.clear();
    }

 //   @Ignore //ignore test as we know and need to accept that the tested method does not do the same its ImageJ counterpart
    @Test
    public void minimum3dSeparable_Buffer() throws InterruptedException {
        CLIJx clijx = CLIJx.getInstance();


        ImagePlus testFlyBrain3D = IJ.openImage("src/test/resources/flybrain.tif");

        ClearCLBuffer src = clijx.convert(testFlyBrain3D, ClearCLBuffer.class);
        ClearCLBuffer minimumCL = clijx.create(src);
        ClearCLBuffer minimumSepCL = clijx.create(src);

        clijx.minimum3DSphere( src, minimumCL, 3,3, 3);
        clijx.minimum3DBox( src, minimumSepCL, 1, 1, 1);

        ImagePlus minimumImp = clijx.convert(minimumCL, ImagePlus.class);
        ImagePlus minimumSepImp = clijx.convert(minimumSepCL, ImagePlus.class);


        // ignore edges
        minimumImp.setRoi(new Roi(1, 1, minimumImp.getWidth() - 2, minimumImp.getHeight() - 2));
        minimumSepImp.setRoi(new Roi(1, 1, minimumSepImp.getWidth() - 2, minimumSepImp.getHeight() - 2));
        minimumImp = new Duplicator().run(minimumImp);
        minimumSepImp = new Duplicator().run(minimumSepImp);

        src.close();
        minimumCL.close();
        minimumSepCL.close();

        System.out.println("Todo: minimumSphere uses a elipsoid as mask while minimumBox uses a cuboid");
        assertTrue(TestUtilities.compareImages(minimumSepImp, minimumImp, 2.0));
        IJ.exit();
        clijx.clear();
    }

}