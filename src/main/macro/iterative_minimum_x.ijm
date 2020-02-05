// CLIJ example macro: iterative_mean.ijm
//
// This macro shows how to blur an image in the GPU
// using an iterative mean filter.
//
// Author: Robert Haase
//         December 20189
// ---------------------------------------------
run("Close All");

// Get test data
run("T1 Head (2.4M, 16-bits)");
input = getTitle();
getDimensions(width, height, channels, slices, frames);

blurred = "Blurred";
mean_blurred = "meanBlurred";

// Init GPU
run("CLIJ Macro Extensions", "cl_device=");
Ext.CLIJ2_clear();

// push images to GPU
Ext.CLIJ2_push(input);

// cleanup ImageJ
run("Close All");

// Blur in GPU
for (radius = 10; radius < 15; radius += 5) {
	time = getTime();
	Ext.CLIJ2_minimum3DSphere(input, blurred, radius, radius, radius);
	IJ.log("min sphere took " + (getTime() - time ));
	
	time = getTime();
	Ext.CLIJ2_minimum3DBox(input, blurred, radius, radius, radius);
	IJ.log("min box took " + (getTime() - time ));
}

// Get results back from GPU
// Ext.CLIJ_pull(blurred);

// Cleanup by the end
Ext.CLIJ2_clear();
