// CLIJ example macro: blur_batch.ijm
//
// This macro shows how to blur a folder of 
// images in the GPU.
//
// Author: Robert Haase
//         November 2019
// ---------------------------------------------
run("Close All");

// set image names
input = "input";
blurred = "Blurred";

// Init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJ2_clear();

// set folders
path_to_results = "C:/structure/temp/";
// path_to_images = "/path/to/images/";
// files = getFileList(path_to_images)

// demo with online folder
path_to_images = "https://samples.fiji.sc/";
files = newArray("blobs.png", "blobs.png", "blobs.png");


// go through the folder
for (i = 0; i < lengthOf(files); i++) {
	open(path_to_images + files[i]);
	rename(input);

	// push images to GPU
	Ext.CLIJ_push(input);
	
	// cleanup ImageJ
	run("Close All");
	
	// Blur in GPU
	Ext.CLIJ2_gaussianBlur3D(input, blurred, 5, 5, 1);
	
	// Get results back from GPU
	Ext.CLIJ2_pull(blurred);

	// save result
	saveAs("tif", path_to_results + files[i]);
	
	// if your input images have different sizes, you should call clear now,
	// otherwise they may be cropped because memory of former images is reused.
	// Ext.CLIJ_clear();
	close();
}

// Cleanup by the end
Ext.CLIJ2_clear();
