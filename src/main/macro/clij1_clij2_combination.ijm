// CLIJ example macro: clij1_clij2_combination.ijm
//
// This macro shows how to combine CLIJ1 and CLIJ2
//
// Author: Robert Haase
//         February 2020
// ---------------------------------------------

run("Close All");

// Get test data
run("Blobs (25K)");
run("8-bit");
rename("original");
getDimensions(width, height, channels, slices, frames)

newImage("background", "16-bit ramp", width, height, slices);

// init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJ2_clear();

// push images to GPU
Ext.CLIJ2_push("original");
Ext.CLIJ_push("background");


// cleanup imagej
run("Close All");

// create output image with 32 bits
Ext.CLIJ_create2D("originalWithBackground", width, height, 32);

// add images
Ext.CLIJ2_addImagesWeighted("original", "background", "originalWithBackground", 1, 0.01);

// show result
Ext.CLIJ2_pull("originalWithBackground");
run("Invert LUT");
