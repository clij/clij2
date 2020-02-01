// CLIJ example macro: allocateBig2DImages.ijm
//
// This macro shows how to process/handle big images in the GPU.
// Basically: They have to pushed in smaller blocks.
//
// Author: Robert Haase
// January 2020
// ---------------------------------------------

run ("Close All");


// Get test data
//open("C:/structure/data/blobs.gif");
run("Blobs (25K)");
getDimensions(width, height, channels, slices, frames);

// define image names
input = "input";
bigPlane = "bigPlane";
crop = "crop";

// Init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJ_clear();

// push images to GPU
rename(input);
Ext.CLIJ_push(input);

// CleanUp ImageJ
close();

// create a big image in GPU memory
big_image_width = 44469;
big_image_height = 39042;
Ext.CLIJ_create2D(bigPlane, big_image_width, big_image_height, 16);
Ext.CLIJx_getSize(bigPlane);


for (i = 0; i < big_image_width / width; i++) {
	// fill the image with content
	Ext.CLIJx_paste2D(input, bigPlane, i * width, 0);
}
Ext.CLIJ_crop2D(bigPlane, crop, 30000, 0, 1000, 1000);

// Get results back from GPU
Ext.CLIJ_pull(crop);

// report about what's allocated in the GPU memory
Ext.CLIJ_reportMemory();

// Cleanup by the end
Ext.CLIJ_clear();
