// CLIJ example macro: createImages.ijm
//
// This macro shows how to create images of different bit-type
//
// Author: Robert Haase
// January 2019
// ---------------------------------------------

run("Close All");

// init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJ2_clear();

width = 100;
height = 100;

// create output image with different bit-type
Ext.CLIJ2_create2D("image8bit", width, height, 8);
Ext.CLIJ2_create2D("image16bit", width, height, 16);
Ext.CLIJ2_create2D("image32bit", width, height, 32);


// show result
Ext.CLIJ2_pull("image8bit");
Ext.CLIJ2_pull("image16bit");
Ext.CLIJ2_pull("image32bit");


