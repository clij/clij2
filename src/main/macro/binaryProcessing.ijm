// CLIJ example macro: binaryProcessing.ijm
//
// This macro shows how to deal with binary images, e.g. thresholding, dilation, erosion, in the GPU.
//
// Author: Robert Haase
// December 2018
// ---------------------------------------------

run("Close All");


// Get test data
run("Blobs (25K)");
//open("C:/structure/data/blobs.gif");
getDimensions(width, height, channels, slices, frames);
input = getTitle();
threshold = 128;

mask = "mask";

temp = "temp";


// Init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJx_clear();

// push data to GPU
Ext.CLIJx_push(input);

// cleanup ImageJ
run("Close All");

// create a mask using a fixed threshold
Ext.CLIJx_threshold(input, mask, threshold);

// binary opening: erosion + dilation, twice each
Ext.CLIJx_erodeBox(mask, temp);
Ext.CLIJx_erodeBox(temp, mask);

Ext.CLIJx_dilateBox(mask, temp);
Ext.CLIJx_dilateBox(temp, mask);


// show result
Ext.CLIJx_pullBinary(mask);


