package net.haesleinhuepf.clij2.plugins;


import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_equalizeMeanIntensitiesOfSlices")
public class EqualizeMeanIntensitiesOfSlices extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized {
    @Override
    public String getCategories() {
        return "Math";
    }


    @Override
    public Object[] getDefaultValues() {
        return new Object[]{null, null, 0};
    }

    @Override
    public String getParameterHelpText() {
        return "Image input, ByRef Image destination, Number referenceSlice";
    }

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();
        boolean result = getCLIJ2().equalizeMeanIntensitiesOfSlices((ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]), asInteger(args[2]));
        releaseBuffers(args);
        return result;
    }

    public static boolean equalizeMeanIntensitiesOfSlices(CLIJ2 clij2, ClearCLBuffer input, ClearCLBuffer output, Integer referenceSlice) {

        ClearCLBuffer temp1 = clij2.create(new long[]{input.getDepth(), input.getHeight()});
        clij2.sumXProjection(input, temp1);

        ClearCLBuffer temp2 = clij2.create(new long[]{input.getDepth(), 1});
        clij2.sumYProjection(temp1, temp2);
        clij2.release(temp1);

        ClearCLBuffer slice = clij2.create(new long[]{input.getWidth(), input.getHeight()});
        clij2.copySlice(input, slice, referenceSlice);
        double reference_value = clij2.sumOfAllPixels(slice);
        //System.out.println("reference" + reference_value);
        slice.close();

        ClearCLBuffer temp3 = clij2.create(temp2);
        clij2.multiplyImageAndScalar(temp2, temp3, 1.0 / reference_value);

        //clij2.print(temp3);

        //double[] intensities = clij2.sumImageSliceBySlice(input);
        //float[] factors = new float[intensities.length];


        /*
        System.out.println("Reference slice: " + referenceSlice);

        for (int i = 0; i < factors.length; i++ ) {
            System.out.println("i: " + (i));
            System.out.println("intensity: " + (intensities[i]));
            System.out.println("Factor: " + ((intensities[referenceSlice] / intensities[i])) );
            System.out.println("Factor as float: " + ((float)(intensities[referenceSlice] / intensities[i])) );
            factors[i] = (float)(intensities[referenceSlice] / intensities[i]);
        }*/
        clij2.multiplyImageStackWithScalars(input, output, temp3);
        return true;
    }

    @Override
    public String getDescription() {
        return "Determines correction factors for each z-slice so that the average intensity in all slices can be made " +
                "the same and multiplies these factors with the slices. \n\n" +
                "This functionality is similar to the 'Simple Ratio Bleaching Correction' in Fiji.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }

    public static void main(String... arg ) {
        new ImageJ();


        ImagePlus imp = IJ.openImage("D:/structure/data/Alexa/crops/200114_T2019007_GS_CS22_CROP-2.tif");
        System.out.println("A");

        CLIJ2 clij2 = CLIJ2.getInstance();
        ClearCLBuffer input = clij2.push(imp);
        ClearCLBuffer bit32 = clij2.create(input.getDimensions(), NativeTypeEnum.Float);
        ClearCLBuffer output = clij2.create(bit32);
        System.out.println("B");

        clij2.copy(input, bit32);

        clij2.equalizeMeanIntensitiesOfSlices(bit32, output, 150);
        System.out.println("C");

        clij2.show(input, "output");
        clij2.show(output, "output");
        System.out.println("D");




    }
}
