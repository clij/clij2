// CLIJ example macro: skeleton3D.ijm
//
// This macro shows how to skeletonize a 
// binary image in the GPU.
//
// Author: Robert Haase
//         March 2020
// ---------------------------------------------


// Get test data
open("https://github.com/clij/clij-advanced-filters/raw/master/src/test/resources/skeleton_3d_test.tif");
input = getTitle();

skeleton = "skeleton";

// Init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJ2_clear();

// push data to GPU
Ext.CLIJ2_push(input);

// cleanup ImageJ
run("Close All");

// skeletonize
Ext.CLIJx_skeletonize(input, skeleton);

// show result
Ext.CLIJ2_pullBinary(input);
Ext.CLIJ2_pullBinary(skeleton);
