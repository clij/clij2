// CLIJ example macro: pushSlice.ijm
//
// This macro shows how the current slice of an image is pushed to the GPU.
//
// Author: Robert Haase
//         September 2019
// ---------------------------------------------


// Get test data
run("T1 Head (2.4M, 16-bits)");
input = getTitle();

// Init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJ2_clear();

// push images to GPU
Ext.CLIJ2_pushCurrentSlice(input);

run("Close All");

// pull the image back to see what it was
Ext.CLIJ2_pull(input);
