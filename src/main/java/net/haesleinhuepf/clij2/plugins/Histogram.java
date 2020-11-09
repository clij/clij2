package net.haesleinhuepf.clij2.plugins;

import ij.ImagePlus;
import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.HasAuthor;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.HasLicense;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;

/**
 *
 *
 * Author: @haesleinhuepf
 *         December 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_histogram")
public class Histogram extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, HasLicense, HasAuthor, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Image";
    }

    @Override
    public String getOutputType() {
        return "Vector";
    }

    @Override
    public String getCategories() {
        return "Measurements";
    }

    @Override
    public boolean executeCL() {
        ClearCLBuffer src = (ClearCLBuffer) (args[0]);
        ClearCLBuffer histogram = (ClearCLBuffer) (args[1]);
        Integer numberOfBins = asInteger(args[2]);
        Float minimumGreyValue = asFloat(args[3]);
        Float maximumGreyValue = asFloat(args[4]);
        Boolean determineMinMax = asBoolean(args[5]);
        return getCLIJ2().histogram(src, histogram, numberOfBins, minimumGreyValue, maximumGreyValue, determineMinMax, false);
    }

    public static float[] histogram(CLIJ2 clij2, ClearCLBuffer image, Float minGreyValue, Float maxGreyValue, Integer numberOfBins) {
        ClearCLBuffer histogram = clij2.create(new long[]{numberOfBins, 1, 1}, NativeTypeEnum.Float);

        if (minGreyValue == null) {
            minGreyValue = new Double(clij2.minimumOfAllPixels(image)).floatValue();
        }
        if (maxGreyValue == null) {
            maxGreyValue = new Double(clij2.maximumOfAllPixels(image)).floatValue();
        }

        fillHistogram(clij2, image, histogram, minGreyValue, maxGreyValue);

        ImagePlus histogramImp = clij2.convert(histogram, ImagePlus.class);
        histogram.close();

        float[] determinedHistogram = (float[])(histogramImp.getProcessor().getPixels());
        return determinedHistogram;
    }

    public static boolean histogram(CLIJ2 clij2, ClearCLBuffer src, ClearCLBuffer histogram) {
        return histogram(clij2, src, histogram, 256, 0f, 0f, true);
    }

    public static ClearCLBuffer histogram(CLIJ2 clij2, ClearCLBuffer src) {
        ClearCLBuffer histogram = clij2.create(new long[]{256, 1, 1}, clij2.Float);
        histogram(clij2, src, histogram, (int)histogram.getWidth(), 0f, 0f, true);
        return histogram;
    }

    public static boolean histogram(CLIJ2 clij2, ClearCLBuffer src, ClearCLBuffer histogram, Integer numberOfBins, Float minimumGreyValue, Float maximumGreyValue, Boolean determineMinMax) {
        return histogram(clij2, src, histogram, numberOfBins, minimumGreyValue, maximumGreyValue, determineMinMax, false);
    }

    public static boolean histogram(CLIJ2 clij2, ClearCLBuffer src, ClearCLBuffer histogram, Integer numberOfBins, Float minimumGreyValue, Float maximumGreyValue, Boolean determineMinMax, boolean showTable) {

        // determine min and max intensity if necessary
        if (determineMinMax) {
            minimumGreyValue = new Double(clij2.minimumOfAllPixels(src)).floatValue();
            maximumGreyValue = new Double(clij2.maximumOfAllPixels(src)).floatValue();
        }

        // determine histogram
        boolean result = fillHistogram(clij2, src, histogram, minimumGreyValue, maximumGreyValue);

        // the histogram is written in args[1] which is supposed to be a one-dimensional image
        ImagePlus histogramImp = clij2.convert(histogram, ImagePlus.class);

        // plot without first eleement
        //histogramImp.setRoi(new Line(1,0.5, histogramImp.getWidth(), 0.5));
        //IJ.run(histogramImp, "Plot Profile", "");

        // plot properly
        float[] determinedHistogram = (float[])(histogramImp.getProcessor().getPixels());
        float[] xAxis = new float[numberOfBins];
        xAxis[0] = minimumGreyValue;
        float step = (maximumGreyValue - minimumGreyValue) / (numberOfBins - 1);

        for (int i = 1 ; i < xAxis.length; i ++) {
            xAxis[i] = xAxis[i-1] + step;
        }
        //new Plot("Histogram", "grey value", "log(number of pixels)", xAxis, determinedHistogram, 0).show();

        // send result to results table
        if (showTable) {
            ResultsTable table = ResultsTable.getResultsTable();
            for (int i = 0; i < xAxis.length; i++) {
                table.incrementCounter();
                table.addValue("Grey value", xAxis[i]);
                table.addValue("Number of pixels", determinedHistogram[i]);
            }
            table.show(table.getTitle());
        }
        return result;
    }

    /**
     * use histogram instead
     */
    @Deprecated
    public static boolean fillHistogram(CLIJ2 clij2, ClearCLBuffer src, ClearCLBuffer dstHistogram, Float minimumGreyValue, Float maximumGreyValue) {
        assertDifferent(src, dstHistogram);

        int stepSizeX = 1;
        int stepSizeY = 1;
        int stepSizeZ = 1;

        long[] globalSizes = new long[]{src.getHeight() / stepSizeZ, 1, 1};

        long numberOfPartialHistograms = globalSizes[0] * globalSizes[1] * globalSizes[2];
        long[] histogramBufferSize = new long[]{dstHistogram.getWidth(), 1, numberOfPartialHistograms};

        long timeStamp = System.currentTimeMillis();

        // allocate memory for partial histograms
        ClearCLBuffer  partialHistograms = clij2.create(histogramBufferSize, dstHistogram.getNativeType());

        //
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst_histogram", partialHistograms);
        parameters.put("minimum", minimumGreyValue);
        parameters.put("maximum", maximumGreyValue);
        parameters.put("step_size_x", stepSizeX);
        parameters.put("step_size_y", stepSizeY);
        if (src.getDimension() > 2) {
            parameters.put("step_size_z", stepSizeZ);
        }

        HashMap<String, Object> constants = new HashMap<>();
        constants.put("NUMBER_OF_HISTOGRAM_BINS", dstHistogram.getWidth());

        clij2.execute(Histogram.class,
                "histogram_" + src.getDimension() + "d_x.cl",
                "histogram_" + src.getDimension() + "d",
                dstHistogram.getDimensions(),
                globalSizes,
                parameters,
                constants);

        clij2.sumZProjection(partialHistograms, dstHistogram);
        //IJ.log("Histogram generation took " + (System.currentTimeMillis() - timeStamp) + " msec");

        partialHistograms.close();
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Image destination, Number number_of_bins, Number minimum_intensity, Number maximum_intensity, Boolean determine_min_max";
    }

    @Override
    public String getDescription() {
        return "Determines the histogram of a given image.\n\n" +
                "The histogram image is of dimensions number_of_bins/1/1; a 3D image with height=1 and depth=1. \n" +
                "Histogram bins contain the number of pixels with intensity in this corresponding bin. \n" +
                "The histogram bins are uniformly distributed between given minimum and maximum grey value intensity. \n" +
                "If the flag determine_min_max is set, minimum and maximum intensity will be determined. \n" +
                "When calling this operation many times, it is recommended to determine minimum and maximum intensity \n" +
                "once at the beginning and handing over these values.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        Integer numberOfBins = asInteger(args[2]);

        return clij.createCLBuffer(new long[]{numberOfBins,1,1},NativeTypeEnum.Float);
    }

    @Override
    public String getAuthorName() {
        return "Robert Haase adapted work from Aaftab Munshi, Benedict Gaster, Timothy Mattson, James Fung, Dan Ginsburg";
    }

    @Override
    public String getLicense() {
        return "// adapted code from\n" +
                "// https://github.com/bgaster/opencl-book-samples/blob/master/src/Chapter_14/histogram/histogram_image.cl\n" +
                "//\n" +
                "// It was published unter BSD license according to\n" +
                "// https://code.google.com/archive/p/opencl-book-samples/\n" +
                "//\n" +
                "// Book:      OpenCL(R) Programming Guide\n" +
                "// Authors:   Aaftab Munshi, Benedict Gaster, Timothy Mattson, James Fung, Dan Ginsburg\n" +
                "// ISBN-10:   0-321-74964-2\n" +
                "// ISBN-13:   978-0-321-74964-2\n" +
                "// Publisher: Addison-Wesley Professional\n" +
                "// URLs:      http://safari.informit.com/9780132488006/\n" +
                "//            http://www.openclprogrammingguide.com";
    }
}