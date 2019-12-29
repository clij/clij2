// CLIJ example macro: backgroundSubtraction.ijm
//
// This macro shows how background subtraction can be done in the GPU.
//
// Author: Robert Haase
// December 2018
// ---------------------------------------------

run ("Close All");

// Get test data
//open("C:/structure/data/t1-head.tif");
run("T1 Head (2.4M, 16-bits)");
input = getTitle();
background = "background";
background_subtracted = "background_subtracted";

// Init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJx_clear();

// push images to GPU
Ext.CLIJx_push(input);

// CleanUp ImageJ
close();

// Blur in GPU
Ext.CLIJx_blur3D(input, background, 10, 10, 1);

// subtraction from original
Ext.CLIJx_addImagesWeighted(input, background, background_subtracted, 1, -1);

// Get results back from GPU
Ext.CLIJx_pull(input);
Ext.CLIJx_pull(background_subtracted);

// Cleanup by the end
Ext.CLIJx_clear();
