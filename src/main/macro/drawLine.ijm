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
Ext.CLIJ_create2D("image", width, height, 8);

// draw a line
Ext.CLIJx_drawLine("image", 10, 10, 0, 50, 50, 0, 5);
Ext.CLIJx_powerImages(Image input, Image exponent, Image destination);

Ext.CLIJx_maskLabel(Image source, Image label_map, Image destination, Number label_index);
Ext.CLIJx_labelToMask(Image label_map_source, Image mask_destination, Number label_index);

// show result
Ext.CLIJ_pull("image");


