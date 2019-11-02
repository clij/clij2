package net.haesleinhuepf.clijx.piv.visualisation;


import ij.ImagePlus;
import ij.plugin.Concatenator;
import ij.plugin.Duplicator;
import org.scijava.command.Command;
import org.scijava.plugin.Plugin;

@Plugin(type = Command.class, menuPath = "Plugins>ImageJ on GPU (CLIJ)>Measure>PIV>Visualise vector field on a timelapse (experimental)")
public class VisualiseVectorFieldOnTimelapsePlugin extends VisualiseVectorFieldsPlugin{

    @Override
    public void run() {
        if (!showDialog()) {
            return;
        }

        ImagePlus[] resultSlices = new ImagePlus[inputImage.getNSlices()];
        for (int t = 0; t < inputImage.getNSlices(); t++) {
            System.out.println("PIV " + t + "/" + inputImage.getNSlices());
            ImagePlus inputSlice = new Duplicator().run(inputImage, t+1, t+1);
            inputSlice.setDisplayRange(inputImage.getDisplayRangeMin(), inputImage.getDisplayRangeMax());
            //inputSlice.updateAndDraw();
            //inputSlice.show();
            ImagePlus vectorXSlice = new Duplicator().run(vectorXImage, t+1, t+1);
            ImagePlus vectorYSlice = new Duplicator().run(vectorYImage, t+1, t+1);

            VisualiseVectorFieldsPlugin vvfp = new VisualiseVectorFieldsPlugin();
            vvfp.setSilent(true);
            vvfp.setShowResult(false);
            vvfp.setInputImage(inputSlice);
            vvfp.setVectorXImage(vectorXSlice);
            vvfp.setVectorYImage(vectorYSlice);
            vvfp.setLineWidth(lineWidth);
            vvfp.setLookupTable(lookupTable);
            vvfp.setMinimumLength(minimumLength);
            vvfp.setMaximumLength(maximumLength);
            vvfp.setStepSize(stepSize);
            vvfp.run();
            resultSlices[t] = vvfp.getOutputImage();
            //resultSlices[t].show();
        }

        outputImage = Concatenator.run(resultSlices);
        if (showResult) {
            outputImage.show();
        }
    }
}
