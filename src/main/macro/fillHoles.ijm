// CLIJ example macro: skeleton.ijm
//
// This macro shows how to fill holes in a 
// binary image in the GPU.
//
// Author: Robert Haase
//         March 2020
// ---------------------------------------------


// Get test data
open("https://github.com/clij/clij-advanced-filters/raw/master/src/test/resources/skeleton_test.tif");
input = getTitle();

filled_holes = "filled_holes";

// Init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJ2_clear();

// push data to GPU
Ext.CLIJ2_push(input);

// cleanup ImageJ
run("Close All");

// fill holes in the binary image
Ext.CLIJ2_binaryFillHoles(input, filled_holes);

// show result
Ext.CLIJ2_pullBinary(input);
Ext.CLIJ2_pullBinary(filled_holes);
