// CLIJ example macro: rotate.ijm
//
// This macro shows how stacks can be rotated in the GPU
//
// Author: Robert Haase
// December 2018
// ---------------------------------------------
run("Close All");

// Get test data
run("T1 Head (2.4M, 16-bits)");
//open("C:/structure/data/t1-head.tif");
getDimensions(width, height, channels, slices, frames);
input = getTitle();

rotated = "Rotate left";

// Init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJ2_clear();

// push data to GPU
Ext.CLIJ2_push(input);

// rotate
Ext.CLIJ2_rotateCounterClockwise(input, rotated);

// show results
Ext.CLIJ2_pull(rotated);
