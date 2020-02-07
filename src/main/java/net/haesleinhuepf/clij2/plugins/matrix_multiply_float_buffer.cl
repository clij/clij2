// Origin: https://github.com/CNugteren/myGEMM/blob/master/src/kernels.cl

// =================================================================================================
// Project:
// Exploring the performance of general matrix-multiplication on an NVIDIA Tesla K40m GPU.
//
// File information:
// Institution.... SURFsara <www.surfsara.nl>
// Author......... Cedric Nugteren <cedric.nugteren@surfsara.nl>
// Changed at..... 2014-11-06
// License........ MIT license
// Tab-size....... 4 spaces
// Line length.... 100 characters
//
// =================================================================================================
//
// Matrices in column-major format
// A: K columns, M rows
// B: N columns, K rows
// C: N columns, M rows
//
//                   N
//                o-----o
//                |     |
//              K | [B] |
//                |     |
//                o-----o
//        K          N
//    o-------o   o-----o
//  M |  [A]  | M | [C] |
//    |       |   |     |
//    o-------o   o-----o
//
//
// C-code for column-major matrix multiplication with alpha=1 and beta=0:
//
// for (int m=0; m<M; m++) {
//     for (int n=0; n<N; n++) {
//         float acc = 0.0f;
//         for (int k=0; k<K; k++) {
//             acc += A[k*M + m] * B[n*K + k];
//         }
//         C[n*M + m] = acc;
//     }
// }
//
// =================================================================================================


#define BK TSK
#define BN TSN
#define BM TSM
#define TX RTSM
#define TY RTSN
#define RX WPTM
#define RY WPTN

// With support for incomplete tiles and arbitrary input/output matrix sizes
__kernel void myGEMM10(const int M, const int N, const int K,
                       const __global floatX* A,
                       const __global floatX* B,
                       __global float* C) {

    // Thread identifiers
    const int tidm = get_local_id(0); // Local row ID (max: TSM/WPTM == RTSM)
    const int tidn = get_local_id(1); // Local col ID (max: TSN/WPTN == RTSN)
    const int gidm = get_group_id(0); // Work-group ID
    const int gidn = get_group_id(1); // Work-group ID
    const int tid = tidn*RTSM + tidm; // Global thread ID (max RTSM*RTSN)

    // Local memory to fit two tiles of A and B
    __local float Asub[2][TSK*TSM];
    __local float Bsub[2][TSK*TSN];

    // Allocate register space
    float Areg;
    float Breg[WPTN];
    float acc[WPTM][WPTN];

    // Initialise the accumulation registers
    #pragma unroll
    for (int wm=0; wm<WPTM; wm++) {
        #pragma unroll
        for (int wn=0; wn<WPTN; wn++) {
            acc[wm][wn] = 0.0f;
        }
    }

    // Tile A
    #pragma unroll
    for (int la=0; la<LPTA/WIDTH; la++) {
        int id = la*RTSN*RTSM + tid;
        int row = MOD2(id,TSM/WIDTH);
        int col = DIV2(id,TSM/WIDTH);

        // Load the value (wide vector load)
        int tiledIndex = TSK*0 + col;
        int indexA = tiledIndex*(M/WIDTH) + gidm*(TSM/WIDTH) + row;
        #ifdef USE_LDG
            floatX vecA = __ldg(&A[indexA]);
        #else
            floatX vecA = A[indexA];
        #endif

        // Store the loaded vector into local memory
        #if WIDTH == 1
            Asub[0][col*TSM + row] = vecA;
        #elif WIDTH == 2
            Asub[0][col*TSM + WIDTH*row + 0] = vecA.x;
            Asub[0][col*TSM + WIDTH*row + 1] = vecA.y;
        #elif WIDTH == 4
            Asub[0][col*TSM + WIDTH*row + 0] = vecA.x;
            Asub[0][col*TSM + WIDTH*row + 1] = vecA.y;
            Asub[0][col*TSM + WIDTH*row + 2] = vecA.z;
            Asub[0][col*TSM + WIDTH*row + 3] = vecA.w;
        #endif
    }

    // Tile B
    #pragma unroll
    for (int lb=0; lb<LPTB/WIDTH; lb++) {
        int id = lb*RTSN*RTSM + tid;
        int row = MOD2(id,TSN/WIDTH);
        int col = DIV2(id,TSN/WIDTH);

        // Load the value (wide vector load)
        int tiledIndex = TSK*0 + col;
        int indexB = tiledIndex*(N/WIDTH) + gidn*(TSN/WIDTH) + row;
        #ifdef USE_LDG
            floatX vecB = __ldg(&B[indexB]);
        #else
            floatX vecB = B[indexB];
        #endif

        // Store the loaded vector into local memory
        #if WIDTH == 1
            Bsub[0][col*TSN + row] = vecB;
        #elif WIDTH == 2
            Bsub[0][col*TSN + WIDTH*row + 0] = vecB.x;
            Bsub[0][col*TSN + WIDTH*row + 1] = vecB.y;
        #elif WIDTH == 4
            Bsub[0][col*TSN + WIDTH*row + 0] = vecB.x;
            Bsub[0][col*TSN + WIDTH*row + 1] = vecB.y;
            Bsub[0][col*TSN + WIDTH*row + 2] = vecB.z;
            Bsub[0][col*TSN + WIDTH*row + 3] = vecB.w;
        #endif
    }

    // Loop over all tiles
    const int numTiles = K/TSK;
    int t=0;
    do {

        // Synchronise
        barrier(CLK_LOCAL_MEM_FENCE);

        // Load the next tile of A and B into local memory
        int tt = t + 1;
        if (tt < numTiles) {

            // Tile A
            #pragma unroll
            for (int la=0; la<LPTA/WIDTH; la++) {
                int id = la*RTSN*RTSM + tid;
                int row = MOD2(id,TSM/WIDTH);
                int col = DIV2(id,TSM/WIDTH);

                // Load the value (wide vector load)
                int tiledIndex = TSK*tt + col;
                int indexA = tiledIndex*(M/WIDTH) + gidm*(TSM/WIDTH) + row;
                #ifdef USE_LDG
                    floatX vecA = __ldg(&A[indexA]);
                #else
                    floatX vecA = A[indexA];
                #endif

                // Store the loaded vector into local memory
                #if WIDTH == 1
                    Asub[tt%2][col*TSM + row] = vecA;
                #elif WIDTH == 2
                    Asub[tt%2][col*TSM + WIDTH*row + 0] = vecA.x;
                    Asub[tt%2][col*TSM + WIDTH*row + 1] = vecA.y;
                #elif WIDTH == 4
                    Asub[tt%2][col*TSM + WIDTH*row + 0] = vecA.x;
                    Asub[tt%2][col*TSM + WIDTH*row + 1] = vecA.y;
                    Asub[tt%2][col*TSM + WIDTH*row + 2] = vecA.z;
                    Asub[tt%2][col*TSM + WIDTH*row + 3] = vecA.w;
                #endif
            }

            // Tile B
            #pragma unroll
            for (int lb=0; lb<LPTB/WIDTH; lb++) {
                int id = lb*RTSN*RTSM + tid;
                int row = MOD2(id,TSN/WIDTH);
                int col = DIV2(id,TSN/WIDTH);

                // Load the value (wide vector load)
                int tiledIndex = TSK*tt + col;
                int indexB = tiledIndex*(N/WIDTH) + gidn*(TSN/WIDTH) + row;
                #ifdef USE_LDG
                    floatX vecB = __ldg(&B[indexB]);
                #else
                    floatX vecB = B[indexB];
                #endif

                // Store the loaded vector into local memory
                #if WIDTH == 1
                    Bsub[tt%2][col*TSN + row] = vecB;
                #elif WIDTH == 2
                    Bsub[tt%2][col*TSN + WIDTH*row + 0] = vecB.x;
                    Bsub[tt%2][col*TSN + WIDTH*row + 1] = vecB.y;
                #elif WIDTH == 4
                    Bsub[tt%2][col*TSN + WIDTH*row + 0] = vecB.x;
                    Bsub[tt%2][col*TSN + WIDTH*row + 1] = vecB.y;
                    Bsub[tt%2][col*TSN + WIDTH*row + 2] = vecB.z;
                    Bsub[tt%2][col*TSN + WIDTH*row + 3] = vecB.w;
                #endif
            }
        }

        // Loop over the values of a single tile
        #pragma unroll
        for (int k=0; k<TSK; k++) {

            // Cache the values of Bsub in registers
            #pragma unroll
            for (int wn=0; wn<WPTN; wn++) {
                int col = tidn + wn*RTSN;
                Breg[wn] = Bsub[t%2][k*TSN + col];
            }

            // Perform the computation
            #pragma unroll
            for (int wm=0; wm<WPTM; wm++) {
                int row = tidm + wm*RTSM;
                Areg = Asub[t%2][k*TSM + row];
                #pragma unroll
                for (int wn=0; wn<WPTN; wn++) {
                    acc[wm][wn] += Areg * Breg[wn];
                }
            }
        }

        // Next tile
        t++;
    } while (t<numTiles);

    // Store the final results in C
    #pragma unroll
    for (int wm=0; wm<WPTM; wm++) {
        int globalRow = gidm*TSM + tidm + wm*RTSM;
        #pragma unroll
        for (int wn=0; wn<WPTN; wn++) {
            int globalCol = gidn*TSN + tidn + wn*RTSN;
            C[globalCol*M + globalRow] = acc[wm][wn];
        }
    }
}
