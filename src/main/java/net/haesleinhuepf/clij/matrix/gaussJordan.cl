// source:
// https://github.com/qbunia/rodinia/blob/master/opencl/gaussian/gaussianElim_kernels.cl

//LICENSE TERMS
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

//#pragma OPENCL EXTENSION cl_khr_byte_addressable_store : enable


typedef struct latLong
{
    float lat;
    float lng;
} LatLong;

__kernel void Fan1(__global float *dst_m_dev,
                  __global float *src_a_dev,
                  const int size,
                  const int t) {
    int globalId = get_global_id(0);

    if (globalId < size-1-t) {
         dst_m_dev[size * (globalId + t + 1)+t] = src_a_dev[size * (globalId + t + 1) + t] /
                                                  src_a_dev[size * t + t];
    }
}


__kernel void Fan2(__global float *src_m_dev,
                  __global float *src_a_dev,
                  __global float *dst_b_dev,
                  const int size,
                  const int t) {
	 int globalId = get_global_id(0);

	 int globalIdx = get_global_id(0);
	 int globalIdy = get_global_id(1);
      if (globalIdx < size-1-t && globalIdy < size-t) {
         src_a_dev[size*(globalIdx+1+t)+(globalIdy+t)] -= src_m_dev[size*(globalIdx+1+t)+t] * src_a_dev[size*t+(globalIdy+t)];

 	    if(globalIdy == 0){
 		   dst_b_dev[globalIdx+1+t] -= src_m_dev[size*(globalIdx+1+t)+(globalIdy+t)] * dst_b_dev[t];
 	    }
 	 }
//   One dimensional
// 	 int globalIdx = globalId % size;
// 	 int globalIdy = globalId / size;
//
// 	 if (globalIdx < size-1-t && globalIdy < size-t) {
//          a_dev[size*(globalIdx+1+t)+(globalIdy+t)] -= m_dev[size*(globalIdx+1+t)+t] * a_dev[size*t+(globalIdy+t)];
// 	 }
// 	 if(globalIdy == 0){
//  		   b_dev[globalIdx+1+t] -= m_dev[size*(globalIdx+1+t)+(globalIdy+t)] * b_dev[t];
//      }

}


__kernel void BackSub(                  __global float *src_a_dev,
                                        __global float *src_b_dev,
                                        __global float *dst_finalVec,
                                        const int size) {

    // solve "bottom up"
	int i,j;
	for(i=0;i<size;i++){
		dst_finalVec[size-i-1]=src_b_dev[size-i-1];
		for(j=0;j<i;j++)
		{
			dst_finalVec[size-i-1]-=*(src_a_dev+size*(size-i-1)+(size-j-1)) * dst_finalVec[size-j-1];
		}
		dst_finalVec[size-i-1]=dst_finalVec[size-i-1]/ *(src_a_dev+size*(size-i-1)+(size-i-1));
	}
}