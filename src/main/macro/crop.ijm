// CLIJ example macro: crop.ijm
//
// This macro shows how crop an image in the GPU
//
// Author: Robert Haase
//         September 2019
// ---------------------------------------------

run("Close All");

// Get test data
run("Blobs (25K)");
rename("original");

// init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJx_clear();

// push images to GPU
Ext.CLIJx_push("original");

// crop image
Ext.CLIJx_crop2D("original", "cropped", 10, 10, 75, 75);

// show result
Ext.CLIJx_pull("cropped");
run("Invert LUT");
