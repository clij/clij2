// CLIJ example macro: push.ijm
//
// This macro shows how an image is pushed to the GPU.
//
// Author: Robert Haase
// December 2018
// ---------------------------------------------


// Get test data
run("T1 Head (2.4M, 16-bits)");
input = getTitle();

// Init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJ2_clear();

// push images to GPU
Ext.CLIJ2_push(input);

Ext.CLIJ2_reportMemory();