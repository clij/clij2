// CLIJ example macro: absolute.ijm
//
// This macro shows how get the absolute of an image
//
// Author: Robert Haase
// January 2019
// ---------------------------------------------

run("Close All");

// Get test data
width = 200;
height = 200;
slices = 1;
original = "original";
newImage(original, "32-bit ramp", width, height, slices);

// init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJ2_clear();

// push images to GPU
Ext.CLIJ2_push(original);

// cleanup imagej
run("Close All");

subtracted = "subtracted";
Ext.CLIJ2_addImageAndScalar(original, subtracted, -0.5);

absolute = "absolute";
Ext.CLIJ2_absolute(subtracted, absolute);

// show result
Ext.CLIJ2_pull(absolute);

