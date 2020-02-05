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
Ext.CLIJ2_clear();

// push images to GPU
Ext.CLIJ2_push("original");

// crop image
Ext.CLIJ2_crop2D("original", "cropped", 10, 10, 75, 75);

// show result
Ext.CLIJ2_pull("cropped");
run("Invert LUT");
