## gaussJordan
<img src="images/mini_empty_logo.png"/><img src="images/mini_empty_logo.png"/><img src="images/mini_clijx_logo.png"/>

By Robert Haase with code from (Shuai Che: sc5nf@cs.virginia.edu
and Kevin Skadron: skadron@cs.virginia.edu)

Gauss Jordan elimination algorithm for solving linear equation systems. Ent the equation coefficients as an n*n sized image A and an n*1 sized image B:
<pre>a(1,1)*x + a(2,1)*y + a(3,1)+z = b(1)
a(2,1)*x + a(2,2)*y + a(3,2)+z = b(2)
a(3,1)*x + a(3,2)*y + a(3,3)+z = b(3)
</pre>
The results will then be given in an n*1 image with values [x, y, z].

Adapted from: 
https://github.com/qbunia/rodinia/blob/master/opencl/gaussian/gaussianElim_kernels.cl
L.G. Szafaryn, K. Skadron and J. Saucerman. "Experiences Accelerating MATLAB Systems
//Biology Applications." in Workshop on Biomedicine in Computing (BiC) at the International
//Symposium on Computer Architecture (ISCA), June 2009.

### Usage in ImageJ macro
```
Ext.CLIJx_gaussJordan(Image A_matrix, Image B_result_vector, Image solution_destination);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer A_matrix = clij2.push(A_matrixImagePlus);
ClearCLBuffer B_result_vector = clij2.push(B_result_vectorImagePlus);
solution_destination = clij.create(A_matrix);
```

```
// Execute operation on GPU
clij2.gaussJordan(clij, A_matrix, B_result_vector, solution_destination);
```

```
//show result
solution_destinationImagePlus = clij2.pull(solution_destination);
solution_destinationImagePlus.show();

// cleanup memory on GPU
clij2.release(A_matrix);
clij2.release(B_result_vector);
clij2.release(solution_destination);
```




### License terms
/LICENSE TERMS  
//  
//Copyright (c)2008-2011 University of Virginia  
//All rights reserved.  
//  
//Redistribution and use in source and binary forms, with or without modification, are permitted without royalty fees or other restrictions, provided that the following conditions are met:  
//  
//* Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.  
//* Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.  
//* Neither the name of the University of Virginia, the Dept. of Computer Science, nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.  
//  
//THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE UNIVERSITY OF VIRGINIA OR THE SOFTWARE AUTHORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  
//  
//If you use this software or a modified version of it, please cite the most relevant among the following papers:  
//  
//- M. A. Goodrum, M. J. Trotter, A. Aksel, S. T. Acton, and K. Skadron. Parallelization of Particle Filter Algorithms. In Proceedings  
//of the 3rd Workshop on Emerging Applications and Many-core Architecture (EAMA), in conjunction with the IEEE/ACM International  
//Symposium on Computer Architecture (ISCA), June 2010.  
//  
//- S. Che, M. Boyer, J. Meng, D. Tarjan, J. W. Sheaffer, Sang-Ha Lee and K. Skadron.  
//"Rodinia: A Benchmark Suite for Heterogeneous Computing". IEEE International Symposium  
//on Workload Characterization, Oct 2009.  
//  
//- J. Meng and K. Skadron. "Performance Modeling and Automatic Ghost Zone Optimization  
//for Iterative Stencil Loops on GPUs." In Proceedings of the 23rd Annual ACM International  
//Conference on Supercomputing (ICS), June 2009.  
//  
//- L.G. Szafaryn, K. Skadron and J. Saucerman. "Experiences Accelerating MATLAB Systems  
//Biology Applications." in Workshop on Biomedicine in Computing (BiC) at the International  
//Symposium on Computer Architecture (ISCA), June 2009.  
//  
//- M. Boyer, D. Tarjan, S. T. Acton, and K. Skadron. "Accelerating Leukocyte Tracking using CUDA:  
//A Case Study in Leveraging Manycore Coprocessors." In Proceedings of the International Parallel  
//and Distributed Processing Symposium (IPDPS), May 2009.  
//  
//- S. Che, M. Boyer, J. Meng, D. Tarjan, J. W. Sheaffer, and K. Skadron. "A Performance  
//Study of General Purpose Applications on Graphics Processors using CUDA" Journal of  
//Parallel and Distributed Computing, Elsevier, June 2008.

[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
