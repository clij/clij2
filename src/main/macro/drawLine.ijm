// CLIJ example macro: drawLine.ijm
//
// This macro shows how to draw a line into
// an image on the GPU
//
// Author: Robert Haase
//         September 2019
// ---------------------------------------------

run("Close All");

// init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJ_clear();

width = 100;
height = 100;

// create output image with different bit-type
Ext.CLIJx_create2D("image", width, height, 8);
Ext.CLIJx_set("image", 0);

// draw a line
Ext.CLIJx_drawLine("image", 10, 10, 0, 50, 50, 0, 5);

// show result
Ext.CLIJx_pull("image");


