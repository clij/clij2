// CLIJ example macro: minimum.ijm
//
// This macro shows how apply a minimum filter in the GPU
//
// Author: Robert Haase
// December 2018
// ---------------------------------------------
run("Close All");

// Get test data
run("T1 Head (2.4M, 16-bits)");
run("Duplicate...", "use");

//open("C:/structure/data/t1-head.tif");
getDimensions(width, height, channels, slices, frames);
input = getTitle();

minimum = "minimum";

// Init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJx_clear();

// push data to GPU
Ext.CLIJx_push(input);

// cleanup ImageJ
run("Close All");

// reslice
Ext.CLIJx_minimum3DBox(input, minimum, 3, 3, 3);

// show results
Ext.CLIJx_pull(input);
Ext.CLIJx_pull(minimum);

