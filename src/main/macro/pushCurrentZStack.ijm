// CLIJ example macro: pushCurrentZStack.ijm
//
// This macro shows how the current Z-stack of an image is pushed to the GPU.
//
// Author: Robert Haase
//         September 2019
// ---------------------------------------------


// Get test data
run("Mitosis (26MB, 5D stack)");
input = getTitle();

// Init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJx_clear();

// push images to GPU
Ext.CLIJx_pushCurrentZStack(input);

run("Close All");

// pull the image back to see what it was
Ext.CLIJx_pull(input);
