package net.haesleinhuepf.clijx.matrix;


import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij.test.TestUtilities;
import net.haesleinhuepf.clij2.utilities.HasAuthor;
import net.haesleinhuepf.clij2.utilities.HasLicense;
import org.scijava.plugin.Plugin;

import java.util.HashMap;


@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_gaussJordan")
public class GaussJordan extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, HasAuthor, HasLicense {

    @Override
    public String getParameterHelpText() {
        return "Image A_matrix, Image B_result_vector, Image solution_destination";
    }

    @Override
    public boolean executeCL() {
        boolean result = gaussJordan(clij, (ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]),  (ClearCLBuffer) (args[2]));
        return result;
    }

    public static boolean gaussJordan(CLIJ clij, ClearCLBuffer matrix, ClearCLBuffer resultBuffer, ClearCLBuffer coefficients_destination) {


        HashMap<String, Object> parameters = new HashMap<>();
        int size = (int) matrix.getWidth();

        ClearCLBuffer temp = clij.create(matrix);
        clij.op().set(temp, 0f);

        for (int t = 0; t < (size-1); t++) {
            System.out.println("mat");
            TestUtilities.printBuffer(CLIJ.getInstance(), matrix);
            System.out.println("res");
            TestUtilities.printBuffer(CLIJ.getInstance(), resultBuffer);
            System.out.println("coe");
            TestUtilities.printBuffer(CLIJ.getInstance(), coefficients_destination);

            parameters.clear();
            parameters.put("dst_m_dev", temp);
            parameters.put("src_a_dev", matrix);
            parameters.put("size", new Integer(size));
            parameters.put("t", new Integer(t));

            clij.execute(MultiplyMatrix.class, "gaussJordan.cl", "Fan1", new long[]{size}, parameters);
            //System.out.println("tem");
            //TestUtilities.printBuffer(CLIJ.getInstance(), temp);

            parameters.clear();
            parameters.put("src_m_dev", temp);
            parameters.put("src_a_dev", matrix);
            parameters.put("dst_b_dev", resultBuffer);
            parameters.put("size", new Integer(size));
            parameters.put("t", new Integer(t));
            clij.execute(MultiplyMatrix.class, "gaussJordan.cl", "Fan2", new long[]{size, size}, parameters);


            //System.out.println("mat");
            //TestUtilities.printBuffer(CLIJ.getInstance(), matrix);
            //System.out.println("res");
            //TestUtilities.printBuffer(CLIJ.getInstance(), resultBuffer);
            //System.out.println("tem");
            //TestUtilities.printBuffer(CLIJ.getInstance(), temp);
        }

        parameters.clear();
        parameters.put("src_a_dev", matrix);
        parameters.put("src_b_dev", resultBuffer);
        parameters.put("dst_finalVec", coefficients_destination);
        parameters.put("size", new Integer(size));
        clij.execute(MultiplyMatrix.class, "gaussJordan.cl", "BackSub", new long[]{1,1,1}, parameters);
        return true;
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        return clij.create((ClearCLBuffer)args[1]);
    }

    @Override
    public String getDescription() {
        return "Gauss Jordan elimination algorithm for solving linear equation systems. " +
                "Ent the equation coefficients as an n*n sized image A and an n*1 sized image B:" +
                "\n" +
                "<pre>" +
                "a(1,1)*x + a(2,1)*y + a(3,1)+z = b(1)\n" +
                "a(2,1)*x + a(2,2)*y + a(3,2)+z = b(2)\n" +
                "a(3,1)*x + a(3,2)*y + a(3,3)+z = b(3)\n" +
                "</pre>\n" +
                "The results will then be given in an n*1 image with values [x, y, z].\n" +
                "\n" +
                "Adapted from: \n" +
                "https://github.com/qbunia/rodinia/blob/master/opencl/gaussian/gaussianElim_kernels.cl\n" +
                "L.G. Szafaryn, K. Skadron and J. Saucerman. \"Experiences Accelerating MATLAB Systems\n" +
                "//Biology Applications.\" in Workshop on Biomedicine in Computing (BiC) at the International\n" +
                "//Symposium on Computer Architecture (ISCA), June 2009.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }

    @Override
    public String getAuthorName() {
        return "Robert Haase with code from (Shuai Che: sc5nf@cs.virginia.edu\n" +
                "and Kevin Skadron: skadron@cs.virginia.edu)";
    }

    @Override
    public String getLicense() {
        return "/LICENSE TERMS\n" +
                "//\n" +
                "//Copyright (c)2008-2011 University of Virginia\n" +
                "//All rights reserved.\n" +
                "//\n" +
                "//Redistribution and use in source and binary forms, with or without modification, are permitted without royalty fees or other restrictions, provided that the following conditions are met:\n" +
                "//\n" +
                "//* Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.\n" +
                "//* Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.\n" +
                "//* Neither the name of the University of Virginia, the Dept. of Computer Science, nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.\n" +
                "//\n" +
                "//THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS \"AS IS\" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE UNIVERSITY OF VIRGINIA OR THE SOFTWARE AUTHORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.\n" +
                "//\n" +
                "//If you use this software or a modified version of it, please cite the most relevant among the following papers:\n" +
                "//\n" +
                "//- M. A. Goodrum, M. J. Trotter, A. Aksel, S. T. Acton, and K. Skadron. Parallelization of Particle Filter Algorithms. In Proceedings\n" +
                "//of the 3rd Workshop on Emerging Applications and Many-core Architecture (EAMA), in conjunction with the IEEE/ACM International\n" +
                "//Symposium on Computer Architecture (ISCA), June 2010.\n" +
                "//\n" +
                "//- S. Che, M. Boyer, J. Meng, D. Tarjan, J. W. Sheaffer, Sang-Ha Lee and K. Skadron.\n" +
                "//\"Rodinia: A Benchmark Suite for Heterogeneous Computing\". IEEE International Symposium\n" +
                "//on Workload Characterization, Oct 2009.\n" +
                "//\n" +
                "//- J. Meng and K. Skadron. \"Performance Modeling and Automatic Ghost Zone Optimization\n" +
                "//for Iterative Stencil Loops on GPUs.\" In Proceedings of the 23rd Annual ACM International\n" +
                "//Conference on Supercomputing (ICS), June 2009.\n" +
                "//\n" +
                "//- L.G. Szafaryn, K. Skadron and J. Saucerman. \"Experiences Accelerating MATLAB Systems\n" +
                "//Biology Applications.\" in Workshop on Biomedicine in Computing (BiC) at the International\n" +
                "//Symposium on Computer Architecture (ISCA), June 2009.\n" +
                "//\n" +
                "//- M. Boyer, D. Tarjan, S. T. Acton, and K. Skadron. \"Accelerating Leukocyte Tracking using CUDA:\n" +
                "//A Case Study in Leveraging Manycore Coprocessors.\" In Proceedings of the International Parallel\n" +
                "//and Distributed Processing Symposium (IPDPS), May 2009.\n" +
                "//\n" +
                "//- S. Che, M. Boyer, J. Meng, D. Tarjan, J. W. Sheaffer, and K. Skadron. \"A Performance\n" +
                "//Study of General Purpose Applications on Graphics Processors using CUDA\" Journal of\n" +
                "//Parallel and Distributed Computing, Elsevier, June 2008.";
    }
}
