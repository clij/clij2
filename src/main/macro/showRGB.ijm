// CLIJ example macro: showRG.ijm
//
// This macro shows how to visualise images as RGB
// 
//
// Author: Robert Haase
//         November 2019
// ---------------------------------------------

original = "original";
image1 = "image1";
image2 = "image2";

run("Close All");

// Get test data
run("Blobs (25K)");
run("8-bit");
rename(original);

// init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJx_clear();

// push images to GPU
Ext.CLIJx_push("original");

// cleanup imagej
run("Close All");

for (i = -10; i < 10; i += 1) {
	
	Ext.CLIJx_translate2D(original, image1, i, 0);
	Ext.CLIJx_translate2D(original, image2, 0, i);
	
	Ext.CLIJx_showRGB(original, image1, image2, "hello RGB");
	Ext.CLIJx_showGrey(image2, "hello grey");

	wait(500);
}