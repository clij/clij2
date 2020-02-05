// CLIJ example macro: addImages3D.ijm
//
// This macro shows how add images of different bit-type
//
// Author: Robert Haase
// January 2019
// ---------------------------------------------

run("Close All");

// Get test data
//run("Blobs (25K)");
//open("C:/structure/data/t1-head.tif");
run("T1 Head (2.4M, 16-bits)");
rename("original");
getDimensions(width, height, channels, slices, frames)

newImage("background", "16-bit ramp", width, height, slices);

// init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJx_clear();

// push images to GPU
Ext.CLIJx_push("original");
Ext.CLIJx_push("background");


// cleanup imagej
run("Close All");

// create output image with 32 bits
Ext.CLIJx_create3D("originalWithBackground", width, height, slices, 32);

// add images
Ext.CLIJx_addImagesWeighted("original", "background", "originalWithBackground", 1, 0.01);

// show result
Ext.CLIJx_pull("originalWithBackground");
