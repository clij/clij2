// CLIJ example macro: paste.ijm
//
// This macro shows how paste images into a big image
//
// Author: Robert Haase
// June 2019
// ---------------------------------------------

run("Close All");

// Get test data
open("http://fiji.sc/site/logo.png");
run("8-bit");
rename("logo");
getDimensions(width, height, channels, slices, frames)

// init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJ2_clear();

// push images to GPU
Ext.CLIJ2_push("logo");

// cleanup imagej
run("Close All");

// create output image with 32 bits
Ext.CLIJ2_create2D("map", 4096, 4096, 8);

// add images
Ext.CLIJ2_paste2D("logo", "map", 100, 100);
//Ext.CLIJx_paste2D("logo", "map", 2000, 2000);
Ext.CLIJ2_paste2D("logo", "map", 500, 1000);

// show result
Ext.CLIJ2_pull("map");
