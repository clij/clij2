package net.haesleinhuepf.clijx.plugins.tenengradfusion;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;

import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;

/**
 * Author: @haesleinhuepf
 * June 2019
 */
public abstract class AbstractTenengradFusion extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        float[] sigmas = new float[3];
        sigmas[0] = asFloat(args[args.length - 3]);
        sigmas[1] = asFloat(args[args.length - 2]);
        sigmas[2] = asFloat(args[args.length - 1]);

        ClearCLBuffer output = (ClearCLBuffer)args[args.length - 4];

        ClearCLBuffer[] input = new ClearCLBuffer[args.length - 4];
        for (int i = 0; i < args.length - 4; i++) {
            input[i] = (ClearCLBuffer) args[i];
        }

        CLIJx clijx = getCLIJx();

        tenengradFusion(clijx, output, sigmas, 1.0f, input);

        //clijx.copy(output, (ClearCLBuffer) this.args[args.length - 4]);
        //output.close();
        for (int i = 0; i < args.length - 4; i++) {
            input[i].close();
        }
        return true;
    }

    public static boolean tenengradFusion(CLIJx clijx, ClearCLBuffer clImageOut, float[] blurSigmas, float exponent, ClearCLBuffer... clImagesIn) {
        for (int i = 0; i < clImagesIn.length; i++) {
            assertDifferent(clImagesIn[i], clImageOut);
        }
        if (clImagesIn.length > 12) {
            throw new IllegalArgumentException("Error: tenengradFusion does not support more than 12 stacks.");
        }
        if (clImagesIn.length == 1) {
            return clijx.copy(clImagesIn[0], clImageOut);
        }
        if (clImagesIn.length == 0) {
            throw new IllegalArgumentException("Error: tenengradFusion didn't get any output images.");
        }
        if (clImagesIn[0].getNativeType() != clijx.Float) {
            System.out.println("Warning: tenengradFusion may only work on float images!");
        }

        HashMap<String, Object> lFusionParameters = new HashMap<>();

        ClearCLBuffer temporaryImage = clijx.create(clImagesIn[0]);
        ClearCLBuffer temporaryImage2 = null;
        if (Math.abs(exponent - 1.0f) > 0.0001) {
            temporaryImage2 = clijx.create(clImagesIn[0]);
        }

        ClearCLBuffer[] temporaryImages = new ClearCLBuffer[clImagesIn.length];
        for (int i = 0; i < clImagesIn.length; i++) {
            HashMap<String, Object> parameters = new HashMap<>();
            temporaryImages[i] = clijx.create(clImagesIn[i]);
            parameters.put("src", clImagesIn[i]);
            parameters.put("dst", temporaryImage);

            clijx.execute(AbstractTenengradFusion.class, "tenengrad_weight_unnormalized_x.cl", "tenengrad_weight_unnormalized", temporaryImage.getDimensions(), temporaryImage.getDimensions(), parameters);

            if (temporaryImage2 != null) {
                clijx.power(temporaryImage, temporaryImage2, exponent);
                clijx.gaussianBlur(temporaryImage2, temporaryImages[i], blurSigmas[0], blurSigmas[1], blurSigmas[2]);
            } else {
                clijx.gaussianBlur(temporaryImage, temporaryImages[i], blurSigmas[0], blurSigmas[1], blurSigmas[2]);
            }

            lFusionParameters.put("src" + i, clImagesIn[i]);
            lFusionParameters.put("weight" + i, temporaryImages[i]);
        }

        lFusionParameters.put("dst", clImageOut);
        lFusionParameters.put("factor", (int) (clImagesIn[0].getWidth() / temporaryImages[0].getWidth()));

        clijx.execute(AbstractTenengradFusion.class, "tenengrad_fusion_of_" + clImagesIn.length + "_x.cl", String.format("tenengrad_fusion_with_provided_weights_%d_images", clImagesIn.length), clImageOut.getDimensions(), clImageOut.getDimensions(), lFusionParameters);

        temporaryImage.close();
        for (int i = 0; i < temporaryImages.length; i++) {
            temporaryImages[i].close();
        }

        if (temporaryImage2 != null) {
            temporaryImage2.close();
        }

        return true;
    }


    protected String getParameterHelpText(int num_images) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < num_images; i++) {
            result.append("Image source" + (i + 1) +", ");
        }
        result.append(" Image destination, Number sigmaX, Number sigmaY, Number sigmaZ");
        return result.toString();
    }

    @Override
    public String getDescription() {
        return "Fuses #n# image stacks using Tenengrads algorithm.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }

}
