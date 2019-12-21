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
Ext.CLIJ_clear();

// push images to GPU
Ext.CLIJ_push(input);

// cleanup ImageJ
run("Close All");

// Blur in GPU
for (radius = 5; radius < 50; radius += 5) {
	time = getTime();
	Ext.CLIJx_iterativeMean(input, mean_blurred, 25);
	IJ.log("iterative mean took " + (getTime() - time ));
	
	minimum = 1000000;
	min_sigma = 0;
	
	for (sigma = 1.5; sigma < 3; sigma += 0.1) {
		Ext.CLIJ_blur3D(input, blurred, sigma, sigma, sigma);
		Ext.CLIJx_meanSquaredError(blurred, mean_blurred);
		setResult("Sigma", nResults() - 1, sigma);
		value = getResult("MSE", nResults() - 1);
		if (minimum > value) {
			minimum = value;
			min_sigma = sigma;
		}
	}


	time = getTime();
	Ext.CLIJ_blur3D(input, blurred, min_sigma, min_sigma, min_sigma);
	IJ.log("blur took " + (getTime() - time ));

	IJ.log("r" + radius + " = s" + min_sigma);
}

// Get results back from GPU
// Ext.CLIJ_pull(blurred);

// Cleanup by the end
Ext.CLIJ_clear();
