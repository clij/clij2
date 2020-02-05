// grid_GPU.ijm 
// 
// Draws a grid in an emppty image on the GPU.
// You need to install this update sites in Fiji:
//  * clij
//  * clij2
//
// Author: Robert Haase, rhaase@mpi-cbg.de
//         November 2019
//------------------------------------------------

// image size
width = 1000;
height = 1000;

// grid config
gridspacing = 50;
thickness = 1.1;

// define the image name
image = "image";

// initialize GPU
run("CLIJ Macro Extensions", "cl_device=[Intel(R) UHD Graphics 620]");

// create an empty image on the GPU
Ext.CLIJ2_create2D(image, width, height, 8);

// initialize the image with zeros
Ext.CLIJ2_set(image, 0);

for(x = 0; x < width; x += gridspacing) {
	for(y = 0; y < height; y += gridspacing) {
		// vertical line
		x1 = x;
		x2 = x;
		y1 = 0;
		y2 = height;
		z1 = 0;
		z2 = 0;
		Ext.CLIJ2_drawLine(image, x1, y1, z1, x2, y2, z2, thickness);

		// horizontal line
		x1 = 0;
		x2 = width;
		y1 = y;
		y2 = y;
		z1 = 0;
		z2 = 0;
		Ext.CLIJ2_drawLine(image, x1, y1, z1, x2, y2, z2, thickness);
	}
}
Ext.CLIJ2_pull(image);

