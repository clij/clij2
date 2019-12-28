package net.haesleinhuepf.clijx.temp;

import ij.ImagePlus;
import ij.gui.NewImage;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.exceptions.OpenCLException;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPluginService;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.modules.Mean3DBox;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.Context;

public class CLIJ1CLIJ2Benchmarking {
    public static void main(String ... arg) {
        CLIJ clij = CLIJ.getInstance();

        CLIJx clijx = CLIJx.getInstance();

        System.out.println("CLIJ: " + clij.getGPUName() + " " + clij.getClearCLContext().toString());
        System.out.println("CLIJx: " + clijx.getGPUName() + " " + clijx.getClij().getClearCLContext().toString());

        ImagePlus random = NewImage.createFloatImage("rand", 1024, 1024, 50, NewImage.FILL_RANDOM);
        ClearCLBuffer input = clij.push(random);
        ClearCLBuffer output = clij.create(input);
        ClearCLBuffer output2 = clij.create(input);

        Object[] b2f3 = {input, output, new Float(3), new Float(3), new Float(3)};
        Object[] b2f2 = {input, output, new Float(3), new Float(3), new Float(3)};
        Object[] b3f3 = {input, output, output2, new Float(3), new Float(3), new Float(3)};
        Object[] b3f2 = {input, output, output2, new Float(3), new Float(3), new Float(3)};

        CLIJMacroPluginService service = new Context(CLIJMacroPluginService.class).getService(CLIJMacroPluginService.class);
        for (String pluginName : service.getCLIJMethodNames()) {
            if (pluginName.startsWith("CLIJ_")) {
                CLIJMacroPlugin clijPlugin = service.getCLIJMacroPlugin(pluginName);
                CLIJMacroPlugin clijxPlugin = service.getCLIJMacroPlugin(pluginName.replace("CLIJ_", "CLIJx_"));

                if (clijPlugin != null && clijxPlugin != null) {
                    System.out.println(clijPlugin + " <=> " + clijxPlugin);
                } else if (clijxPlugin == null) {
                    System.out.println("Error: No successor found for " + pluginName);
                }
            }
        }

        if (true) return;


        for (int i = 0; i < 10; i++) {

            //long time = System.currentTimeMillis();
            //clij.op().meanBox(input, output, 3, 3, 3);
            //System.out.println("CLIJ mean took " + (System.currentTimeMillis() - time) + " ms");

            long duration = benchmarkOp(clij, new Mean3DBox(), b2f3);
            System.out.println("CLIJ mean took " + (duration) + " ms");
        }


        for (int i = 0; i < 10; i++) {
            //long time = System.currentTimeMillis();
            //clijx.meanBox(input, output, 3, 3, 3);
            //System.out.println("CLIJx mean took " + (System.currentTimeMillis() - time) + " ms");
            long duration = benchmarkOp(clij, new net.haesleinhuepf.clijx.advancedfilters.Mean3DBox(), b2f3);
            System.out.println("CLIJx mean took " + (duration) + " ms");

        }
    }

    private static long benchmarkOp(CLIJ clij, Object op, Object[] args) {
        if (op instanceof AbstractCLIJPlugin) {
            ((AbstractCLIJPlugin) op).setClij(clij);
            ((AbstractCLIJPlugin) op).setArgs(args);
        }
        if (op instanceof CLIJOpenCLProcessor) {
            long time = System.currentTimeMillis();
            ((CLIJOpenCLProcessor) op).executeCL();
            return (System.currentTimeMillis() - time);
        }
        return -1;
    }
}
